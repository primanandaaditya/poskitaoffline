package com.kitadigi.poskita.fragment.deletebrand;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class DeleteBrandUtil {

    private DeleteBrandUtil() {};

    public static final String BASE_URL = Url.DIKI_URL;

    public static IDeleteBrand getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IDeleteBrand.class);
    }
}
