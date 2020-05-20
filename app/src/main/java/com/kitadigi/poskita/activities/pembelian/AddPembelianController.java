package com.kitadigi.poskita.activities.pembelian;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.beli.Beli;
import com.kitadigi.poskita.dao.beli.BeliHelper;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.model.ArrayPosModel;
import com.kitadigi.poskita.model.BeliModel;
import com.kitadigi.poskita.model.ListBeliModel;
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

public class AddPembelianController implements IAddPembelianRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IAddPembelian iAddPembelian;
    IAddPembelianResult iAddPembelianResult;
    SessionManager sessionManager;
    String enkripIdUsers;

    //untuk sync
    InternetChecker internetChecker;
    BeliHelper beliHelper;
    Beli beli;
    BeliMasterHelper beliMasterHelper;
    BeliDetailHelper beliDetailHelper;
    BeliMaster beliMaster;
    BeliDetail beliDetail;

    boolean offlineMode;

    public AddPembelianController(Context context, IAddPembelianResult iAddPembelianResult, boolean offlineMode) {
        this.context = context;
        this.iAddPembelianResult = iAddPembelianResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        beliHelper=new BeliHelper(context);

        beliMasterHelper=new BeliMasterHelper(context);
        beliDetailHelper=new BeliDetailHelper(context);

    }

    public AddPembelianController(Context context, IAddPembelianResult iAddPembelianResult) {
        this.context = context;
        this.iAddPembelianResult = iAddPembelianResult;

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        beliHelper=new BeliHelper(context);

        beliMasterHelper=new BeliMasterHelper(context);
        beliDetailHelper=new BeliDetailHelper(context);


    }

    @Override
    public void addPembelian(final String supplier_id, final String ref_no, final Integer total_pay, final Integer total_price) {


        if (offlineMode){

            simpanOffline(supplier_id,ref_no,total_pay,total_price,false);
            iAddPembelianResult.onAddPembelianError("");

        }else{

            //cek koneksi internet dulu
            if (internetChecker.haveNetwork(context)){
                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.CONFIRM_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();


                iAddPembelian= AddPembelianUtil.getInterface();

                //siapkan 3 string untuk idproductmaster,qty dan price
                ArrayPosModel arrayPosModel = siapkanArray();

                iAddPembelian.addPembelian(
                        enkripIdUsers,
                        supplier_id,
                        ref_no,
                        arrayPosModel.getArrayIdProductMaster(),
                        arrayPosModel.getArrayQty(),
                        arrayPosModel.getArrayPrice(),
                        total_pay,
                        total_price
                ).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.d("url sukses", call.request().url().toString());
                        iAddPembelianResult.onAddPembelianSuccess(response.body());

                        //simpan di sqlite
                        //tapi sync insert = SUDAH SYNC
//                    simpanPembelianOffline(supplier_id,ref_no,total_pay,total_price,true);
                        simpanOffline(supplier_id,ref_no,total_pay,total_price,true);
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("url gagal", call.request().url().toString());


                        //jika gagal nembak API
                        //simpan di sqlite saja
                        //tapi sync insert = BELUM_SYNC
//                    simpanPembelianOffline(supplier_id,ref_no,total_pay,total_price,false);
                        simpanOffline(supplier_id,ref_no,total_pay,total_price,false);
                        iAddPembelianResult.onAddPembelianError(t.getMessage());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada koneksi internet
                //simpan di sqlite saja
                //tapi sync insert = BELUM_SYNC
                //simpanPembelianOffline(supplier_id,ref_no,total_pay,total_price,false);
                simpanOffline(supplier_id,ref_no,total_pay,total_price,false);
                iAddPembelianResult.onAddPembelianError("");

            }



        }




    }

    void simpanOffline(String supplier_id, String ref_no, Integer total_pay, Integer total_price, boolean sudahSync){

        //buat tanggal sekarang
        String tanggal = StringUtil.tanggalSekarang();

        //buat penomoran otomatis untuk tabel Master Detail penjualan di sqlite
        //tabel yang dilibatkan : JualMaster dan JualDetail
        String nomor = StringUtil.timeMilis();

        //siapkan listbeli dari session
        ListBeliModel listBeliModel = sessionManager.getPembelianOffline();

        Log.d("listbelimodel",String.valueOf(listBeliModel.getBeliModels().size()));

        //looping, untuk di-pass di tabel JualDetail
        for (BeliModel beliModel: listBeliModel.getBeliModels()){

            beliDetail =new BeliDetail();
            beliDetail.setKode_id_produk(beliModel.getId());
            beliDetail.setNomor(nomor);
            beliDetail.setPrice(beliModel.getPurchase_price().toString());
            beliDetail.setQty(beliModel.getQty().toString());

            //commit insert ke tabel JualDetail
            beliDetailHelper.addBeli(beliDetail);
        }

        //insert row untuk tabel BeliMaster
        beliMaster = new BeliMaster();
        beliMaster.setTanggal(tanggal);
        beliMaster.setSupplier_id(supplier_id);
        beliMaster.setNomor(nomor);
        beliMaster.setRef_no(ref_no);
        beliMaster.setTotal_pay(total_pay);
        beliMaster.setTotal_price(total_price);
        beliMaster.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        beliMaster.setSync_update(Constants.STATUS_SUDAH_SYNC);


        //tentukan sync_insert dari parameter sudahSync
        if (sudahSync){
            beliMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
        }else{
            beliMaster.setSync_insert(Constants.STATUS_BELUM_SYNC);
        }

        //commit insert di tabel BeliMaster
        beliMasterHelper.addBeli(beliMaster);
    }


    //fungsi ini tidak terpakai
    //==============================================================================================
    //==============================================================================================
    void simpanPembelianOffline(String supplier_id, String ref_no, Integer total_pay, Integer total_price, boolean sudahSync){

        //siapkan 3 string untuk idproductmaster,qty dan price
        ArrayPosModel arrayPosModel = siapkanArray();

        beli=new Beli();
        beli.setSupplier_id(supplier_id);
        beli.setRef_no(ref_no);
        beli.setId_product_master(arrayPosModel.getArrayIdProductMaster());
        beli.setPrice(arrayPosModel.getArrayPrice());
        beli.setQty(arrayPosModel.getArrayQty());
        beli.setTotal_pay(total_pay);
        beli.setTotal_price(total_price);
        beli.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        beli.setSync_update(Constants.STATUS_SUDAH_SYNC);
        if (sudahSync){
            beli.setSync_insert(Constants.STATUS_SUDAH_SYNC);
        }else{
            beli.setSync_insert(Constants.STATUS_BELUM_SYNC);
        }

        //commit insert
        beliHelper.addBeli(beli);

    }
    //==============================================================================================

    private ArrayPosModel siapkanArray(){

        //siapkan 3 array
        List<String> arrayIdProductMaster = new ArrayList<>();
        List<String> arrayQty               = new ArrayList<>();
        List<String> arrayPrice             = new ArrayList<>();

        //siapkan list beli dari session
        ListBeliModel listBeliModel = sessionManager.getPembelianOffline();

        //cek jumlahnya
        Log.d("jml", String.valueOf(listBeliModel.getBeliModels().size()));

        //looping, untuk di-pass di ketiga array
        for (BeliModel beliModel:listBeliModel.getBeliModels()){
            arrayIdProductMaster.add(beliModel.getId());
            arrayQty.add(beliModel.getQty().toString());
            arrayPrice.add(beliModel.getPurchase_price().toString());
        }



        //siapkan model untuk menampung
        ArrayPosModel arrayPosModel=new ArrayPosModel();

        //pass ketiga array di model
        arrayPosModel.setArrayIdProductMaster(StringUtil.StringArrayToArray(arrayIdProductMaster));
        arrayPosModel.setArrayQty(StringUtil.StringArrayToArray(arrayQty));
        arrayPosModel.setArrayPrice(StringUtil.StringArrayToArray(arrayPrice));

        return arrayPosModel;

    }

}
