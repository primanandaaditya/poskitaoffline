package com.kitadigi.poskita.fragment.deleteunit;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class DeleteUnitUtil {


    private DeleteUnitUtil() {};

    public static final String BASE_URL = Url.DIKI_URL;

    public static IDeleteUnit getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IDeleteUnit.class);
    }
}
