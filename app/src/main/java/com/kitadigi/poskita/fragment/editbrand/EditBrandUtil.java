package com.kitadigi.poskita.fragment.editbrand;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class EditBrandUtil {
    private EditBrandUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IEditBrand getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IEditBrand.class);
    }
}
