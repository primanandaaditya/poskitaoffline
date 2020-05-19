package com.kitadigi.poskita.sinkron.kategori.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAddKategori {
    @FormUrlEncoded
    @POST(Url.SINKRON_INSERT_KATEGORI)
    Call<SinkronResponse> insert_kategori(
            @Field("data") String data
    );
}
