package com.kitadigi.poskita.activities.login;

import android.util.Log;

import com.kitadigi.poskita.activities.login.LoginActivityContract.GetLoginResultIntractor;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityImpl implements GetLoginResultIntractor {


    String email,password;
    ILogin iLogin;

    public LoginActivityImpl(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void getResultModel(final OnFinishedListener onFinishedListener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("username", email);
        params.put("password",password);

        iLogin=LoginUtil.getLoginInterface();
        iLogin.doLogin(email,params).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                onFinishedListener.onFinished(response.body());
                Log.d("URL", call.request().url().toString());
//                Log.d("Sampel", response.body().message);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d("URL", call.request().url().toString());
            }
        });

    }
}
