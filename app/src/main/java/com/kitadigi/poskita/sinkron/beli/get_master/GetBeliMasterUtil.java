package com.kitadigi.poskita.sinkron.beli.get_master;


import com.kitadigi.poskita.sinkron.jual.get_master.ISinkronGetJualMaster;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class GetBeliMasterUtil {

    private GetBeliMasterUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronGetBeliMaster getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronGetBeliMaster.class);

    }

}
