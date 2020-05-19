package com.kitadigi.poskita.activities.coba;

import com.kitadigi.poskita.retrofit.UserRC;

public class CobaUtil {

    private CobaUtil() {}

    public static CobaInterface getInterface() {

        return UserRC.getClient("https://reqres.in/api/").create(CobaInterface.class);
    }

}
