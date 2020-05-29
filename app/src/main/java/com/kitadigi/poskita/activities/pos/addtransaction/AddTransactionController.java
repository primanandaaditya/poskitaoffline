package com.kitadigi.poskita.activities.pos.addtransaction;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.jual.Jual;
import com.kitadigi.poskita.dao.jual.JualHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.model.ArrayPosModel;
import com.kitadigi.poskita.model.JualModel;
import com.kitadigi.poskita.model.ListJualModel;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionController implements IAddTransactionRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IAddTransaction iAddTransaction;
    IAddTransactionResult iAddTransactionResult;
    SessionManager sessionManager;
    String enkripIdUsers;


    //untuk sybnc
    InternetChecker internetChecker;
    JualHelper jualHelper;
    Jual jual;
    JualMasterHelper jualMasterHelper;
    JualDetailHelper jualDetailHelper;
    JualMaster jualMaster;
    JualDetail jualDetail;

    boolean offlineMode;


    public AddTransactionController(Context context, IAddTransactionResult iAddTransactionResult) {
        this.context = context;
        this.iAddTransactionResult = iAddTransactionResult;

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        jualHelper=new JualHelper(context);
        jualMasterHelper = new JualMasterHelper(context);
        jualDetailHelper = new JualDetailHelper(context);

    }

    public AddTransactionController(Context context, IAddTransactionResult iAddTransactionResult, boolean offlineMode) {
        this.context = context;
        this.iAddTransactionResult = iAddTransactionResult;
        this.offlineMode = offlineMode;


        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        jualHelper=new JualHelper(context);
        jualMasterHelper = new JualMasterHelper(context);
        jualDetailHelper = new JualDetailHelper(context);
    }

    @Override
    public void addTransaction(final String contact_id, final int total_pay, final int total_price) {

        if (offlineMode){
            //jika mode offline
            simpanPenjualanOffline(contact_id,total_pay,total_price,false);
            iAddTransactionResult.onAddTransactionError(context.getResources().getString(R.string.tersimpan_offline));

        }else {

            //cek dulu apakah ada koneksi internet
            if (internetChecker.haveNetwork(context)){

                //jika ada koneksi internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.CONFIRM_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                iAddTransaction=AddTransactionUtil.getInterface();

                //siapkan 3 string untuk idproductmaster,qty dan price
                ArrayPosModel arrayPosModel = siapkanArray();

                //tembak API
                iAddTransaction.addTransaction(
                        enkripIdUsers,
                        arrayPosModel.getArrayIdProductMaster(),
                        arrayPosModel.getArrayQty(),
                        arrayPosModel.getArrayPrice(),
                        contact_id,
                        total_pay,
                        total_price)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                Log.d("url sukses",call.request().url().toString());
                                Log.d("request body", call.request().toString());


                                //simpan di sqlite
                                //tapi sync insert = SUDAH SYNC
//                            simpanPenjualanKeSQLite(contact_id,total_pay,total_price,false);
                                simpanPenjualanOffline(contact_id,total_pay,total_price,true);

                                iAddTransactionResult.onAddTransactionSuccess(response.body());
                                sweetAlertDialog.dismissWithAnimation();
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.d("url gagal",call.request().url().toString());

                                //jika gagal nembak API
                                //simpan di sqlite saja
                                //tapi sync insert = BELUM_SYNC
//                            simpanPenjualanKeSQLite(contact_id,total_pay,total_price,false);
                                simpanPenjualanOffline(contact_id,total_pay,total_price,false);
                                iAddTransactionResult.onAddTransactionError(context.getResources().getString(R.string.tersimpan_offline));
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });


            }else{
                //jika tidak ada koneksi internet
                //simpan di sqlite saja
                //tapi sync insert = BELUM_SYNC
//            simpanPenjualanKeSQLite(contact_id,total_pay,total_price,false);
                simpanPenjualanOffline(contact_id,total_pay,total_price,false);
                iAddTransactionResult.onAddTransactionError(context.getResources().getString(R.string.tersimpan_offline));


            }



        }



    }


    //ini fungsi yang tidak usah digunakan!!!!!!
    //===============================================================================================
    void simpanPenjualanKeSQLite(String contact_id, int total_pay, int total_price, boolean sudahSync){

        //siapkan 3 string untuk idproductmaster,qty dan price
        ArrayPosModel arrayPosModel = siapkanArray();

        jual=new Jual();
        jual.setContact_id(contact_id);
        jual.setId_product_master(arrayPosModel.getArrayIdProductMaster());
        jual.setPrice(arrayPosModel.getArrayPrice());
        jual.setQty(arrayPosModel.getArrayQty());
        jual.setTotal_pay(total_pay);
        jual.setTotal_price(total_price);
        jual.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        jual.setSync_update(Constants.STATUS_SUDAH_SYNC);
        if (sudahSync){
            jual.setSync_insert(Constants.STATUS_SUDAH_SYNC);
        }else{
            jual.setSync_insert(Constants.STATUS_BELUM_SYNC);
        }

        //commit insert
        jualHelper.addJual(jual);

    }
    //===============================================================================================
    //===============================================================================================

    private ArrayPosModel siapkanArray(){

        //siapkan 3 array
        List<String> arrayIdProductMaster   = new ArrayList<>();
        List<String> arrayQty               = new ArrayList<>();
        List<String> arrayPrice             = new ArrayList<>();

        //siapkan listjualmodel dari session
        ListJualModel listJualModel = sessionManager.getPenjualanOffline();

        //looping, untuk di-pass di ketiga array
        for (JualModel jualModel: listJualModel.getJualModels()){
            arrayIdProductMaster.add(jualModel.getId());
            arrayQty.add(jualModel.getQty().toString());
            arrayPrice.add(jualModel.getSell_price().toString());
        }

        //siapkan model untuk menampung
        ArrayPosModel arrayPosModel=new ArrayPosModel();

        //pass ketiga array di model
        arrayPosModel.setArrayIdProductMaster(StringUtil.StringArrayToArray(arrayIdProductMaster));
        arrayPosModel.setArrayQty(StringUtil.StringArrayToArray(arrayQty));
        arrayPosModel.setArrayPrice(StringUtil.StringArrayToArray(arrayPrice));

        return arrayPosModel;

    }


    //fungsi ini untuk menyimpan ke sqlite
    //melalui tabel JualMaster dan JualDetail
    private void simpanPenjualanOffline(String contact_id, int total_pay, int total_price,boolean sudahSync){

        //buat var integer, untuk di-set ke kolom totalitem di tabel JualMaster sqlite
        Integer totalqty;
        Integer totalitem = 0;

        //buat string untuk tanggal sekarang
        String tanggal = StringUtil.tanggalSekarang();

        //buat penomoran otomatis untuk tabel Master Detail penjualan di sqlite
        //tabel yang dilibatkan : JualMaster dan JualDetail
        String nomor = StringUtil.timeMilis();

        //siapkan listjualmodel dari session
        ListJualModel listJualModel = sessionManager.getPenjualanOffline();

        //looping, untuk di-pass di tabel JualDetail
        for (JualModel jualModel: listJualModel.getJualModels()){

            //tampung dulu qty di variabel
            totalqty = jualModel.getQty();

            //tambahkan qty diatas dalam var totalitem
            //totalitem nanti di-assign di kolom totalitem di tabel jualmaster
            totalitem = totalitem + totalqty;

            jualDetail =new JualDetail();
            jualDetail.setKode_id(jualModel.getId());
            jualDetail.setNomor(nomor);
            jualDetail.setPrice(jualModel.getSell_price());
            jualDetail.setQty(jualModel.getQty());


            //commit insert ke tabel JualDetail
            jualDetailHelper.addJualDetail(jualDetail);
        }

        //insert row untuk tabel JualMaster
        jualMaster = new JualMaster();
        jualMaster.setTanggal(tanggal);
        jualMaster.setContact_id(contact_id);
        jualMaster.setNomor(nomor);
        jualMaster.setTotal_item(totalitem);
        jualMaster.setTotal_pay(total_pay);
        jualMaster.setTotal_price(total_price);
        jualMaster.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        jualMaster.setSync_update(Constants.STATUS_SUDAH_SYNC);

        //tentukan sync_insert dari parameter sudahSync
        if (sudahSync){
            jualMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
        }else{
            jualMaster.setSync_insert(Constants.STATUS_BELUM_SYNC);
        }

        //commit insert di tabel JualMaster
        jualMasterHelper.addJualMaster(jualMaster);

    }

}

