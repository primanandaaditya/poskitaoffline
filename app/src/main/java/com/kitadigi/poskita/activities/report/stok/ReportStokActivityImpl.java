package com.kitadigi.poskita.activities.report.stok;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportStokActivityImpl implements IReportStokActivityContract.IGetReportStokIntractor {


    IReportStok iReportStok;
    String enkripUserId;
    Context context;
    SweetAlertDialog sweetAlertDialog;


    public ReportStokActivityImpl(String enkripUserId, Context context) {
        this.enkripUserId = enkripUserId;
        this.context = context;
    }

    @Override
    public void getReportModel(final IReportStokOnFinishedListener onFinishedListener) {
        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportStok=ReportStokUtil.getInterface();
        iReportStok.getReport(enkripUserId).enqueue(new Callback<ReportStokModel>() {
            @Override
            public void onResponse(Call<ReportStokModel> call, Response<ReportStokModel> response) {
                Log.d("url sukses", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportStokModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}
