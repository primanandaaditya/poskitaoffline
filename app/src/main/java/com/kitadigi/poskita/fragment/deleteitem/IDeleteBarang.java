package com.kitadigi.poskita.fragment.deleteitem;

import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.util.Url;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IDeleteBarang {

    @FormUrlEncoded
    @POST(Url.DIKI_ITEM_DELETE_URL + "/{id_product}" + "/{id_users}")
    rx.Observable<AddBarangResult> deleteBarang(
            @Path("id_product") String id_product,
            @Path("id_users") String id_users,
            @Field("kosong") String kosong
    );

}
