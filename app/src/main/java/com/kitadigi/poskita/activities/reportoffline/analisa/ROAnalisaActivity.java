package com.kitadigi.poskita.activities.reportoffline.analisa;

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

public class ROAnalisaActivity extends BaseActivity implements IROAnalisaResult {


    //session untuk get enkrip id user
    SessionManager sessionManager;

    //init controller
    ROAnalisaController roAnalisaController;

    //untuk menampung id user
    String enkripIdUser;

    //string untuk menampung tanggal dari dan tanggal sampai
    String tanggal_dari,tanggal_sampai;

    //init tv
    TextView tv_nav_header, tv_label_grand_total_penjualan, tv_label_grand_total_pembelian,
    tv_label_total_item_penjualan, tv_total_item_penjualan, tv_grand_total_penjualan,tv_grand_total_pembelian;

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
        setContentView(R.layout.activity_roanalisa);


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
        sessionManager=new SessionManager(ROAnalisaActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_label_grand_total_pembelian=(TextView)findViewById(R.id.tv_label_grand_total_pembelian);
        tv_label_grand_total_penjualan=(TextView)findViewById(R.id.tv_label_grand_total_penjualan);
        tv_label_total_item_penjualan=(TextView)findViewById(R.id.tv_label_total_item_penjualan);
        tv_total_item_penjualan=(TextView)findViewById(R.id.tv_total_item_penjualan);
        tv_grand_total_pembelian=(TextView)findViewById(R.id.tv_grand_total_pembelian);
        tv_grand_total_penjualan=(TextView)findViewById(R.id.tv_grand_total_penjualan);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_label_grand_total_pembelian);
        this.applyFontRegularToTextView(tv_label_grand_total_penjualan);
        this.applyFontRegularToTextView(tv_label_total_item_penjualan);
        this.applyFontBoldToTextView(tv_grand_total_pembelian);
        this.applyFontBoldToTextView(tv_grand_total_penjualan);
        this.applyFontBoldToTextView(tv_total_item_penjualan);

        //waktu pertama kali, suruh user memilih tanggal
        DialogFormStartDate();


    }


    void  requestReport(){
        roAnalisaController = new ROAnalisaController(ROAnalisaActivity.this, this);
        roAnalisaController.getReport(tanggal_dari,tanggal_sampai);


    }


    private void DialogFormStartDate() {

        dialog = new AlertDialog.Builder(ROAnalisaActivity.this);
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

        dialog = new AlertDialog.Builder(ROAnalisaActivity.this);
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
    public void onROAnalisaSuccess(List<ROAnalisaModel> roAnalisaModels) {

        //set teks ke grand total penjualan
        String grandTotalPenjualan = roAnalisaController.getGrandTotalPenjualan(roAnalisaModels);
        tv_grand_total_penjualan.setText(grandTotalPenjualan);

        //set teks ke total item penjualan
        String totalItem = roAnalisaController.getTotalPenjualan(roAnalisaModels);
        tv_total_item_penjualan.setText(totalItem);

        //set teks ke grand total pembelian
        String grandTotalPembelian = roAnalisaController.getGrandTotalPembelian(roAnalisaModels);
        tv_grand_total_pembelian.setText(grandTotalPembelian);
    }

    @Override
    public void onROAnalisaGagal(String error) {
        this.showToast(error);
    }
}
