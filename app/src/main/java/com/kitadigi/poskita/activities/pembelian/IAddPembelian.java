package com.kitadigi.poskita.activities.pembelian;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAddPembelian {

    @FormUrlEncoded
    @POST(Url.DIKI_ADD_PEMBELIAN_URL + "/{id_users}")
    Call<BaseResponse> addPembelian(
            @Path("id_users") String id_users,
            @Field("supplier_id") String supplier_id,
            @Field("ref_no") String ref_no,
            @Field("id_product_master[]") String id_product_master,
            @Field("qty[]") String qty,
            @Field("price[]") String price,
            @Field("total_pay") Integer total_pay,
            @Field("total_price") Integer total_price

    );
}
