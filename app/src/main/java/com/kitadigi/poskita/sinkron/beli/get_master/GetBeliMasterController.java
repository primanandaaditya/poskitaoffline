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
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBeliMasterController implements IGetBeliMasterRequest {

    Context context;
    ISinkronGetBeliMaster iSinkronGetBeliMaster;
    IGetBeliMasterResult iGetBeliMasterResult;
    SessionManager sessionManager;
    String auth_token;

    public GetBeliMasterController(Context context, IGetBeliMasterResult iGetBeliMasterResult) {
        this.context = context;
        this.iGetBeliMasterResult = iGetBeliMasterResult;


    }

    @Override
    public void getBeliMaster() {

        sessionManager = new SessionManager(context);
        auth_token = sessionManager.getAuthToken();

        iSinkronGetBeliMaster = GetBeliMasterUtil.getInterface();
        iSinkronGetBeliMaster.getBeliMaster(auth_token).enqueue(new Callback<GetBeliMasterModel>() {
            @Override
            public void onResponse(Call<GetBeliMasterModel> call, Response<GetBeliMasterModel> response) {

                Log.d("getbelimaster_sukses",call.request().url().toString() );

                //tampung respon dalam variabel
                GetBeliMasterModel getBeliMasterModel = response.body();

                //jika status = true
                if (getBeliMasterModel.getStatus().getMessage().equals(Constants.OK)){

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


                            beliMaster.setNomor(pembelianMaster.getId_transaction());
                            beliMaster.setRef_no(pembelianMaster.getRef_no());
                            beliMaster.setSupplier_id(pembelianMaster.getSupplier_id());
                            beliMaster.setTanggal(pembelianMaster.getTransaction_date().substring(0,10));
                            beliMaster.setTotal_pay(pembelianMaster.getTotal_pay());
                            beliMaster.setTotal_price(pembelianMaster.getTotal_price());
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
