package com.kitadigi.poskita.activities.report.transaction;

import android.content.DialogInterface;
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
import com.kitadigi.poskita.activities.report.transactiondetail.ReportTransactionDetailActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.MainView;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.Presenter;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;


public class ReportTransactionActivity extends BaseActivity implements MainView {

    Presenter presenter;

    //session untuk get enkrip id user
    SessionManager sessionManager;

    //hashmapp untuk retrofit
    HashMap<String,String> hashMap;

    //untuk menampung id user
    String enkripIdUser;

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
        setContentView(R.layout.activity_report_transaction);

        //tombol untuk filter tanggal
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm();
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
        sessionManager=new SessionManager(ReportTransactionActivity.this);
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
        DialogForm();
    }


    @Override
    public void requestReport(String enkripIdUser, String tanggal) {
        hashMap=new HashMap<>();
        hashMap.put(getResources().getString(R.string.report_transaction_date),tanggal);

        presenter=new ReportTransactionPresenter(this, new ReportTransactionActivityImpl(ReportTransactionActivity.this,enkripIdUser,hashMap));
        presenter.requestDataFromServer();
    }

    @Override
    public void setDataToView(ReportTransactionModel reportTransactionModel) {

        //jika tidak expired, tamppilkan report
        if (this.sessionExpired(reportTransactionModel.getMessage())==0){

            //clear report sebelumnya
            lv.setAdapter(null);
            tv_grand_total.setText("Grand Total : ");
            tv_total_qty.setText("Total qty :");


            if (reportTransactionModel == null || reportTransactionModel.data == null){

            }else{
                ReportTransactionAdapter reportTransactionAdapter
                        = new ReportTransactionAdapter(
                        ReportTransactionActivity.this,
                        ReportTransactionActivity.this,
                        reportTransactionModel
                );

                //atribut report
//            tv_tanggal.setText(StringUtil.tanggalIndonesia(reportTransactionModel.getCurrent_time()));
                tv_grand_total.setText("Grand Total : " +reportTransactionModel.getTotal_amount());
                tv_total_qty.setText("Total qty :" +reportTransactionModel.getTotal_qty());

                lv.setAdapter(reportTransactionAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //mau pindah ke report detail activity
                        Intent intent=new Intent(ReportTransactionActivity.this, ReportTransactionDetailActivity.class);

                        //get enkrip transaksi dari model, utnuk dilempar ke intent
                        ReportTransactionData reportTransactionData = (ReportTransactionData)parent.getAdapter().getItem(position);

                        //letakkan enkrip transaksi di intent
                        intent.putExtra("additional",reportTransactionData.additional);

                        //mulai pindah intent
                        startActivity(intent);
                    }
                });
            }


        }

    }

    @Override
    public void onError(Throwable throwable) {
        this.showToast(throwable.getMessage());
    }


    private void DialogForm() {
        dialog = new AlertDialog.Builder(ReportTransactionActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);

//        this.applyFontRegularToEditText(et_periode_awal);
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tahun = String.valueOf(et_periode_awal.getYear());
                String bulan = String.valueOf(et_periode_awal.getMonth()+1);
                String tanggal = String.valueOf(et_periode_awal.getDayOfMonth());
                String filterTanggal = tahun + "-" + bulan + "-" +tanggal;

                requestReport(enkripIdUser,filterTanggal);
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
