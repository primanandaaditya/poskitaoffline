package com.kitadigi.poskita.sinkron.produk.insert;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.ResizeImage;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinkronInsertProdukController implements ISinkronAddProdukRequest {


    //CATATAN PENTING dari backend:
    //untuk jumlah produk yang akan diinsert harus sama dengan jumlah gambar yang diupload
    //jadi misalnya ada 2 produk yang akan diupload

    //ambil contoh: Miegoreng dan Aqua
    //Miegoreng ada gambarnya, tapi Aqua tidak ada gambarnya
    //nanti gambarnya tetap ada 2 yang diupload
    //yang satu gambar Miegoreng, yang satunya harus buat sendiri
    //gambarnya buat dari asset/image/icon_stock.png, (lihat fungsi kumpulkan data, yang dikomentar : //jika file gambar tidak ditemukan/dihapus)

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronAddProdukResult iSinkronAddProdukResult;
    ISinkronInsertProduk iSinkronInsertProduk;
    ItemHelper itemHelper;
    InternetChecker internetChecker;

    //untuk get business id
    SessionManager sessionManager;
    String business_id;
    String auth_token;

    //untuk multiple image
    File file, file_image_ready;
    List<MultipartBody.Part> multipartBodies = new ArrayList<MultipartBody.Part>();
    String image_ready;
    String kode_id,image;


    public SinkronInsertProdukController(Context context, ISinkronAddProdukResult iSinkronAddProdukResult) {
        this.context = context;
        this.iSinkronAddProdukResult = iSinkronAddProdukResult;

        internetChecker=new InternetChecker();
        sessionManager = new SessionManager(context);
        business_id = sessionManager.getBussinessId();
        auth_token = sessionManager.getAuthToken();
    }

    @Override
    public void insert_produk() {

        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();
            Log.d("data produk",data);

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
//                iSinkronAddProdukResult.onSinkronAddProdukError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

                iSinkronAddProdukResult.onSinkronAddProdukSuccess(null);
            }else{
                //jika ada data yg mau di-sync
                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));



                //buat request body untuk fungsi 'kumpulkan data' dulu
                RequestBody dataRequest = RequestBody.create(okhttp3.MultipartBody.FORM, data);


                //kumpulkan data yang mau di-sync
                iSinkronInsertProduk = AddProdukUtil.getInterface();
                Log.d("nas", String.valueOf(multipartBodies.size()));
                iSinkronInsertProduk.insert_produk(auth_token,dataRequest,multipartBodies.size() == 0 ? null : multipartBodies).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {

                        Log.d("sukses",call.request().url().toString());
                        Log.d("kanaeru", String.valueOf(response.code()) + response.toString());

                        SinkronResponse sinkronResponse = response.body();
                        Log.d("Respon Add Produk", sinkronResponse.getStatus().getCode());
                        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){

                            iSinkronAddProdukResult.onSinkronAddProdukSuccess(response.body());
                            ubahStatusSudahSync();

                        }else{

                            iSinkronAddProdukResult.onSinkronAddProdukError("Error add produk");
                        }


