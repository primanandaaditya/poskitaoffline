package com.kitadigi.poskita.sinkron.produk.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ISinkronUpdateProduk {
    @Multipart
    @POST(Url.SINKRON_UPDATE_PRODUK)
    Call<SinkronResponse> update_produk(
            @Header(Constants.auth_token) String auth_token,
            @Part("data") RequestBody data,
            @Part List<MultipartBody.Part> upload
    );
}
