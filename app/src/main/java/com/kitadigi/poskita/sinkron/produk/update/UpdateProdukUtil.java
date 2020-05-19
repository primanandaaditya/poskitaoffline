package com.kitadigi.poskita.sinkron.produk.update;


import com.kitadigi.poskita.sinkron.produk.insert.ISinkronInsertProduk;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class UpdateProdukUtil {

    private UpdateProdukUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronUpdateProduk getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronUpdateProduk.class);

    }

}
