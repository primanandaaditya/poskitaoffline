package com.kitadigi.poskita.activities.reportoffline.detailjual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;

import java.util.HashMap;
import java.util.List;

public class ROJualDetailActivity extends BaseActivity implements IROHistoriJualDetailResult {

    //init controller
    IROHistoriJualDetailController controller;

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

    //string untuk menerima intent, yaitu nomor transaksi
    String nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rojual_detail);


        //dapatkan intent
        Intent intent = getIntent();
        nomor = intent.getStringExtra("nomor");


        //iv_back
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //init session manager
        sessionManager=new SessionManager(ROJualDetailActivity.this);
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


        //tampilkan report
        requestReport(nomor);
    }

    void requestReport(String nomor_transaksi){
        controller = new IROHistoriJualDetailController(ROJualDetailActivity.this,this);
        controller.getReport(nomor_transaksi);
    }

    @Override
    public void onHistoriJualDetailSuccess(List<DetailJualModel> detailJualModels) {
        ROHistoriJualDetailAdapter roHistoriJualDetailAdapter = new ROHistoriJualDetailAdapter(ROJualDetailActivity.this,detailJualModels);
        lv.setAdapter(roHistoriJualDetailAdapter);
    }

    @Override
    public void onHistoriJualDetailError(String error) {
        this.showToast(error);
    }
}
