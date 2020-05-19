package com.kitadigi.poskita.activities.report.analisa;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAnalisaActivityImpl implements IReportAnalisaActivityContract.IGetReportAnalisaIntractor {


    IReportAnalisa iReportAnalisa;
    String enkripUserId;
    HashMap<String,String> hashTransactionDate;
    Context context;
    SweetAlertDialog sweetAlertDialog;

    public ReportAnalisaActivityImpl(String enkripUserId, HashMap<String, String> hashTransactionDate, Context context) {
        this.enkripUserId = enkripUserId;
        this.hashTransactionDate = hashTransactionDate;
        this.context = context;
    }

    @Override
    public void getReportModel(final IReportAnalisaOnFinishedListener onFinishedListener) {

        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportAnalisa=ReportAnalisaUtil.getInterface();
        iReportAnalisa.getReport(enkripUserId,hashTransactionDate).enqueue(new Callback<ReportRingkasanAnalisaModel>() {
            @Override
            public void onResponse(Call<ReportRingkasanAnalisaModel> call, Response<ReportRingkasanAnalisaModel> response) {
                Log.d("url sukses", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportRingkasanAnalisaModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });

    }
}
