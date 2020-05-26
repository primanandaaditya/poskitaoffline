package com.kitadigi.poskita.fragment.edititem;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.fragment.additem.IAddBarangResult;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBarangController implements IEditRequest {

    IEditBarang iEditBarang;
    Context context;
    IAddBarangResult iAddBarangResult;
    SweetAlertDialog sweetAlertDialog;
    SessionManager sessionManager;
    String encryptedIdUsers;
    String id_product;
    String enkripIdProduct;

    //var ini sebagai konstruktor dari nama produk
    //karena di retrofit nama produk bukan @Field string
    //ada gambar yang diupload
    String nama_produk;

    //var ini sebagai konstruktor dari imagepath
    String imageFilePath;

    //untuk sync
    InternetChecker internetChecker;
    SimpleMD5 simpleMD5;
    ItemHelper itemHelper;
    Item item;

    boolean offlineMode;

    public EditBarangController(Context context, IAddBarangResult iAddBarangResult, String nama_produk, String imageFilePath, boolean offlineMode) {
        this.context = context;
        this.iAddBarangResult = iAddBarangResult;
        this.nama_produk = nama_produk;
        this.imageFilePath = imageFilePath;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
        itemHelper=new ItemHelper(context);
    }

    public EditBarangController(Context context, IAddBarangResult iAddBarangResult, String nama_produk, String imageFilePath) {
        this.context = context;
        this.iAddBarangResult = iAddBarangResult;
        this.nama_produk=nama_produk;
        this.imageFilePath=imageFilePath;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
        itemHelper=new ItemHelper(context);

    }

    @Override
    public void editBarang(String id_product, MultipartBody.Part image, final RequestBody name_product, final Integer brand_id, final Integer category_id, final Integer unit_id, final Integer purchase_price, final Integer sell_price, final String code_product, final Integer qty_stock, final Integer qty_minimum) {

        //temukan record untuk id yang bersangkutan
        item = itemHelper.getItemById(Long.parseLong(id_product));


        if (offlineMode){

            //jika gagal nembak API
            //edit barang di sqlite
            editOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum);

        }else{

            //cek koneksi internet dulu
            if (internetChecker.haveNetwork(context)){

                //jika ada koneksi internet
                sweetAlertDialog =new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //siapkan enkrip buat id produk
                enkripIdProduct = simpleMD5.generate(id_product);


                //mulai tembak API
                iEditBarang= EditBarangUtil.getInterface();

                iEditBarang.editBarang(image,
                        enkripIdProduct,
                        encryptedIdUsers,
                        name_product,
                        brand_id,
                        category_id,
                        unit_id,
                        purchase_price,
                        sell_price,
                        code_product,
                        qty_stock,
                        qty_minimum).enqueue(new Callback<AddBarangResult>() {
                    @Override
                    public void onResponse(Call<AddBarangResult> call, Response<AddBarangResult> response) {
                        iAddBarangResult.onSuccess(response.body());
                        Log.d("url edit product", call.request().url().toString());

                        //jika sukses, edit record item di sqlite
                        item.setName_product(nama_produk);
                        item.setImage(imageFilePath);
                        item.setCode_product(code_product);
                        item.setBrand_id(brand_id);
                        item.setCategory_id(category_id);
                        item.setUnit_id(unit_id);
                        item.setPurchase_price(purchase_price);
                        item.setSell_price(sell_price);
                        item.setQty_stock(qty_stock);
                        item.setQty_minimum(qty_minimum);
                        item.setSync_update(Constants.STATUS_SUDAH_SYNC);
                        itemHelper.updateItem(item);


                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<AddBarangResult> call, Throwable t) {

                        Log.d("upload foto edit error", t.getMessage().toString());
                        Log.d("url edit product error", call.request().url().toString());

                        //jika gagal nembak API
                        //edit barang di sqlite
                        editOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum);
                        sweetAlertDialog.dismissWithAnimation();
                        iAddBarangResult.onError(context.getResources().getString(R.string.tersimpan_offline));
                    }
                });
            }else{

                //jika tidak ada koneksi internet


                //jika gagal nembak API
                //edit barang di sqlite
                editOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum);
            }



        }




    }

    void editOffline(String code_product,Integer brand_id,Integer category_id,Integer unit_id,Integer purchase_price,Integer sell_price,Integer qty_stock,Integer qty_minimum){
        item.setName_product(nama_produk);
        item.setImage(imageFilePath);
        item.setCode_product(code_product);
        item.setBrand_id(brand_id);
        item.setCategory_id(category_id);
        item.setUnit_id(unit_id);
        item.setPurchase_price(purchase_price);
        item.setSell_price(sell_price);
        item.setQty_stock(qty_stock);
        item.setQty_minimum(qty_minimum);
        item.setSync_update(Constants.STATUS_BELUM_SYNC);
        itemHelper.updateItem(item);

        iAddBarangResult.onError(context.getResources().getString(R.string.tersimpan_offline));
    }
}
