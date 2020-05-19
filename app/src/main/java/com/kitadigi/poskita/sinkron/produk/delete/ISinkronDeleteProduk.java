package com.kitadigi.poskita.sinkron.produk.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Url;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ISinkronDeleteProduk {
    @FormUrlEncoded
    @POST(Url.SINKRON_DELETE_PRODUK)
    Call<SinkronResponse> delete_produk(
            @Field("data") String data
    );
}
