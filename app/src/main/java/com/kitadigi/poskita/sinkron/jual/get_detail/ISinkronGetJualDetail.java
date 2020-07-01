package com.kitadigi.poskita.sinkron.jual.get_detail;

import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ISinkronGetJualDetail {

    @GET(Url.SINKRON_PENJUALAN_GET_DETAIL)
    Call<GetDetailModel> getDetailJual(
    );
}
