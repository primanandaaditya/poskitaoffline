package com.kitadigi.poskita.activities.pembelian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;

public class PembelianBerhasilActivity extends BaseActivity {

    Button btnFinish;
    TextView tv_nav_header,tv_remark_transaksi;
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian_berhasil);

        //find id
        btnFinish=(Button)findViewById(R.id.btn_finish);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_remark_transaksi=(TextView)findViewById(R.id.tv_remark_transaksi);
        iv_back=(ImageView)findViewById(R.id.iv_back);


        //apply font
        this.applyFontBoldToButton(btnFinish);
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_remark_transaksi);

        //finish activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindahKeHome();
            }
        });
    }

    void pindahKeHome(){
        //pindah ke home
        Intent homeActivity = new Intent(PembelianBerhasilActivity.this, MainActivity.class);
        homeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeActivity);
        this.finish();

    }
}
