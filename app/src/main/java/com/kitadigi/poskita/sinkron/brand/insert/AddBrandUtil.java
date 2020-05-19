package com.kitadigi.poskita.sinkron.brand.insert;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddBrandUtil {

    private AddBrandUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronInsertBrand getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronInsertBrand.class);

    }

}
