package com.kitadigi.poskita.activities.coba;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CobaInterface {

    @GET("users")
    rx.Observable<CobaModel> getUser(
      @QueryMap HashMap<String,String> params
    );

}
