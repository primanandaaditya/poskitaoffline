package com.kitadigi.poskita.fragment.brand.dengan_header;

import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IBrand {
    @GET(Url.DIKI_MASTER_BRAND)
    Call<BrandModel> getBrandList(
            @Header(Constants.auth_token) String auth_token
    );
}
