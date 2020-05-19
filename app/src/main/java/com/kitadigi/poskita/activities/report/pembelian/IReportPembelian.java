package com.kitadigi.poskita.activities.report.pembelian;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IReportPembelian {

    @GET(Url.DIKI_REPORT_PEMBELIAN + "/{id_users}")
    Call<ReportPembelianModel> getReport(
            @Path("id_users") String id_users,
            @QueryMap HashMap<String,String> params
    );


}
