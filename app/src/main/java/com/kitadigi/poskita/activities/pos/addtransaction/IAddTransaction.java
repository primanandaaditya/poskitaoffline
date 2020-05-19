package com.kitadigi.poskita.activities.pos.addtransaction;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAddTransaction {
    @FormUrlEncoded
    @POST(Url.DIKI_ADD_TRANSACTION_URL + "/{id_users}")
    Call<BaseResponse> addTransaction(
            @Path("id_users") String id_users,
            @Field("id_product_master[]") String id_product_master,
            @Field("qty[]") String qty,
            @Field("price[]") String price,
            @Field("contact_id") String contact_id,
            @Field("total_pay") Integer total_pay,
            @Field("total_price") Integer total_price

    );
}
