package com.kitadigi.poskita.fragment.editkategori;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class EditKategoriUtil {

    private EditKategoriUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IEditKategori getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IEditKategori.class);
    }
}
