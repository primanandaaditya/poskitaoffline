package com.kitadigi.poskita.activities.report.analisa;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportAnalisaUtil {

    private ReportAnalisaUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportAnalisa getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportAnalisa.class);
    }
}
