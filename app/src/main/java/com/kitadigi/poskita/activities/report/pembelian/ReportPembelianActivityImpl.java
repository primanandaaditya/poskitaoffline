package com.kitadigi.poskita.activities.report.pembelian;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IGetReportPembelianIntractor;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPembelianActivityImpl implements IGetReportPembelianIntractor {



    IReportPembelian iReportPembelian;
    String enkripUserId;
    HashMap<String,String> hashTransactionDate;
    Context context;
    SweetAlertDialog sweetAlertDialog;

    public ReportPembelianActivityImpl(String enkripUserId, HashMap<String, String> hashTransactionDate, Context context) {
        this.enkripUserId = enkripUserId;
        this.hashTransactionDate = hashTransactionDate;
        this.context = context;
    }

    @Override
    public void getReportModel(final ReportPembelianOnFinishedListener reportPembelianOnFinishedListener) {

        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        iReportPembelian = ReportPembelianUtil.getInterface();
        iReportPembelian.getReport(enkripUserId,hashTransactionDate)
        .enqueue(new Callback<ReportPembelianModel>() {
            @Override
            public void onResponse(Call<ReportPembelianModel> call, Response<ReportPembelianModel> response) {
                Log.d("url sukses", call.request().url().toString());
                reportPembelianOnFinishedListener.onFinished(response.body());
                sweetAlertDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<ReportPembelianModel> call, Throwable t) {
                Log.d("url gagal", call.request().url().toString());
                reportPembelianOnFinishedListener.onError(t);
                sweetAlertDialog.dismissWithAnimation();
            }
        });

    }
}
