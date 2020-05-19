package com.kitadigi.poskita.activities.login;


import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class LoginUtil {

    private LoginUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static ILogin getLoginInterface() {
        return RetrofitClient.getClient(BASE_URL).create(ILogin.class);
    }
}
