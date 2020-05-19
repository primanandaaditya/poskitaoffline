package com.kitadigi.poskita.fragment.deletekategori;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class DeleteKategoriUtil {


    private DeleteKategoriUtil() {};

    public static final String BASE_URL = Url.DIKI_URL;

    public static IDeleteKategori getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IDeleteKategori.class);
    }
}
