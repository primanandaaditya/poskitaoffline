package com.kitadigi.poskita.fragment.editkategori;


import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IEditKategori {
    @FormUrlEncoded
    @POST(Url.DIKI_EDIT_KATEGORI +  "/{id_kategori}"+ "/{id_users}")
    Call<BaseResponse> editKategori(
            @Path("id_kategori") String id_kategori,
            @Path("id_users") String id_users,
            @Field("name_category") String name_category,
            @Field("code_category") String code_category

    );
}
