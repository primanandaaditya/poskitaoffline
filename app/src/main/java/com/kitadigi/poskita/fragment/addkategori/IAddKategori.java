package com.kitadigi.poskita.fragment.addkategori;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAddKategori {
    @FormUrlEncoded
    @POST(Url.DIKI_ADD_KATEGORI + "/{id_users}")
    Call<BaseResponse> addKategori(
            @Path("id_users") String id_users,
            @Field("name_category") String name_category,
            @Field("code_category") String code_category

    );
}
