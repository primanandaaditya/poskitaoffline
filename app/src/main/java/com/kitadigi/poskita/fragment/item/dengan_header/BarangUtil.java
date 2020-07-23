package com.kitadigi.poskita.fragment.item.dengan_header;

import com.kitadigi.poskita.fragment.item.dengan_header.IBarang;
import com.kitadigi.poskita.retrofit.UserRC;
import com.kitadigi.poskita.util.Url;

public class BarangUtil {

    private BarangUtil() {}

    public static IBarang getInterface() {

        return UserRC.getClient(Url.DIKI_URL).create(IBarang.class);
    }

}
