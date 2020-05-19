package com.kitadigi.poskita.fragment.addbrand;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddBrandUtil {
    private AddBrandUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddBrand getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddBrand.class);
    }
}
