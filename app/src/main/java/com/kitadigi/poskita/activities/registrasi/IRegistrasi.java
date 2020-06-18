package com.kitadigi.poskita.activities.registrasi;

import com.kitadigi.poskita.activities.login.LoginResult;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IRegistrasi {

    @FormUrlEncoded
    @POST(Url.SINKRON_REGISTRASI)
    Call<BaseResponse> doRegistrasi(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("telepon") String telepon,
            @Field("jenis_toko") String jenis_toko,
            @Field("nama_toko") String nama_toko,
            @Field("alamat_toko") String alamat_toko,
            @Field("alamat_pemilik") String alamat_pemilik,
            @Field("keterangan") String keterangan
    );

}
