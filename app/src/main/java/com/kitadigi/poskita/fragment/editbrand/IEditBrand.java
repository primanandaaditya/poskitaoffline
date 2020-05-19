package com.kitadigi.poskita.fragment.editbrand;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IEditBrand {

    @FormUrlEncoded
    @POST(Url.DIKI_EDIT_BRAND +  "/{id_brand}"+ "/{id_users}")
    Call<BaseResponse> editBrand(
            @Path("id_brand") String id_brand,
            @Path("id_users") String id_users,
            @Field("name") String name,
            @Field("description") String description

    );
}
