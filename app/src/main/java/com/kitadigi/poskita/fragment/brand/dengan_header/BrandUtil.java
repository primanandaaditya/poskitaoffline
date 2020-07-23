package com.kitadigi.poskita.fragment.brand.dengan_header;

import com.kitadigi.poskita.fragment.brand.dengan_header.IBrand;
import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class BrandUtil {
    private BrandUtil() {}
    public static IBrand getInterface() {
        return RetrofitClient.getClient(Url.DIKI_URL).create(IBrand.class);
    }
}
