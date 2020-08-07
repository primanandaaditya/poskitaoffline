package com.kitadigi.poskita.sinkron.jual.get_detail;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
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

public class GetJualDetailController implements IGetJualDetailRequest {

    Context context;
    ISinkronGetJualDetail iSinkronGetJualDetail;
    IGetJualDetailResult iGetJualDetailResult;
    SessionManager sessionManager;
    String auth_token;


    public GetJualDetailController(Context context, IGetJualDetailResult iGetJualDetailResult) {
        this.context = context;
        this.iGetJualDetailResult = iGetJualDetailResult;

    }


    @Override
    public void getJualDetail() {

        sessionManager = new SessionManager(context);
        auth_token = sessionManager.getAuthToken();

        iSinkronGetJualDetail = GetJualDetailUtil.getInterface();
        iSinkronGetJualDetail.getDetailJual(auth_token).enqueue(new Callback<GetDetailModel>() {
            @Override
            public void onResponse(Call<GetDetailModel> call, Response<GetDetailModel> response) {
                Log.d("sukses", call.request().url().toString());
                iGetJualDetailResult.onGetJualDetailSuccess(response.body());



                //kalau sukses
                //hapus data penjualan detail di sqlite
                //dan isi dengan yang baru

                GetDetailModel getDetailModel = response.body();
                Log.d("status", String.valueOf(getDetailModel.status));

                if (getDetailModel.getStatus().getMessage().equals(Constants.OK)){

                    //cek jumlah data yang diterima
                    Integer jumlah = getDetailModel.getPenjualan_detail().size();

                    //jika 0
                    if (jumlah == 0){

                    }else{

                        Log.d("jumlah", jumlah.toString());
                        //
                        JualDetail jualDetail;

                        //init sqlite
                        JualDetailHelper jualDetailHelper = new JualDetailHelper(context);


                        //hapus semua row
                        jualDetailHelper.deleteAllJual();

                        //isi dengan yang baru
                        //looping dari model
                        List<PenjualanDetail> penjualan_detail = getDetailModel.getPenjualan_detail();

                        for (PenjualanDetail penjualanDetail: penjualan_detail){


                            jualDetail = new JualDetail();

                            jualDetail.setKode_id(penjualanDetail.getMobile_id_produk());
                            jualDetail.setNomor(penjualanDetail.getId_transaction());
                            jualDetail.setPrice(penjualanDetail.getPrice());
                            jualDetail.setQty(penjualanDetail.getQty());

                            //commit insert di sqlite
                            //commit insert di sqlite
                            jualDetailHelper.addJualDetail(jualDetail);

                        }
                    }

                }else{

                }

            }

            @Override
            public void onFailure(Call<GetDetailModel> call, Throwable t) {
                Log.d("gagal", call.request().url().toString());
                iGetJualDetailResult.onGetJualDetailError(t.getMessage());
            }
        });
    }
}
