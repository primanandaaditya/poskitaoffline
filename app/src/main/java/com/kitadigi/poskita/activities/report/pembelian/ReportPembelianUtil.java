package com.kitadigi.poskita.activities.report.pembelian;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportPembelianUtil {

    private ReportPembelianUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportPembelian getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportPembelian.class);
    }
}
