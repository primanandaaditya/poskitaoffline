package com.kitadigi.poskita.sinkron.brand.update;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronUpdateBrandUtil {

    private SinkronUpdateBrandUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronUpdateBrand getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronUpdateBrand.class);

    }

}
