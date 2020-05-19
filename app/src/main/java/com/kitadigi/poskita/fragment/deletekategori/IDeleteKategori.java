package com.kitadigi.poskita.fragment.deletekategori;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IDeleteKategori {

    @FormUrlEncoded
    @POST(Url.DIKI_DELETE_KATEGORI + "/{id_kategori}" + "/{id_users}")
    Call<BaseResponse> deleteKategori(
            @Path("id_kategori") String id_kategori,
            @Path("id_users") String id_users,
            @Field("kosongan") String kosongan

    );

}
