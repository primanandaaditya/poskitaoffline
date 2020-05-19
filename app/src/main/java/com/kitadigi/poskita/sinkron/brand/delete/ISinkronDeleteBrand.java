package com.kitadigi.poskita.sinkron.brand.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISinkronDeleteBrand {
    @FormUrlEncoded
    @POST(Url.SINKRON_DELETE_BRAND)
    Call<SinkronResponse> delete_brand(
            @Field("data") String data
    );
}
