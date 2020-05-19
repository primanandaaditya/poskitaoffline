package com.kitadigi.poskita.sinkron.kategori.delete;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronDeleteKategoriUtil {

    private SinkronDeleteKategoriUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronDeleteKategori getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronDeleteKategori.class);

    }

}
