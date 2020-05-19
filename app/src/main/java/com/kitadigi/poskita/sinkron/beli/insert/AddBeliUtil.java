package com.kitadigi.poskita.sinkron.beli.insert;


import com.kitadigi.poskita.sinkron.jual.insert.ISinkronInsertJual;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class AddBeliUtil {

    private AddBeliUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronInsertBeli getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronInsertBeli.class);

    }

}
