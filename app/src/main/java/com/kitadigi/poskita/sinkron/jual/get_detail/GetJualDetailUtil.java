package com.kitadigi.poskita.sinkron.jual.get_detail;


import com.kitadigi.poskita.sinkron.jual.get_master.ISinkronGetJualMaster;
import com.kitadigi.poskita.sinkron.retrofit.SinkronClient;
import com.kitadigi.poskita.util.Url;

public class GetJualDetailUtil {

    private GetJualDetailUtil() {}
    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static ISinkronGetJualDetail getInterface() {
        return SinkronClient.getClient(BASE_URL).create(ISinkronGetJualDetail.class);

    }

}
