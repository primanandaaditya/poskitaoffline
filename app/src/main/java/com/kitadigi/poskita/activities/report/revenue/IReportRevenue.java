package com.kitadigi.poskita.activities.report.revenue;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IReportRevenue {

    @GET(Url.DIKI_REPORT_REVENUE + "/{id_users}")
    Call<ReportRevenueModel> getReport(
            @Path("id_users") String id_users,
            @QueryMap HashMap<String,String> start_date,
            @QueryMap HashMap<String,String> end_date
    );
}
