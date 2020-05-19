package com.kitadigi.poskita.activities.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

public interface ILogin {

        @FormUrlEncoded
        @POST(Url.LOGIN)
        Call<LoginResult> doLogin(
          @Field("usernam") String username,
          @QueryMap HashMap<String,String> params
        );

}
