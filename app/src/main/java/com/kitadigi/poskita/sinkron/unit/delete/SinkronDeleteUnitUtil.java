package com.kitadigi.poskita.sinkron.unit.delete;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronDeleteUnitUtil {

    private SinkronDeleteUnitUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronDeleteUnit getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronDeleteUnit.class);

    }

}
