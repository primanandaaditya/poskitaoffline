package com.kitadigi.poskita.sinkron.kategori.insert;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddKategoriUtil {

    private AddKategoriUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static IAddKategori getLoginInterface() {
        return SinkronClient.getClient(BASE_URL).create(IAddKategori.class);

    }

}
