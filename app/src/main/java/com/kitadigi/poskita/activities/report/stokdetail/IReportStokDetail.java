package com.kitadigi.poskita.activities.report.stokdetail;

import com.kitadigi.poskita.util.Url;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IReportStokDetail {

    @GET(Url.DIKI_REPORT_STOK_DETAIL + "/{id_transaksi}" + "/{id_users}")
    Call<ReportDetailModel> getReport(
            @Path("id_transaksi") String id_transaksi,
            @Path("id_users") String id_users,
            @QueryMap HashMap<String,String> start_date,
            @QueryMap HashMap<String,String> end_date
    );

}
