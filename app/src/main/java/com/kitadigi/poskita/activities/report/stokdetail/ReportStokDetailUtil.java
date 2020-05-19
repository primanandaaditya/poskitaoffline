package com.kitadigi.poskita.activities.report.stokdetail;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportStokDetailUtil {

    private ReportStokDetailUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportStokDetail getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportStokDetail.class);
    }
}
