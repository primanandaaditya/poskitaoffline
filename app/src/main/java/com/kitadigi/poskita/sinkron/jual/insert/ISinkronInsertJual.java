package com.kitadigi.poskita.sinkron.jual.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISinkronInsertJual {
    @FormUrlEncoded
    @POST(Url.SINKRON_PENJUALAN)
    Call<SinkronResponse> insert_jual(
            @Field("data") String data
    );
}
