package com.kitadigi.poskita.fragment.item;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBarang {

    @GET(Url.DIKI_ITEM_URL+Url.DIKI_ITEM_LIST_URL+ "/{id_users}")
    Call<BarangResult> getBarang(
            @Path("id_users") String id_users
    );


}
