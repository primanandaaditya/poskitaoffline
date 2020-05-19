package com.kitadigi.poskita.activities.pos.addtransaction;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class AddTransactionUtil {

    private AddTransactionUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IAddTransaction getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IAddTransaction.class);
    }
}
