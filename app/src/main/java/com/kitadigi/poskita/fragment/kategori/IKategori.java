package com.kitadigi.poskita.fragment.kategori;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IKategori {


    @GET(Url.DIKI_MASTER_KATEGORI + "/{id_users}")
    Call<KategoriModel> getKategoriList(
            @Path("id_users") String id_users
    );
}
