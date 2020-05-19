package com.kitadigi.poskita.fragment.kategori;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class KategoriUtil {

    private KategoriUtil() {}
    public static IKategori getInterface() {
        return RetrofitClient.getClient(Url.DIKI_URL).create(IKategori.class);
    }
}
