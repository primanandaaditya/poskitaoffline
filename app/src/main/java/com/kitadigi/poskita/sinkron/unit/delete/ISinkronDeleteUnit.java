package com.kitadigi.poskita.sinkron.unit.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISinkronDeleteUnit {
    @FormUrlEncoded
    @POST(Url.SINKRON_DELETE_UNIT)
    Call<SinkronResponse> delete_unit(
            @Field("data") String data
    );
}
