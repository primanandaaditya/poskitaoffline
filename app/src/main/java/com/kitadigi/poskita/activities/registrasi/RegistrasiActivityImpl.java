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

public class RegistrasiActivityImpl implements GetRegistrasiResultIntractor {


    String email,nama,telepon,jenis_toko,nama_toko,alamat_toko,alamat_pemilik,keterangan;
    IRegistrasi iRegistrasi;


    public RegistrasiActivityImpl(String email, String nama, String telepon, String jenis_toko, String nama_toko, String alamat_toko, String alamat_pemilik, String keterangan) {
        this.email = email;
        this.nama = nama;
        this.telepon = telepon;
        this.jenis_toko = jenis_toko;
        this.nama_toko = nama_toko;
        this.alamat_toko = alamat_toko;
        this.alamat_pemilik = alamat_pemilik;
        this.keterangan = keterangan;

    }

    @Override
    public void getResultModel(final OnFinishedListener onFinishedListener) {

        iRegistrasi = RegistrasiUtil.getInterface();
        iRegistrasi.doRegistrasi(email,nama,telepon,jenis_toko,nama_toko,alamat_toko,alamat_pemilik,keterangan).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });


    }
}
