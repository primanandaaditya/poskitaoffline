package com.kitadigi.poskita.sinkron.jual.get_detail;

import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ISinkronGetJualDetail {

    @GET(Url.SINKRON_PENJUALAN_GET_DETAIL)
    Call<GetDetailModel> getDetailJual(
            @Header(Constants.auth_token) String auth_token
    );
}
