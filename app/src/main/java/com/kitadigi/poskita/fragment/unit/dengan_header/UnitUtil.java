package com.kitadigi.poskita.fragment.unit.dengan_header;

import com.kitadigi.poskita.fragment.unit.dengan_header.IUnit;
import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class UnitUtil {
    private UnitUtil() {}
    public static IUnit getInterface() {
        return RetrofitClient.getClient(Url.DIKI_URL).create(IUnit.class);
    }
}
