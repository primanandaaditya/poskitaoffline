package com.kitadigi.poskita.fragment.kategori.dengan_header;

import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriModel;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IKategori {


    @GET(Url.DIKI_MASTER_KATEGORI)
    Call<KategoriModel> getKategoriList(
            @Header("auth_token") String auth_token
    );
}
