package com.kitadigi.poskita.fragment.addunit;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAddUnit {

    @FormUrlEncoded
    @POST(Url.DIKI_ADD_UNIT + "/{id_users}")
    Call<BaseResponse> addUnit(
            @Path("id_users") String id_users,
            @Field("name_unit") String name_unit,
            @Field("code_unit") String code_unit

    );

}
