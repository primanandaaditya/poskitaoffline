package com.kitadigi.poskita.activities.report.transaction;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportTransactionUtil {

    private ReportTransactionUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportTransaction getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportTransaction.class);
    }
}
