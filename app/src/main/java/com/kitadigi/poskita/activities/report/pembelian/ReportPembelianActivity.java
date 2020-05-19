package com.kitadigi.poskita.activities.report.pembelian;

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
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IReportPembelianMainView;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IReportPembelianPresenter;
import com.kitadigi.poskita.activities.report.pembeliandetail.ReportPembelianDetailActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;


public class ReportPembelianActivity extends BaseActivity implements IReportPembelianMainView {


    IReportPembelianPresenter iReportPembelianPresenter;


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
        setContentView(R.layout.activity_report_pembelian);


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
        sessionManager=new SessionManager(ReportPembelianActivity.this);
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

        iReportPembelianPresenter=new ReportPembelianPresenter(this, new ReportPembelianActivityImpl(enkripIdUser,hashMap,ReportPembelianActivity.this));
        iReportPembelianPresenter.requestDataFromServer();

    }

    @Override
    public void setDataToView(ReportPembelianModel reportPembelianModel) {

        //jika tidak expired, tamppilkan report
        if (this.sessionExpired(reportPembelianModel.getMessage())==0){

            //clear repport dulu
            lv.setAdapter(null);
            tv_grand_total.setText("Grand Total : 0");
            tv_total_qty.setText("Total qty : 0");


            ReportPembelianAdapter reportPembelianAdapter
                    = new ReportPembelianAdapter(
                    ReportPembelianActivity.this,
                    ReportPembelianActivity.this,
                    reportPembelianModel
            );

            //atribut report
//            tv_tanggal.setText(StringUtil.tanggalIndonesia(reportTransactionModel.getCurrent_time()));
            tv_grand_total.setText("Grand Total : " +reportPembelianModel.getTotal_amount());
            tv_total_qty.setText("Total qty :" +reportPembelianModel.getTotal_qty());

            lv.setAdapter(reportPembelianAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //mau pindah ke report detail activity
                    Intent intent=new Intent(ReportPembelianActivity.this, ReportPembelianDetailActivity.class);

                    //get enkrip transaksi dari model, utnuk dilempar ke intent
                    ReportPembelianData reportPembelianData = (ReportPembelianData)parent.getAdapter().getItem(position);

                    //letakkan enkrip transaksi di intent
                    intent.putExtra("additional",reportPembelianData.additional);

                    //mulai pindah intent
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onError(Throwable throwable) {

        this.showToast(throwable.getMessage());
    }


    private void DialogForm() {
        dialog = new AlertDialog.Builder(ReportPembelianActivity.this);
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
