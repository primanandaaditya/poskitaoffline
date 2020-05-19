package com.kitadigi.poskita.activities.report.analisa;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IReportAnalisa {


    @GET(Url.DIKI_REPORT_RINGKASAN_ANALISA + "/{id_users}")
    Call<ReportRingkasanAnalisaModel> getReport(
            @Path("id_users") String id_users,
            @QueryMap HashMap<String,String> params
    );
}
