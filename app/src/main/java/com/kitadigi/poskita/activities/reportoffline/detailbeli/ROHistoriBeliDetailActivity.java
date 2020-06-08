package com.kitadigi.poskita.activities.reportoffline.detailbeli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.util.HashMap;
import java.util.List;

public class ROHistoriBeliDetailActivity extends BaseActivity implements IROHistoriBeliDetailResult {



    //init controller
    IROHistoriBeliDetailController controller;

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
        setContentView(R.layout.activity_rohistori_beli_detail);

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
        sessionManager=new SessionManager(ROHistoriBeliDetailActivity.this);
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
        controller = new IROHistoriBeliDetailController(ROHistoriBeliDetailActivity.this,this);
        controller.getReport(nomor_transaksi);


    }

    @Override
    public void onHistoriBeliDetailSuccess(List<DetailBeliModel> detailBeliModels) {

        ROHistoriBeliDetailAdapter adapter = new ROHistoriBeliDetailAdapter(ROHistoriBeliDetailActivity.this,detailBeliModels);
        lv.setAdapter(adapter);

        String total_qty = controller.getTotalItem(detailBeliModels).toString() + " item";
        String grand_total = "Grand total : " + StringUtil.formatRupiah(controller.getGrandTotal(detailBeliModels));

        tv_total_qty.setText(total_qty);
        tv_grand_total.setText(grand_total);
    }

    @Override
    public void onHistoriBeliDetailError(String error) {
        this.showToast(error);

        tv_total_qty.setText("");
        tv_grand_total.setText("");
    }
}
