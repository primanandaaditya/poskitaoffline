package com.kitadigi.poskita.fragment.unit;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IUnit {

    @GET(Url.DIKI_MASTER_UNIT + "/{id_users}")
    Call<UnitModel> getUnitList(
            @Path("id_users") String id_users
    );
}
