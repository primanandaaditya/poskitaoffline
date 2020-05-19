package com.kitadigi.poskita.fragment.editunit;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IEditUnit {

    @FormUrlEncoded
    @POST(Url.DIKI_EDIT_UNIT +  "/{id_unit}"+ "/{id_users}")
    Call<BaseResponse> editUnit(
            @Path("id_unit") String id_unit,
            @Path("id_users") String id_users,
            @Field("name_unit") String name_unit,
            @Field("code_unit") String code_unit
    );
}
