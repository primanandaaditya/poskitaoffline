package com.kitadigi.poskita.activities.report.pembeliandetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.transactiondetail.TransactionDetailModel;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailMainView;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailPresenter;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;

public class ReportPembelianDetailActivity extends BaseActivity implements IReportPembelianDetailMainView {

    IReportPembelianDetailPresenter iReportPembelianDetailPresenter;

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

    //string untuk menerima intent, yaitu enkrip transaksi
    String enkripTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pembelian_detail);

        //dapatkan intent
        Intent intent = getIntent();
        enkripTransaksi = intent.getStringExtra("additional");


        //iv_back
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //init session manager
        sessionManager=new SessionManager(ReportPembelianDetailActivity.this);
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

        requestReport(enkripIdUser,enkripTransaksi);

    }

    @Override
    public void requestReport(String enkripIdUser, String enkripTransaksi) {

        iReportPembelianDetailPresenter=new ReportPembelianDetailPresenter(this, new ReportPembelianDetailImpl(enkripIdUser,enkripTransaksi, ReportPembelianDetailActivity.this));
        iReportPembelianDetailPresenter.requestDataFromServer();
    }

    @Override
    public void setDataToView(TransactionDetailModel transactionDetailModel) {

        ReportPembelianDetailAdapter reportPembelianDetailAdapter=new ReportPembelianDetailAdapter(transactionDetailModel,ReportPembelianDetailActivity.this);
        lv.setAdapter(reportPembelianDetailAdapter);

        //tampilkan total qty dan total grand
        tv_total_qty.setText(getResources().getString(R.string.label_qty) + transactionDetailModel.getTotal_quantity());
        tv_grand_total.setText(getResources().getString(R.string.label_subtotal) + transactionDetailModel.getTotal_price());
    }

    @Override
    public void onError(Throwable throwable) {

        this.showToast(throwable.getMessage());
    }
}
