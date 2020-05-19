package com.kitadigi.poskita.sinkron.unit.update;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class SinkronUpdateUnitUtil {

    private SinkronUpdateUnitUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronUpdateUnit getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronUpdateUnit.class);

    }

}
