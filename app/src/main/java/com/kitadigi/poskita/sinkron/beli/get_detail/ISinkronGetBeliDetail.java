package com.kitadigi.poskita.sinkron.beli.get_detail;

import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterModel;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ISinkronGetBeliDetail {

    @GET(Url.SINKRON_PEMBELIAN_GET_DETAIL)
    Call<GetBeliDetailModel> getBeliDetail();
}
