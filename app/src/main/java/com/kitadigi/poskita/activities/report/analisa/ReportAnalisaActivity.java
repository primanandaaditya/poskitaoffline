package com.kitadigi.poskita.activities.report.analisa;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;

import com.kitadigi.poskita.activities.report.analisa.IReportAnalisaActivityContract.IReportAnalisaMainView;
import com.kitadigi.poskita.activities.report.analisa.IReportAnalisaActivityContract.IReportAnalisaPresenter;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;


public class ReportAnalisaActivity extends BaseActivity implements IReportAnalisaMainView {


    //adapter untuk menampilkan list produk terlaku
    ReportAnalisaAdapter reportAnalisaAdapter;

    IReportAnalisaPresenter iReportAnalisaPresenter;

    //session untuk get enkrip id user
    SessionManager sessionManager;

    //hashmapp untuk retrofit
    HashMap<String,String> hashMap;

    //untuk menampung id user
    String enkripIdUser;


    //init tv
    TextView tv_nav_header, tv_total_qty, tv_grand_total,tv_tanggal;
    TextView tv_label_produk_terjual,tv_produk_terjual;
    TextView tv_label_produk_terlaku,tv_produk_terlaku;
    TextView tv_label_tanggal;
    TextView tv_label_total_keuntungan,tv_total_keuntungan;
    TextView tv_label_total_pembelian,tv_total_pembelian;
    TextView tv_label_total_penjualan,tv_total_penjualan;
    TextView tv_label_total_purchase_order,tv_total_purchase_order;
    TextView tv_label_total_transaksi,tv_total_transaksi;

    //button untuk menampilkan list produk terlaku
    Button btn_produk_terlaku;

    //init imageview
    ImageView iv_back,iv_filter;

    //init listview
    ListView lv_produk_terjual;


    //di bawah ini untuk dialog pilih tanggal
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView tv_title;
    DatePicker et_periode_awal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_analisa);




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
        sessionManager=new SessionManager(ReportAnalisaActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();


        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
        tv_label_produk_terjual=(TextView)findViewById(R.id.tv_label_produk_terjual);
        tv_produk_terjual=(TextView)findViewById(R.id.tv_produk_terjual);
        tv_label_produk_terlaku=(TextView)findViewById(R.id.tv_label_produk_terlaku);
        tv_produk_terlaku=(TextView)findViewById(R.id.tv_produk_terlaku);
        tv_label_tanggal=(TextView)findViewById(R.id.tv_label_tanggal);
        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);
        tv_total_keuntungan=(TextView)findViewById(R.id.tv_total_keuntungan);
        tv_label_total_keuntungan=(TextView)findViewById(R.id.tv_label_total_keuntungan);
        tv_total_pembelian=(TextView)findViewById(R.id.tv_total_pembelian);
        tv_label_total_pembelian=(TextView)findViewById(R.id.tv_label_total_pembelian);
        tv_total_penjualan=(TextView)findViewById(R.id.tv_total_penjualan);
        tv_label_total_penjualan=(TextView)findViewById(R.id.tv_label_total_penjualan);
        tv_total_purchase_order=(TextView)findViewById(R.id.tv_total_purchase_order);
        tv_label_total_purchase_order=(TextView)findViewById(R.id.tv_label_total_penjualan);
        tv_total_transaksi=(TextView)findViewById(R.id.tv_total_transaksi);
        tv_label_total_transaksi=(TextView)findViewById(R.id.tv_label_total_transaksi);
        btn_produk_terlaku=(Button)findViewById(R.id.btn_produk_terlaku);


        //button utk menampilkan list produk_terlaku
        btn_produk_terlaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListProdukTerlaku();
            }
        });
//        lv_produk_terjual=(ListView)findViewById(R.id.lv_produk_terlaku);



//        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_total_qty);
        this.applyFontRegularToTextView(tv_grand_total);
//        this.applyFontRegularToTextView(tv_tanggal);
        this.applyFontRegularToTextView(tv_label_produk_terjual);
        this.applyFontBoldToTextView(tv_produk_terjual);
        this.applyFontRegularToTextView(tv_label_produk_terlaku);
        this.applyFontBoldToTextView(tv_produk_terlaku);
        this.applyFontRegularToTextView(tv_label_tanggal);
        this.applyFontBoldToTextView(tv_tanggal);
        this.applyFontRegularToTextView(tv_label_total_keuntungan);
        this.applyFontBoldToTextView(tv_total_keuntungan);
        this.applyFontRegularToTextView(tv_label_total_pembelian);
        this.applyFontBoldToTextView(tv_total_pembelian);
        this.applyFontRegularToTextView(tv_label_total_penjualan);
        this.applyFontBoldToTextView(tv_total_penjualan);
        this.applyFontRegularToTextView(tv_label_total_purchase_order);
        this.applyFontBoldToTextView(tv_total_purchase_order);
        this.applyFontRegularToTextView(tv_label_total_transaksi);
        this.applyFontBoldToTextView(tv_total_transaksi);
        this.applyFontBoldToButton(btn_produk_terlaku);

        //waktu pertama kali, suruh user memilih tanggal
        DialogForm();


    }

    @Override
    public void requestReport(String enkripIdUser, String tanggal) {

        hashMap=new HashMap<>();
        hashMap.put(getResources().getString(R.string.report_date),tanggal);

        iReportAnalisaPresenter=new ReportAnalisaPresenter(this, new ReportAnalisaActivityImpl(enkripIdUser,hashMap,ReportAnalisaActivity.this));
        iReportAnalisaPresenter.requestDataFromServer();
    }

    @Override
    public void setDataToView(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel) {

        //jika tidak expired
        if (this.sessionExpired(reportRingkasanAnalisaModel.getMessage())==0){
            tv_produk_terjual.setText(reportRingkasanAnalisaModel.getProduk_terjual().toString());
            tv_tanggal.setText(reportRingkasanAnalisaModel.getReport_date());
            tv_total_keuntungan.setText(reportRingkasanAnalisaModel.getTotal_keuntungan());
            tv_total_pembelian.setText(reportRingkasanAnalisaModel.getTotal_pembelian());
            tv_total_penjualan.setText(reportRingkasanAnalisaModel.getTotal_penjualan());
            tv_total_purchase_order.setText(reportRingkasanAnalisaModel.getTotal_purchase_order());
            tv_total_transaksi.setText(reportRingkasanAnalisaModel.getTotal_transaksi());
            reportAnalisaAdapter=new ReportAnalisaAdapter(reportRingkasanAnalisaModel,ReportAnalisaActivity.this);

        }

    }

    @Override
    public void onError(Throwable throwable) {

        this.showToast(throwable.getMessage());
    }



    private void DialogForm() {
        dialog = new AlertDialog.Builder(ReportAnalisaActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);


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



    private void DialogListProdukTerlaku() {
        dialog = new AlertDialog.Builder(ReportAnalisaActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_produk_terlaku, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.produk_terlaku));

        lv_produk_terjual=(ListView)dialogView.findViewById(R.id.lv_produk_terlaku);
        lv_produk_terjual.setAdapter(reportAnalisaAdapter);

//        this.applyFontRegularToEditText(et_periode_awal);
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

//                String tahun = String.valueOf(et_periode_awal.getYear());
//                String bulan = String.valueOf(et_periode_awal.getMonth()+1);
//                String tanggal = String.valueOf(et_periode_awal.getDayOfMonth());
//                String filterTanggal = tahun + "-" + bulan + "-" +tanggal;
//
//                requestReport(enkripIdUser,filterTanggal);
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
