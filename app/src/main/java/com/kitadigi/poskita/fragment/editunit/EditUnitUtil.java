package com.kitadigi.poskita.fragment.editunit;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class EditUnitUtil {

    private EditUnitUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IEditUnit getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IEditUnit.class);
    }
}
