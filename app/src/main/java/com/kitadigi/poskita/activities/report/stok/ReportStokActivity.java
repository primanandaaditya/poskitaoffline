package com.kitadigi.poskita.activities.report.stok;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.stokdetail.ReportStokDetailActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;

public class ReportStokActivity extends BaseActivity implements IReportStokActivityContract.IReportStokMainView {


    IReportStokActivityContract.IReportStokPresenter presenter;


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
    TextView tv_nav_header, tv_total_qty, tv_grand_total,tv_tanggal,tv_total_stok;

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
        setContentView(R.layout.activity_report_stok);



        //tombol untuk filter tanggal
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        sessionManager=new SessionManager(ReportStokActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        //init listview report
        lv=(ListView)findViewById(R.id.lv);



        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
        tv_total_stok=(TextView)findViewById(R.id.tv_total_stok);
//        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_total_qty);
        this.applyFontRegularToTextView(tv_grand_total);
        this.applyFontRegularToTextView(tv_total_stok);
//        this.applyFontRegularToTextView(tv_tanggal);


        requestReport(enkripIdUser);

    }

    @Override
    public void requestReport(String enkripIdUser) {


        presenter=new ReportStokPresenter(this, new ReportStokActivityImpl(enkripIdUser, ReportStokActivity.this));
        presenter.requestDataFromServer();
    }

    @Override
    public void setDataToView(ReportStokModel reportStokModel) {

        //clear report dahulu
        lv.setAdapter(null);
        tv_grand_total.setText(getResources().getString(R.string.label_total_price));
        tv_total_stok.setText(getResources().getString(R.string.label_total_stok));
        tv_total_qty.setText(getResources().getString(R.string.label_total_sold));

        if (this.sessionExpired(reportStokModel.getMessage())==0){
            if (reportStokModel.getMessage().matches(getResources().getString(R.string.success))){


                //tampilkan di listview
                final ReportStokAdapter reportStokAdapter=new ReportStokAdapter(reportStokModel,ReportStokActivity.this);
                lv.setAdapter(reportStokAdapter);

                //jika listview diklik, maka akan pindah ke ReportStokDetailActivity.java
                //sertakan intent dari "additional"
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ReportStokData reportStokData = (ReportStokData)parent.getAdapter().getItem(position);
                        Intent intent = new Intent(ReportStokActivity.this, ReportStokDetailActivity.class);
                        intent.putExtra("additional",reportStokData.getAdditional());
                        startActivity(intent);
                    }
                });


                //tampilkan total price
                tv_grand_total.setText(getResources().getString(R.string.label_total_price) + reportStokModel.getTotal_price());
                tv_total_stok.setText(getResources().getString(R.string.label_total_stok) + reportStokModel.getTotal_stock());
                tv_total_qty.setText(getResources().getString(R.string.label_total_sold) + reportStokModel.getTotal_sold());
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        this.showToast(throwable.getMessage());
    }
}
