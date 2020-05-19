package com.kitadigi.poskita.activities.report.stok;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportStokUtil {

    private ReportStokUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportStok getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportStok.class);
    }
}
