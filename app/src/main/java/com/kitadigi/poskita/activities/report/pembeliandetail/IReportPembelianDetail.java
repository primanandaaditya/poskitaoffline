package com.kitadigi.poskita.activities.report.pembeliandetail;

import com.kitadigi.poskita.activities.report.transactiondetail.TransactionDetailModel;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IReportPembelianDetail {

    @GET(Url.DIKI_REPORT_PEMBELIAN_DETAIL + "/{id_transaksi}" + "/{id_users}")
    Call<TransactionDetailModel> getReport(
            @Path("id_transaksi") String id_transaksi,
            @Path("id_users") String id_users
    );

}
