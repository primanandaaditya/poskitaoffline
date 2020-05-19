package com.kitadigi.poskita.activities.report.transaction;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IReportTransaction {

    @GET(Url.DIKI_REPORT_TRANSACTION + "/{id_users}")
    Call<ReportTransactionModel> getReport(
            @Path("id_users") String id_users,
            @QueryMap HashMap<String,String> params
    );

}
