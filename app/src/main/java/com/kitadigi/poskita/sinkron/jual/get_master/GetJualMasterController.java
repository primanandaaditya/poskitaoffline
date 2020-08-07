package com.kitadigi.poskita.sinkron.jual.get_master;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetJualMasterController implements IGetJualMasterRequest {

    Context context;
    ISinkronGetJualMaster iSinkronGetJualMaster;
    IGetJualMasterResult iGetJualMasterResult;
    SessionManager sessionManager;
    String auth_token;

    public GetJualMasterController(Context context, IGetJualMasterResult iGetJualMasterResult) {
        this.context = context;
        this.iGetJualMasterResult = iGetJualMasterResult;


    }

    @Override
    public void getJualMaster() {

        sessionManager = new SessionManager(context);
        auth_token = sessionManager.getAuthToken();

        iSinkronGetJualMaster = GetJualMasterUtil.getInterface();
        iSinkronGetJualMaster.getMasterJual(auth_token).enqueue(new Callback<MasterModel>() {
            @Override
            public void onResponse(Call<MasterModel> call, Response<MasterModel> response) {
                Log.d("sukses", call.request().url().toString());

                iGetJualMasterResult.onGetJualMasterSuccess(response.body());

                //kalau sukses
                //hapus data penjualan master di sqlite
                //dan isi dengan yang baru

                MasterModel masterModel = response.body();

                if (masterModel.getStatus().getMessage().equals(Constants.OK)){

                    //cek jumlah data yang diterima
                    Integer jumlah = masterModel.penjualan_master.size();

                    //jika 0
                    if (jumlah == 0){

                    }else{

                        Log.d("jumlah penjualan master", jumlah.toString());
                        //
                        JualMaster jualMaster;

                        //init sqlite
                        JualMasterHelper jualMasterHelper = new JualMasterHelper(context);

                        //hapus semua row
                        jualMasterHelper.deleteAllJual();

                        //isi dengan yang baru
                        //looping dari model
                        List<PenjualanMaster> penjualan_master = masterModel.getPenjualan_master();

                        Log.d("jml penjualn master", String.valueOf(penjualan_master.size()));

                        for (PenjualanMaster penjualanMaster: penjualan_master){

                            jualMaster = new JualMaster();

                            jualMaster.setContact_id(penjualanMaster.getContact_id());
                            jualMaster.setNomor(penjualanMaster.getId_transaction());
                            jualMaster.setTanggal(penjualanMaster.getTransaction_date().substring(0,10));
//                            jualMaster.setTotal_item(Integer.parseInt(penjualanMaster.getTotal_item()));
                            jualMaster.setTotal_pay(Integer.parseInt(penjualanMaster.getTotal_pay()));
                            jualMaster.setTotal_price(Integer.parseInt(penjualanMaster.getTotal_price()));
                            jualMaster.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            jualMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            jualMaster.setSync_update(Constants.STATUS_SUDAH_SYNC);

                            //commit insert di sqlite
                            jualMasterHelper.addJualMaster(jualMaster);
                        }

                    }



                }else{

                }



            }

            @Override
            public void onFailure(Call<MasterModel> call, Throwable t) {
                Log.d("gagal", call.request().url().toString());
                iGetJualMasterResult.onGetJualMasterError(t.getMessage());
            }
        });
    }
}
