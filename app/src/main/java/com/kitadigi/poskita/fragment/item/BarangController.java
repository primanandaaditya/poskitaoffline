package com.kitadigi.poskita.fragment.item;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.FileUtil;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarangController implements IBarangRequest {

    private IBarangResult iBarangResult;
    private IBarang iBarang;
    Context context;
    SweetAlertDialog progressDialog;
    SessionManager sessionManager;
    String encryptedIdUsers;

    //untuk sync
    InternetChecker internetChecker;
    ItemHelper itemHelper;
    Item item;

    //untuk lokasi foto produk
    String lokasi;
    boolean showDialog=true;

    boolean offlineMode;

    public BarangController(IBarangResult iBarangResult, Context context) {
        this.iBarangResult = iBarangResult;
        this.context = context;
        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        itemHelper=new ItemHelper(context);

        //isi string lokasi dengan path
        //lihat fungsi picassoImageTarget di FileUtil.java
        lokasi=FileUtil.getFotoProduk(context);
    }

    public BarangController( Context context, IBarangResult iBarangResult, boolean offlineMode) {
        this.iBarangResult = iBarangResult;
        this.context = context;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        itemHelper=new ItemHelper(context);

        //isi string lokasi dengan path
        //lihat fungsi picassoImageTarget di FileUtil.java
        lokasi=FileUtil.getFotoProduk(context);
    }

    @Override
    public void getBarang() {


        if (offlineMode){

            iBarangResult.onError("", itemHelper.getAllItem());

        }else{

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){

                //jika ada internet
                progressDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                progressDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                progressDialog.setCancelable(false);
//            if ((showDialog == true)) {
//                progressDialog.show();
//            } else {
//
//            }

                iBarang=BarangUtil.getInterface();
                iBarang.getBarang(encryptedIdUsers).enqueue(new Callback<BarangResult>() {
                    @Override
                    public void onResponse(Call<BarangResult> call, Response<BarangResult> response) {

                        //ini langkah untuk memasukkan data online
                        //ke database offline
                        //hapus dulu semua item di sqlite
                        itemHelper.deleteAllItem();

                        //looping hasil item
                        //masukkan semua data online ke sqlite
                        for (Datum datum:response.body().getData()){

                            item=new Item();

                            item.setBrand_id(Integer.parseInt(datum.getBrands_id()));
                            item.setCategory_id(Integer.parseInt(datum.getCategory_id()));
                            item.setUnit_id(Integer.parseInt(datum.getUnits_id()));
                            item.setCode_product(datum.getCode_product());
                            item.setId(Long.parseLong(datum.getId().toString()));
                            item.setName_product(datum.getName_product());
                            item.setPurchase_price(Integer.parseInt(datum.getPurchase_price()));
                            item.setQty_minimum(Integer.parseInt(datum.getQty_minimum()));
                            item.setQty_stock(Integer.parseInt(datum.getQty_stock()));
                            item.setTypes(datum.getTypes());
                            item.setCategory_name(datum.getName_category());
                            item.setUnit_name(datum.getUnits_name());
                            item.setBrand_name(datum.getBrands_name());
                            item.setSell_price(Integer.parseInt(datum.getSell_price()));
                            item.setAdditional(datum.getAdditional());
                            item.setKode_id(datum.getMobile_id());
                            item.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            item.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            item.setSync_update(Constants.STATUS_SUDAH_SYNC);

                            //khusus untuk gambar, karena dihasilkan dari nembak API online,
                            //nilai dari image path-nya adalah disesuaikan dari fungsi
                            //dimana gambar disimpan, ada di FileUtil.java (lihat fungsi picassoImageTarget)
                            item.setImage(lokasi + datum.getImage());

                            //commit insert di sqlite
                            itemHelper.addItem(item);

                            //simpan thumbnail gambar di hp user
                            //simpan gambar di hp user lewat picasso
                            //dengan nama file-nya = datum.getImage()
                            Picasso.with(context)
                                    .load( Url.DIKI_IMAGE_URL + datum.getImage())
                                    .into(FileUtil.picassoImageTarget(context,datum.getImage()));


                        }

                        iBarangResult.onSuccess(response.body(), itemHelper.getAllItem());
                        Log.d("url_barang", call.request().url().toString());
//                    if ((showDialog == true)) {
//                        progressDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }

                    @Override
                    public void onFailure(Call<BarangResult> call, Throwable t) {
                        Log.d("url_barang", call.request().url().toString());

                        //jika gagal nembak API

                        iBarangResult.onError(t.getMessage(), itemHelper.getAllItem());
//                    if ((showDialog == true)) {
//                        progressDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }
                });
            }else{
                //jika tidak ada internet
                iBarangResult.onError("", itemHelper.getAllItem());
            }



        }



    }


}
