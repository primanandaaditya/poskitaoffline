package com.kitadigi.poskita.activities.report.pembeliandetail;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class ReportPembelianDetailUtil {

    private ReportPembelianDetailUtil() {}

    public static final String BASE_URL = Url.DIKI_URL;

    public static IReportPembelianDetail getInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IReportPembelianDetail.class);
    }

}
