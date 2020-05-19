package com.kitadigi.poskita.activities.report.revenue;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;

public class ReportRevenueActivity extends BaseActivity implements IReportRevenueActivityContract.IReportRevenueMainView {

    IReportRevenueActivityContract.IReportRevenuePresenter iReportRevenuePresenter;


    //session untuk get enkrip id user
    SessionManager sessionManager;

    //hashmapp untuk retrofit
    HashMap<String,String> hashMapStartDate;
    HashMap<String,String> hashMapEndDate;

    //untuk menampung id user
    String enkripIdUser;

    //string untuk menampung tanggal dari dan tanggal sampai
    String tanggal_dari,tanggal_sampai;

    //init listview
    ListView lv;

    //init tv
    TextView tv_nav_header, tv_total_qty, tv_grand_total,tv_tanggal;

    //init imageview
    ImageView iv_back,iv_filter;



    //di bawah ini untuk dialog pilih tanggal
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView tv_title;
    DatePicker et_periode_awal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_revenue);


        //tombol untuk filter tanggal
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFormStartDate();
            }
        });

        //iv_back
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //init session manager
        sessionManager=new SessionManager(ReportRevenueActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        //init listview report
        lv=(ListView)findViewById(R.id.lv);

        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
//        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_total_qty);
        this.applyFontRegularToTextView(tv_grand_total);
//        this.applyFontRegularToTextView(tv_tanggal);

        //waktu pertama kali, suruh user memilih tanggal
        DialogFormStartDate();

    }

    @Override
    public void requestReport(String enkripIdUser, String start_date, String end_date) {

        //bentuk query map start date, untuk interface IReportRevenue
        hashMapStartDate=new HashMap<>();
        hashMapStartDate.put("start_date",start_date);

        //bentuk query map end date, untuk interface IReportRevenue
        hashMapEndDate=new HashMap<>();
        hashMapEndDate.put("end_date",end_date);


        iReportRevenuePresenter=new ReportRevenuePresenter(this, new ReportRevenueActivityImpl(enkripIdUser,hashMapStartDate,hashMapEndDate,ReportRevenueActivity.this));
        iReportRevenuePresenter.requestDataFromServer();

    }

    @Override
    public void setDataToView(ReportRevenueModel reportRevenueModel) {



        //jika tidak expired
        if (this.sessionExpired(reportRevenueModel.getMessage())==0){

            //clear report terdahulu
            lv.setAdapter(null);
            tv_total_qty.setText(getResources().getString(R.string.label_qty) );
            tv_grand_total.setText(getResources().getString(R.string.label_subtotal));

            //jika sukses
            if (reportRevenueModel.getMessage().toString().matches(getResources().getString(R.string.success))){


                //tampilkan report di listview
                ReportRevenueAdapter reportRevenueAdapter=new ReportRevenueAdapter(reportRevenueModel,ReportRevenueActivity.this);
                lv.setAdapter(reportRevenueAdapter);

                //tampilkan total
                tv_total_qty.setText(getResources().getString(R.string.label_qty) + reportRevenueModel.getTotal_qty().toString());
                tv_grand_total.setText(getResources().getString(R.string.label_subtotal) + reportRevenueModel.getTotal_amount());
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {

        this.showToast(throwable.getMessage());
    }



    //ini untuk menampilkan dialog tanggal dari
    //nanti kalau di-ok sama user, langsung pindah ke DialogFormEndDate

    private void DialogFormStartDate() {

        dialog = new AlertDialog.Builder(ReportRevenueActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);


        //set title
        tv_title.setText(getResources().getString(R.string.dari_tanggal));


//        this.applyFontRegularToEditText(et_periode_awal);
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tahun = String.valueOf(et_periode_awal.getYear());
                String bulan = String.valueOf(et_periode_awal.getMonth()+1);
                String tanggal = String.valueOf(et_periode_awal.getDayOfMonth());
                String filterTanggal = tahun + "-" + bulan + "-" +tanggal;

                //simpan ke variabel tanggal_dari
                tanggal_dari = filterTanggal;

                //tutup dialog
                dialog.dismiss();

                //pindah ke dialog end date
                DialogFormEndDate();


//                requestReport(enkripIdUser,filterTanggal,filterTanggal);
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void DialogFormEndDate() {

        dialog = new AlertDialog.Builder(ReportRevenueActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);


        //set title
        tv_title.setText(getResources().getString(R.string.sampai_tanggal));
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tahun = String.valueOf(et_periode_awal.getYear());
                String bulan = String.valueOf(et_periode_awal.getMonth()+1);
                String tanggal = String.valueOf(et_periode_awal.getDayOfMonth());
                String filterTanggal = tahun + "-" + bulan + "-" +tanggal;

                //simpan ke variabel tanggal sampai
                tanggal_sampai = filterTanggal;

                //tembak API
                requestReport(enkripIdUser,tanggal_dari,tanggal_sampai);

                //tutup dialog
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
