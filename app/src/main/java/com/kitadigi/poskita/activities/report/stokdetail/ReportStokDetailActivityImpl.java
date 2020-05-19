package com.kitadigi.poskita.activities.report.stokdetail;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportStokDetailActivityImpl implements IReportStokDetailActivityContract.IGetReportStokDetailIntractor {



    IReportStokDetail iReportStokDetail;
    String enkripUserId;
    String enkripIdTransaksi;
    HashMap<String,String> start_date;
    HashMap<String,String> end_date;
    Context context;
    SweetAlertDialog sweetAlertDialog;

    public ReportStokDetailActivityImpl(String enkripUserId, String enkripIdTransaksi, HashMap<String, String> start_date, HashMap<String, String> end_date, Context context) {
        this.enkripUserId = enkripUserId;
        this.enkripIdTransaksi = enkripIdTransaksi;
        this.start_date = start_date;
        this.end_date = end_date;
        this.context = context;
    }

    @Override
    public void getReportModel(final IReportStokDetailOnFinishedListener onFinishedListener) {

        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportStokDetail=ReportStokDetailUtil.getInterface();
        iReportStokDetail.getReport(enkripIdTransaksi,enkripUserId,start_date,end_date).enqueue(new Callback<ReportDetailModel>() {
            @Override
            public void onResponse(Call<ReportDetailModel> call, Response<ReportDetailModel> response) {
                Log.d("url sukses", call.request().url().toString());
                onFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportDetailModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                onFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}