//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronAddProdukResult.onSinkronAddProdukError(t.getMessage());
//                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronAddProdukResult.onSinkronAddProdukError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }


    }

    @Override
    public String kumpulkan_data() {


        final MultipartBody multipartBody;

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        itemHelper = new ItemHelper(context);

        //get semua brand
        List<Item> items = itemHelper.semuaItem();

        //buat JSONArray
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;


        //looping brand list
        for (Item item : items){

            //tampung dalam variabel
            kode_id = item.getKode_id();

            //cek dulu apakah ada path gambar di sqlite
            if (item.getImage()==null || item.getImage().matches("")){

                //jika tidak ada gambar
                //isi dengan 'image/icon_stock'
                //artinya diisi dengan gambar yang terletak di folder /assets/image/icon_stock.png
                //lihat project folder

                image = Constants.ICON_STOCK_PATH;
            }else{
                //jika ada gambar
                image = item.getImage();
            }


            //jika ada yg belum sync insert
            if (item.getSync_insert() == Constants.STATUS_BELUM_SYNC){

                //persiapan upload image
                //cek dulu apakah item ada gambar
                if (image != ""){

                    //siapkan file baru berdasarkan path image
                    file = new File(image);


                    //cek dulu apakah ada gambarnya di hp user
                    if (file.exists()){

                        //resize image
                        image_ready= ResizeImage.resizeAndCompressImageBeforeSend(context, file.getPath(), kode_id + ".jpg");

                        //gambar baru yang sudah dikompress
                        file_image_ready = new File(image_ready);

                        //buat request body di retrofit
                        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file_image_ready);

                        //bentuk array image yang akan diupload
                        multipartBodies.add(MultipartBody.Part.createFormData("upload[]", file_image_ready.getName(), imageBody));

                        //persiapan upload image SELESAI sampai baris diatas

                    }else{

                        //jika file gambar tidak ditemukan/dihapus
                        //buat file dulu, namanya 'icon_stock.png'
                        File f = new File(context.getCacheDir(), Constants.ICON_STOCK);

                        try {

                            //buat file baru
                            f.createNewFile();

                            //get bitmap dari asset/image/icon_stock.png
                            Bitmap bitmap = StringUtil.getBitmapFromAsset(context, Constants.ICON_STOCK_PATH);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            //write the bytes in file
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();

                            //resize image
                            image_ready= ResizeImage.resizeAndCompressImageBeforeSend(context, f.getPath(), Constants.ICON_STOCK);

                            //gambar baru yang sudah dikompress
                            file_image_ready = new File(image_ready);

                            //buat request body di retrofit
                            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file_image_ready);

                            //bentuk array image yang akan diupload
                            multipartBodies.add(MultipartBody.Part.createFormData("upload[]", file_image_ready.getName(), imageBody));

                            //persiapan upload image SELESAI sampai baris diatas

                        }catch (Exception e){

                        }

                    }
                }

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {

                    //nama json
                    jsonObject.put("nama", item.getName_product());
                    jsonObject.put("mobile_id", item.getKode_id());
//                    jsonObject.put("business_id",business_id);
                    jsonObject.put("category_mobile_id", item.getCategory_mobile_id());
                    jsonObject.put("brand_mobile_id", item.getBrand_mobile_id());
                    jsonObject.put("unit_mobile_id", item.getUnit_mobile_id());
                    jsonObject.put("sell_price",item.getSell_price().toString());
                    jsonObject.put("purchase_price",item.getPurchase_price().toString());

                    if (item.getCode_product()==null){
                        item.setCode_product("");
                    }
                    jsonObject.put("code_product",item.getCode_product().toString());
                    jsonObject.put("qty_stock",item.getQty_stock().toString());
                    jsonObject.put("qty_minimum",item.getQty_minimum().toString());
                    jsonObject.put("tax_type","inclusive");
//                    jsonObject.put("enable_stock","0");
//                    jsonObject.put("sku","");
//                    jsonObject.put("enable_sr_no","0");
//                    jsonObject.put("is_inactive","0");
//                    jsonObject.put("not_for_selling","0");
                    jsonObject.put("created_by","1");

                    //khusus untuk gambar
                    //dalam hal sinkronisasi ini
                    //bisa ditemukan kemungkinan
                    //bahwa produk bisa saja tidak memakai gambar, jadi cuma diisi nama dan harganya saja
                    //jadi di dalam lemparan ini, nama gambar diganti jadi 'kode_id + .jpg'
                    //tujuannya adalah supaya sinkron dengan looping dengan array file (variabel  upload[jumlah] ) diatas
                    //dalam variabel 'upload[jumlah]', nama file sudah disesuaikan dengan kode_id
                    if (image ==""){
                        jsonObject.put("image",Constants.ICON_STOCK);
                    }else {
                        jsonObject.put("image",kode_id + ".jpg");
                    }

                    //tambahkan ke JSON
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //cek apakah ada data yang di-sync atau tidak
        //jika ada buat JSON
        //jika tidak, return String kosongan
        if (jumlah == 0){
            //jika tidak ada data yang di-sync
            //return String kosongan
            hasil = "[]";
        }else{
            hasil =  jsonArray.toString();
            Log.d("jsonarray item", hasil);
        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {

        //init sqlite
        itemHelper = new ItemHelper(context);

        //get semua brand
        List<Item> items = itemHelper.semuaItem();

        //looping unit list
        for (Item item : items){

            //jika ada yg belum sync insert
            if (item.getSync_insert() == Constants.STATUS_BELUM_SYNC){
                item.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                itemHelper.updateItem(item);
            }
        }
    }


}
