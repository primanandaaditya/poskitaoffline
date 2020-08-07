package com.kitadigi.poskita.sinkron.beli.get_detail;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterModel;
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterUtil;
import com.kitadigi.poskita.sinkron.beli.get_master.IGetBeliMasterRequest;
import com.kitadigi.poskita.sinkron.beli.get_master.IGetBeliMasterResult;
import com.kitadigi.poskita.sinkron.beli.get_master.ISinkronGetBeliMaster;
import com.kitadigi.poskita.sinkron.beli.get_master.PembelianMaster;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBeliDetailController implements IGetBeliDetailRequest {

    Context context;
    ISinkronGetBeliDetail iSinkronGetBeliDetail;
    IGetBeliDetailResult iGetBeliDetailResult;
    SessionManager sessionManager;
    String auth_token;

    public GetBeliDetailController(Context context, IGetBeliDetailResult iGetBeliDetailResult) {
        this.context = context;
        this.iGetBeliDetailResult = iGetBeliDetailResult;

    }

    @Override
    public void getBeliDetail() {

        sessionManager =new SessionManager(context);
        auth_token = sessionManager.getAuthToken();

        iSinkronGetBeliDetail = GetBeliDetailUtil.getInterface();
        iSinkronGetBeliDetail.getBeliDetail(auth_token).enqueue(new Callback<GetBeliDetailModel>() {
            @Override
            public void onResponse(Call<GetBeliDetailModel> call, Response<GetBeliDetailModel> response) {

                Log.d("belidetail sukses", call.request().url().toString());
                iGetBeliDetailResult.onGetBeliDetailSuccess(response.body());

                //kalau sukses
                //tampung respon dalam variabel
                GetBeliDetailModel getBeliDetailModel = response.body();

                //jika status = true
                if (getBeliDetailModel.getStatus().getMessage().equals(Constants.OK)){

                    //cek jumlah data yang diterima
                    Integer jumlah = getBeliDetailModel.pembelian_detail.size();

                    //jika 0
                    if (jumlah == 0){

                    }else{

                        Log.d("jumlah", jumlah.toString());
                        //
                        BeliDetail beliDetail;

                        //init sqlite
                        BeliDetailHelper beliDetailHelper = new BeliDetailHelper(context);

                        //hapus semua row
                        beliDetailHelper.deleteAllBeli();

                        //isi dengan yang baru
                        //looping dari model
                        List<PembelianDetail> pembelianDetails = getBeliDetailModel.getPembelian_detail();

                        for (PembelianDetail pembelianDetail : pembelianDetails){

                            beliDetail = new BeliDetail();

                            beliDetail.setKode_id_produk(pembelianDetail.getMobile_id_produk());
                            beliDetail.setNomor(pembelianDetail.getId_transaction());
                            beliDetail.setPrice(pembelianDetail.getPrice().toString());
                            beliDetail.setQty(pembelianDetail.getQty().toString());

                            //commit insert di sqlite
                            beliDetailHelper.addBeli(beliDetail);
                        }

                    }

                    iGetBeliDetailResult.onGetBeliDetailSuccess(getBeliDetailModel);
                }else{

                }
            }

            @Override
            public void onFailure(Call<GetBeliDetailModel> call, Throwable t) {

                Log.d("belidetail error", call.request().url().toString());
                iGetBeliDetailResult.onGetBeliDetailError(t.getMessage());
            }
        });


    }


}
