package com.kitadigi.poskita.fragment.additem;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddBarangUtil {

    private AddBarangUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddBarang getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddBarang.class);
    }
}
