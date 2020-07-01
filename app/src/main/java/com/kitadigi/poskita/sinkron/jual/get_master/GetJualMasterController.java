package com.kitadigi.poskita.sinkron.jual.get_master;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetJualMasterController implements IGetJualMasterRequest {

    Context context;
    ISinkronGetJualMaster iSinkronGetJualMaster;
    IGetJualMasterResult iGetJualMasterResult;

    public GetJualMasterController(Context context, IGetJualMasterResult iGetJualMasterResult) {
        this.context = context;
        this.iGetJualMasterResult = iGetJualMasterResult;
    }

    @Override
    public void getJualMaster() {
        iSinkronGetJualMaster = GetJualMasterUtil.getInterface();
        iSinkronGetJualMaster.getMasterJual().enqueue(new Callback<MasterModel>() {
            @Override
            public void onResponse(Call<MasterModel> call, Response<MasterModel> response) {
                Log.d("sukses", call.request().url().toString());

                iGetJualMasterResult.onGetJualMasterSuccess(response.body());

                //kalau sukses
                //hapus data penjualan master di sqlite
                //dan isi dengan yang baru

                MasterModel masterModel = response.body();

                if (masterModel.status==true){

                    //cek jumlah data yang diterima
                    Integer jumlah = masterModel.penjualan_master.size();

                    //jika 0
                    if (jumlah == 0){

                    }else{

                        Log.d("jumlah", jumlah.toString());
                        //
                        JualMaster jualMaster;

                        //init sqlite
                        JualMasterHelper jualMasterHelper = new JualMasterHelper(context);

                        //hapus semua row
                        jualMasterHelper.deleteAllJual();

                        //isi dengan yang baru
                        //looping dari model
                        List<PenjualanMaster> penjualan_master = masterModel.getPenjualan_master();

                        for (PenjualanMaster penjualanMaster: penjualan_master){

                            jualMaster = new JualMaster();

                            jualMaster.setContact_id(penjualanMaster.getMaster().getContact_id());
                            jualMaster.setNomor(penjualanMaster.getMaster().getNomor());
                            jualMaster.setTanggal(penjualanMaster.getMaster().getTanggal());
                            jualMaster.setTotal_item(Integer.parseInt(penjualanMaster.getMaster().getTotal_item()));
                            jualMaster.setTotal_pay(Integer.parseInt(penjualanMaster.getMaster().getTotal_pay()));
                            jualMaster.setTotal_price(Integer.parseInt(penjualanMaster.getMaster().getTotal_price()));
                            jualMaster.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            jualMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            jualMaster.setSync_update(Constants.STATUS_SUDAH_SYNC);

                            //commit insert di sqlite
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
