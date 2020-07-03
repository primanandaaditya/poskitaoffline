package com.kitadigi.poskita.sinkron.beli.get_detail;


import com.kitadigi.poskita.sinkron.beli.get_master.ISinkronGetBeliMaster;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class GetBeliDetailUtil {

    private GetBeliDetailUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronGetBeliDetail getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronGetBeliDetail.class);

    }

}
