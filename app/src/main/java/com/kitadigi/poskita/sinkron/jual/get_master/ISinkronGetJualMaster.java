package com.kitadigi.poskita.sinkron.jual.get_master;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISinkronGetJualMaster {

    @GET(Url.SINKRON_PENJUALAN_GET_MASTER)
    Call<MasterModel> getMasterJual(
            @Header(Constants.auth_token) String auth_token
    );
}
