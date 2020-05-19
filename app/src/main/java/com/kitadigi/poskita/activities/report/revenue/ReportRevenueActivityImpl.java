package com.kitadigi.poskita.activities.report.revenue;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportRevenueActivityImpl implements IReportRevenueActivityContract.IGetReportRevenueIntractor {


    IReportRevenue iReportRevenue;
    String enkripUserId;
    HashMap<String,String> start_date;
    HashMap<String,String> end_date;
    Context context;
    SweetAlertDialog sweetAlertDialog;

    public ReportRevenueActivityImpl(String enkripUserId, HashMap<String, String> start_date, HashMap<String, String> end_date, Context context) {
        this.enkripUserId = enkripUserId;
        this.start_date = start_date;
        this.end_date = end_date;
        this.context = context;
    }

    @Override
    public void getReportModel(final IReportRevenueOnFinishedListener onFinishedListener) {

        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportRevenue = ReportRevenueUtil.getInterface();
        iReportRevenue.getReport(enkripUserId,start_date,end_date).enqueue(new Callback<ReportRevenueModel>() {
            @Override
            public void onResponse(Call<ReportRevenueModel> call, Response<ReportRevenueModel> response) {
                Log.d("url sukses",call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportRevenueModel> call, Throwable t) {
                Log.d("url gagal",call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}
