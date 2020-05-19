package com.kitadigi.poskita.fragment.deleteunit;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IDeleteUnit {

    @FormUrlEncoded
    @POST(Url.DIKI_DELETE_UNIT + "/{id_unit}" + "/{id_users}")
    Call<BaseResponse> deleteUnit(
            @Path("id_unit") String id_unit,
            @Path("id_users") String id_users,
            @Field("kosongan") String kosongan

    );
}
