package com.kitadigi.poskita.fragment.pos;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IStok {

    @GET(Url.DIKI_STOK_JUAL_URL+ "/{id_users}")
    Call<StokModel> getStok(
            @Path("id_users") String id_users
    );

}
