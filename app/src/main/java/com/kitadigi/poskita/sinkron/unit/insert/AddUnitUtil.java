package com.kitadigi.poskita.sinkron.unit.insert;


import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddUnitUtil {

    private AddUnitUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronInsertUnit getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronInsertUnit.class);

    }

}
