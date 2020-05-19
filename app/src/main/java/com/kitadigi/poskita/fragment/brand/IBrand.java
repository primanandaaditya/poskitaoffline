package com.kitadigi.poskita.fragment.brand;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBrand {
    @GET(Url.DIKI_MASTER_BRAND + "/{id_users}")
    Call<BrandModel> getBrandList(
            @Path("id_users") String id_users
    );
}
