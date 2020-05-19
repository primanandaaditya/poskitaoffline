package com.kitadigi.poskita.fragment.edititem;


import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.util.Url;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IEditBarang {
    @Multipart
    @POST(Url.DIKI_ITEM_EDIT_URL + "/{id_product}" +  "/{id_users}")


    Call<AddBarangResult> editBarang(
            @Part MultipartBody.Part image,
            @Path("id_product") String id_product,
            @Path("id_users") String id_users,
            @Part("name_product") RequestBody name_product,
            @Part("brand_id") Integer brand_id,
            @Part("category_id") Integer category_id,
            @Part("unit_id") Integer unit_id,
            @Part("purchase_price") Integer purchase_price,
            @Part("sell_price") Integer sell_price,
            @Part("code_product") String code_product,
            @Part("qty_stock") Integer qty_stock,
            @Part("qty_minimum") Integer qty_minimum);
}
