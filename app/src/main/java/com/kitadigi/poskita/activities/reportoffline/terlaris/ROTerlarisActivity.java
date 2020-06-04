package com.kitadigi.poskita.activities.reportoffline.terlaris;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class ROTerlarisActivity extends BaseActivity implements  IROTerlarisResult{


    //init controller
    ROTerlarisController roTerlarisController;

    //session untuk get enkrip id user
    SessionManager sessionManager;

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
        setContentView(R.layout.activity_roterlaris);


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
        sessionManager=new SessionManager(ROTerlarisActivity.this);
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
//        DialogFormStartDate();

        requestReport();
    }

    void requestReport(){

        roTerlarisController=new ROTerlarisController(ROTerlarisActivity.this, this);
        roTerlarisController.getReport();

    }

    private void DialogFormStartDate() {

        dialog = new AlertDialog.Builder(ROTerlarisActivity.this);
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
                String filterTanggal = StringUtil.tanggalDariDatePicker(et_periode_awal);

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

        dialog = new AlertDialog.Builder(ROTerlarisActivity.this);
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

                String filterTanggal = StringUtil.tanggalDariDatePicker(et_periode_awal);

                //simpan ke variabel tanggal sampai
                tanggal_sampai = filterTanggal;

                //tembak API
                requestReport();

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

    @Override
    public void onTerlarisSuccess(List<ROTerlarisModel> roTerlarisModels) {
        for (ROTerlarisModel roTerlarisModel: roTerlarisModels){
            Log.d("kode_id", roTerlarisModel.getName_product());
            Log.d("laris", roTerlarisModel.getJumlah_terjual().toString());
        }
    }

    @Override
    public void onTerlarisError(String error) {
        this.showToast(error);
    }
}
