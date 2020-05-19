package com.kitadigi.poskita.sinkron.beli.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISinkronInsertBeli {
    @FormUrlEncoded
    @POST(Url.SINKRON_PEMBELIAN)
    Call<SinkronResponse> insert_beli(
            @Field("data") String data
    );
}
