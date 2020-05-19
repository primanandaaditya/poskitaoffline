package com.kitadigi.poskita.fragment.addbrand;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAddBrand {
    @FormUrlEncoded
    @POST(Url.DIKI_ADD_BRAND + "/{id_users}")
    Call<BaseResponse> addBrand(
            @Path("id_users") String id_users,
            @Field("name") String name,
            @Field("description") String description

    );
}
