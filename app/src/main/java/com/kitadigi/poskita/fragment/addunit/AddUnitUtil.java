package com.kitadigi.poskita.fragment.addunit;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddUnitUtil {

    private AddUnitUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddUnit getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddUnit.class);
    }
}
