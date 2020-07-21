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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBeliDetailController implements IGetBeliDetailRequest {

    Context context;
    ISinkronGetBeliDetail iSinkronGetBeliDetail;
    IGetBeliDetailResult iGetBeliDetailResult;

    public GetBeliDetailController(Context context, IGetBeliDetailResult iGetBeliDetailResult) {
        this.context = context;
        this.iGetBeliDetailResult = iGetBeliDetailResult;
    }

    @Override
    public void getBeliDetail() {

        iSinkronGetBeliDetail = GetBeliDetailUtil.getInterface();
        iSinkronGetBeliDetail.getBeliDetail().enqueue(new Callback<GetBeliDetailModel>() {
            @Override
            public void onResponse(Call<GetBeliDetailModel> call, Response<GetBeliDetailModel> response) {

                Log.d("belidetail sukses", call.request().url().toString());
                iGetBeliDetailResult.onGetBeliDetailSuccess(response.body());

                //kalau sukses
                //tampung respon dalam variabel
                GetBeliDetailModel getBeliDetailModel = response.body();

                //jika status = true
                if (getBeliDetailModel.getStatus()){

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

                            beliDetail.setKode_id_produk(pembelianDetail.getDetail().getMobile_id());
                            beliDetail.setNomor(pembelianDetail.getDetail().getNomor());
                            beliDetail.setPrice(pembelianDetail.getDetail().getPrice());
                            beliDetail.setQty(pembelianDetail.getDetail().getQty());

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