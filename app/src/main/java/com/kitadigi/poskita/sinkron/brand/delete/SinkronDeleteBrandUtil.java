package com.kitadigi.poskita.sinkron.brand.delete;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronDeleteBrandUtil {

    private SinkronDeleteBrandUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronDeleteBrand getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronDeleteBrand.class);

    }

}
