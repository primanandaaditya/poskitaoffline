package com.kitadigi.poskita.activities.report.transaction;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.GetResultIntractor;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportTransactionActivityImpl implements GetResultIntractor {

    IReportTransaction iReportTransaction;
    String enkripUserId;
    HashMap<String,String> hashTransactionDate;
    Context context;

    SweetAlertDialog sweetAlertDialog;

    public ReportTransactionActivityImpl(Context context,String enkripUserId, HashMap<String, String> hashTransactionDate) {
        this.context=context;
        this.enkripUserId = enkripUserId;
        this.hashTransactionDate = hashTransactionDate;
    }

    @Override
    public void getReportModel(final OnFinishedListener onFinishedListener) {

        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportTransaction=ReportTransactionUtil.getInterface();
        iReportTransaction.getReport(enkripUserId,hashTransactionDate)
        .enqueue(new Callback<ReportTransactionModel>() {
            @Override
            public void onResponse(Call<ReportTransactionModel> call, Response<ReportTransactionModel> response) {
                Log.d("url sukses", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportTransactionModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });

    }
}
