package com.kitadigi.poskita.activities.registrasi;

import com.kitadigi.poskita.activities.login.LoginResult;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.model.Status;
import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IRegistrasi {

    @FormUrlEncoded
    @POST(Url.DIKI_REGISTER)
    Call<Status> doRegistrasi(
            @Field("email") String email,
            @Field("name") String nama,
            @Field("province_id") String province_id,
            @Field("city_id") String city_id,
            @Field("subdistrict_id") String subdistrict_id,
            @Field("telephone") String telepon,
            @Field("type_store") String jenis_toko,
            @Field("store_name") String store_name,
            @Field("landmark") String landmark,
            @Field("annotation") String annotation
    );

}
