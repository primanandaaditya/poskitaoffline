package com.kitadigi.poskita.fragment.item.dengan_header;

import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IBarang {

    @GET(Url.DIKI_ITEM_URL+Url.DIKI_ITEM_LIST_URL)
    Call<BarangResult> getBarang(
            @Header(Constants.auth_token) String auth_token
    );


}
