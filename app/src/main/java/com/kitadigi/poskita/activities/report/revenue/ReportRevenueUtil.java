package com.kitadigi.poskita.activities.report.revenue;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportRevenueUtil {

    private ReportRevenueUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportRevenue getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportRevenue.class);
    }
}
