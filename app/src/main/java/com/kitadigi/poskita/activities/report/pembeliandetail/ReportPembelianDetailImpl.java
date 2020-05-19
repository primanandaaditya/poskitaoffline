package com.kitadigi.poskita.activities.report.pembeliandetail;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailGetIntractor;
import com.kitadigi.poskita.activities.report.transactiondetail.TransactionDetailModel;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPembelianDetailImpl implements IReportPembelianDetailGetIntractor {

    IReportPembelianDetail iReportPembelianDetail;
    String enkripUserId;
    String enkripTransaksi;
    Context context;
    SweetAlertDialog sweetAlertDialog;

    public ReportPembelianDetailImpl(String enkripUserId, String enkripTransaksi, Context context) {
        this.enkripUserId = enkripUserId;
        this.enkripTransaksi = enkripTransaksi;
        this.context = context;
    }

    @Override
    public void getReportModel(final IReportPembelianDetailOnFinishedListener onFinishedListener) {
        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportPembelianDetail=ReportPembelianDetailUtil.getInterface();
        iReportPembelianDetail.getReport(enkripTransaksi,enkripUserId)
        .enqueue(new Callback<TransactionDetailModel>() {
            @Override
            public void onResponse(Call<TransactionDetailModel> call, Response<TransactionDetailModel> response) {
                Log.d("url sukses", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<TransactionDetailModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}
