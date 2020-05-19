package com.kitadigi.poskita.fragment.pos;

import com.kitadigi.poskita.retrofit.UserRC;
import com.kitadigi.poskita.util.Url;

public class StokUtil {

    private StokUtil() {}

    public static IStok getInterface() {
        return UserRC.getClient(Url.DIKI_URL).create(IStok.class);
    }
}
