package com.kitadigi.poskita.activities.registrasi;

import android.util.Log;

import com.kitadigi.poskita.activities.login.ILogin;
import com.kitadigi.poskita.activities.login.LoginActivityContract.GetLoginResultIntractor;
import com.kitadigi.poskita.activities.login.LoginResult;
import com.kitadigi.poskita.activities.login.LoginUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.GetRegistrasiResultIntractor;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.model.Status;

public class RegistrasiActivityImpl implements GetRegistrasiResultIntractor {


    String name;
    String province_id;
    String city_id;
    String subdistrict_id;
    String landmark;
    String telephone;
    String store_name;
    String type_store;
    String email;
    String annotation;
    IRegistrasi iRegistrasi;


    public RegistrasiActivityImpl(String name,
                                  String province_id,
                                  String city_id,
                                  String subdistrict_id,
                                  String landmark,
                                  String telephone,
                                  String store_name,
                                  String type_store,
                                  String email,
                                  String annotation) {
        this.email = email;
        this.name = name;
        this.province_id = province_id;
        this.city_id = city_id;
        this.subdistrict_id = subdistrict_id;
        this.landmark = landmark;
        this.telephone = telephone;
        this.store_name = store_name;
        this.type_store = type_store;
        this.annotation = annotation;

    }

    @Override
    public void getResultModel(final OnFinishedListener onFinishedListener) {

        iRegistrasi = RegistrasiUtil.getInterface();
        iRegistrasi.doRegistrasi(email,name,province_id,city_id,subdistrict_id,telephone,type_store,store_name,landmark,annotation).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.d("url register ok", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("url register gagal", call.request().url().toString());
                onFinishedListener.onFailure(t);
            }
        });




    }
}
