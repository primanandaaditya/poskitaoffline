package com.kitadigi.poskita.sinkron.kategori.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISinkronUpdateKategori {
    @FormUrlEncoded
    @POST(Url.SINKRON_UPDATE_KATEGORI)
    Call<SinkronResponse> update_kategori(
            @Field("data") String data,
            @Header(Constants.auth_token) String auth_token
    );
}
