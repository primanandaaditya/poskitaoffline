package com.kitadigi.poskita.activities.registrasi;


import com.kitadigi.poskita.activities.login.ILogin;
import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class RegistrasiUtil {

    private RegistrasiUtil() {}

    public static final String BASE_URL = Url.SINKRON_BASE_URL;

    public static IRegistrasi getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IRegistrasi.class);
    }
}
