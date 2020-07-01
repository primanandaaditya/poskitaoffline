package com.kitadigi.poskita.sinkron.jual.get_master;


import com.kitadigi.poskita.sinkron.jual.insert.ISinkronInsertJual;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class GetJualMasterUtil {

    private GetJualMasterUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronGetJualMaster getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronGetJualMaster.class);

    }

}
