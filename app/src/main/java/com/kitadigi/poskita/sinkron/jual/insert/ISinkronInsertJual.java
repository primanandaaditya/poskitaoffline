package com.kitadigi.poskita.sinkron.jual.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISinkronInsertJual {
    @FormUrlEncoded
    @POST(Url.SINKRON_PENJUALAN)
    Call<SinkronResponse> insert_jual(
            @Header(Constants.auth_token) String auth_token,
            @Field("data") String data
    );
}
