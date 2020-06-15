package com.kitadigi.poskita.fragment.pos;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.dao.stok.StokHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.FileUtil;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StokController implements IStokRequest {


    private IStokResult iStokResult;
    private IStok iStok;
    Context context;
    SweetAlertDialog sweetAlertDialog;
    SessionManager sessionManager;
    String encryptedIdUsers;

    //untuk keperluan sync
    InternetChecker internetChecker;
    StokHelper stokHelper;
    ItemHelper itemHelper;
    Stok stok;
    String lokasi;

    boolean offlineMode;

    public StokController(IStokResult iStokResult, Context context, boolean offlineMode) {
        this.iStokResult = iStokResult;
        this.context = context;
        this.offlineMode = offlineMode;

        internetChecker=new InternetChecker();
        stokHelper=new StokHelper(context);
        itemHelper=new ItemHelper(context);

        //isi string lokasi dengan path
        //lihat fungsi picassoImageTarget di FileUtil.java
        lokasi= FileUtil.getFotoProduk(context);
    }

    public StokController(IStokResult iStokResult, Context context) {
        this.iStokResult = iStokResult;
        this.context = context;

        internetChecker=new InternetChecker();
        stokHelper=new StokHelper(context);
        itemHelper=new ItemHelper(context);

        //isi string lokasi dengan path
        //lihat fungsi picassoImageTarget di FileUtil.java
        lokasi= FileUtil.getFotoProduk(context);
    }

    @Override
    public void getStok() {


        if (offlineMode){

            //jika offline
            addFromItem();
        }else{

            //cek dulu apakah ada koneksi internet
            if (internetChecker.haveNetwork(context)){

                //jika ada koneksi internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
//            sweetAlertDialog.show();

                sessionManager=new SessionManager(context);
                encryptedIdUsers=sessionManager.getEncryptedIdUsers();

                //mulai tembak API
                iStok=StokUtil.getInterface();
                iStok.getStok(encryptedIdUsers).enqueue(new Callback<StokModel>() {
                    @Override
                    public void onResponse(Call<StokModel> call, Response<StokModel> response) {
                        Log.d("sukses url",call.request().url().toString());

                        //jika sukses nembak API
                        //hapus dulu data stok di sqlite
                        stokHelper.deleteAllStok();

                        //isikan semua stok di sqlite dengan hasil nembak API
                        for (StokDatum stokDatum : response.body().getData()){

                            stok=new Stok();
                            stok.setAdditional(stokDatum.getAdditional());
                            stok.setBrands_id(stokDatum.getBrands_id());
                            stok.setBrands_name(stokDatum.getBrands_name());
                            stok.setCategory_id(stokDatum.getCategory_id());
                            stok.setCode_product(stokDatum.getCode_product());
                            stok.setId(Long.parseLong(stokDatum.getId()));
                            stok.setName_category(stokDatum.getName_category());
                            stok.setName_product(stokDatum.getName_product());
                            stok.setName_sub_category(stokDatum.getName_sub_category());
                            stok.setProfits_precent(stokDatum.getProfits_precent());
                            stok.setPurchase_price(stokDatum.getPurchase_price());
                            stok.setQty_available(stokDatum.getQty_available());
                            stok.setSell_price(stokDatum.getSell_price());
                            stok.setSub_category_id(stokDatum.getSub_category_id());
                            stok.setTypes(stokDatum.getTypes());
                            stok.setUnits_id(stokDatum.getUnits_id());
                            stok.setUnits_name(stokDatum.getUnits_name());
                            stok.setKode_id(stokDatum.getKode_id());

                            //khusus untuk gambar, karena dihasilkan dari nembak API online,
                            //nilai dari image path-nya adalah disesuaikan dari fungsi
                            //dimana gambar disimpan, ada di FileUtil.java (lihat fungsi picassoImageTarget)
                            stok.setImage(lokasi + stokDatum.getImage());
                            Log.d("gbr stok sukses", stok.getImage());

                            //commit insert di sqlite
                            stokHelper.addStok(stok);

                            //simpan thumbnail gambar di hp user
                            //simpan gambar di hp user lewat picasso
                            //dengan nama file-nya = datum.getImage()
                            Picasso.with(context)
                                    .load( Url.DIKI_IMAGE_URL + stok.getImage())
                                    .into(FileUtil.picassoImageTarget(context,stok.getImage()));


                        }
                        iStokResult.onStokSuccess(response.body(),stokHelper.semuaStok());
//                    sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<StokModel> call, Throwable t) {
                        Log.d("gagal url",call.request().url().toString());
                        addFromItem();
//                    sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{

                //jika tidak ada koneksi internet
                addFromItem();

            }


        }



    }

    void addFromItem(){

            //jika gagal nembak API
            //buat list, tapi dari tabel item di sqlite
            //buat var list stok baru
            List<Stok> stokList = new ArrayList<>();

            //get semua item dari sqlite
            List<Item> items = itemHelper.semuaItem();

            //looping items lalu dimasukin ke stokList
            //untuk item yang sudah di-delete
            //jangan ikut dimasukkan dalam looping
            for (Item item: items){

                //jika sudah didelete
                if (item.getSync_delete()== Constants.STATUS_BELUM_SYNC ){

                }else{

                    //masukkan semua item ke stok controller atau layar POS
                    stok=new Stok();
                    stok.setKode_id(item.getKode_id());
                    stok.setBrands_id(item.getBrand_id().toString());
                    stok.setBrands_name(item.getBrand_name());
                    stok.setCategory_id(item.getCategory_id().toString());
                    stok.setCode_product(item.getCode_product());
                    stok.setId(item.getId());
                    stok.setName_category(item.getCategory_name());
                    stok.setName_product(item.getName_product());
                    stok.setPurchase_price(item.getPurchase_price().toString());
                    stok.setQty_available(item.getQty_stock());
                    stok.setSell_price(item.getSell_price().toString());
                    stok.setTypes(item.getTypes());
                    stok.setUnits_id(item.getUnit_id().toString());
                    stok.setUnits_name(item.getUnit_name());
                    stok.setImage(item.getImage());
                    stokList.add(stok);


                    //dapatkan semua list stok dari tabel sqlite
                    List<Stok> stokDariTabel = stokHelper.semuaStok();

                    //tambahkan dengan stokList
                    stokList.addAll(stokDariTabel);

                }
            }
            iStokResult.onStokError("", stokList);
    }
}
