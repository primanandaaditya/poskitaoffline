package com.kitadigi.poskita.sinkron.kategori.update;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronUpdateKategoriUtil {

    private SinkronUpdateKategoriUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronUpdateKategori getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronUpdateKategori.class);

    }

}
