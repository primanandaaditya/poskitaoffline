package com.kitadigi.poskita.activities.pembelian;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddPembelianUtil {

    private AddPembelianUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddPembelian getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddPembelian.class);
    }
}
