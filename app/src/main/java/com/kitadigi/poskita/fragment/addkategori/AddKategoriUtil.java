package com.kitadigi.poskita.fragment.addkategori;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddKategoriUtil {

    private AddKategoriUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddKategori getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddKategori.class);
    }
}
