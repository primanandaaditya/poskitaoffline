package com.kitadigi.poskita.sinkron.produk.delete;


import com.kitadigi.poskita.sinkron.produk.insert.ISinkronInsertProduk;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class DeleteProdukUtil {

    private DeleteProdukUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronDeleteProduk getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronDeleteProduk.class);

    }

}
