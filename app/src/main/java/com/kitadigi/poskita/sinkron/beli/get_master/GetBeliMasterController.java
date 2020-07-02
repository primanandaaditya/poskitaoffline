package com.kitadigi.poskita.sinkron.beli.get_master;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.beli.Beli;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.sinkron.jual.get_master.GetJualMasterUtil;
import com.kitadigi.poskita.sinkron.jual.get_master.IGetJualMasterRequest;
import com.kitadigi.poskita.sinkron.jual.get_master.IGetJualMasterResult;
import com.kitadigi.poskita.sinkron.jual.get_master.ISinkronGetJualMaster;
import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;
import com.kitadigi.poskita.sinkron.jual.get_master.PenjualanMaster;
import com.kitadigi.poskita.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBeliMasterController implements IGetBeliMasterRequest {

    Context context;
    ISinkronGetBeliMaster iSinkronGetBeliMaster;
    IGetBeliMasterResult iGetBeliMasterResult;

    public GetBeliMasterController(Context context, IGetBeliMasterResult iGetBeliMasterResult) {
        this.context = context;
        this.iGetBeliMasterResult = iGetBeliMasterResult;
    }

    @Override
    public void getBeliMaster() {

        iSinkronGetBeliMaster = GetBeliMasterUtil.getInterface();
        iSinkronGetBeliMaster.getBeliMaster().enqueue(new Callback<GetBeliMasterModel>() {
            @Override
            public void onResponse(Call<GetBeliMasterModel> call, Response<GetBeliMasterModel> response) {

                Log.d("getbelimaster_sukses",call.request().url().toString() );

                //tampung respon dalam variabel
                GetBeliMasterModel getBeliMasterModel = response.body();

                //jika status = true
                if (getBeliMasterModel.getStatus()){

                    //cek jumlah data yang diterima
                    Integer jumlah = getBeliMasterModel.pembelian_master.size();

                    //jika 0
                    if (jumlah == 0){

                    }else{

                        Log.d("jumlah", jumlah.toString());
                        //
                        BeliMaster beliMaster;

                        //init sqlite
                        BeliMasterHelper beliMasterHelper = new BeliMasterHelper(context);

                        //hapus semua row
                        beliMasterHelper.deleteAllBeli();

                        //isi dengan yang baru
                        //looping dari model
                        List<PembelianMaster> pembelianMasters = getBeliMasterModel.getPembelian_master();

                        for (PembelianMaster pembelianMaster: pembelianMasters){

                            beliMaster = new BeliMaster();



                            beliMaster.setNomor(pembelianMaster.getMaster().getNomor());
                            beliMaster.setRef_no(pembelianMaster.getMaster().getRef_no());
                            beliMaster.setSupplier_id(pembelianMaster.getMaster().getSupplier_id());
                            beliMaster.setTanggal(pembelianMaster.getMaster().getTanggal());
                            beliMaster.setTotal_pay(Integer.parseInt(pembelianMaster.getMaster().getTotal_pay()));
                            beliMaster.setTotal_price(Integer.parseInt(pembelianMaster.getMaster().getTotal_price()));
                            beliMaster.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            beliMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            beliMaster.setSync_update(Constants.STATUS_SUDAH_SYNC);

                            //commit insert di sqlite
                            beliMasterHelper.addBeli(beliMaster);
                        }

                    }

                    iGetBeliMasterResult.onGetBeliMasterSuccess(getBeliMasterModel);
                }else{

                }

            }


            @Override
            public void onFailure(Call<GetBeliMasterModel> call, Throwable t) {

                Log.d("getbelimaster_eror",call.request().url().toString() );
                iGetBeliMasterResult.onGetBeliMasterError(t.getMessage());
            }
        });


    }
}
