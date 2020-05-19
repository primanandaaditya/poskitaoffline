package com.kitadigi.poskita.sinkron.produk.insert;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddProdukUtil {

    private AddProdukUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronInsertProduk getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronInsertProduk.class);

    }

}
