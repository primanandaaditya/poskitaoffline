package com.kitadigi.poskita.fragment.deleteitem;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class DeleteBarangUtil {

    private DeleteBarangUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IDeleteBarang getInterface() {

        return RetrofitClient.getClient(BASE_URL).create(IDeleteBarang.class);
    }

}
