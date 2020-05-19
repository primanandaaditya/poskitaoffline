package com.kitadigi.poskita.activities.coba;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICoba {

    @GET("users")
    rx.Observable<CobaModel> getUser(@Query("page") String page);

}
