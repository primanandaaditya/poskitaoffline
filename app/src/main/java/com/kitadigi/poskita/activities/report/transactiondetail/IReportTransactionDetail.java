package com.kitadigi.poskita.activities.report.transactiondetail;

import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IReportTransactionDetail {

    @GET(Url.DIKI_REPORT_TRANSACTION_DETAIL + "/{id_transaksi}" + "/{id_users}")
    Call<TransactionDetailModel> getReport(
            @Path("id_transaksi") String id_transaksi,
            @Path("id_users") String id_users
    );

}
