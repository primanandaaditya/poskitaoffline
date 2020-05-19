package com.kitadigi.poskita.activities.report.stok;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IReportStok {

    @GET(Url.DIKI_REPORT_STOK + "/{id_users}")
    Call<ReportStokModel> getReport(
            @Path("id_users") String id_users
    );

}
