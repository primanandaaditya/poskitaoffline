package com.kitadigi.poskita.sinkron.jual.insert;


import com.kitadigi.poskita.sinkron.brand.insert.ISinkronInsertBrand;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddJualUtil {

    private AddJualUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronInsertJual getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronInsertJual.class);

    }

}
