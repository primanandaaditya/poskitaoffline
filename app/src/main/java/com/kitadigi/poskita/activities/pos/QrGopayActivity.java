package com.kitadigi.poskita.activities.pos;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.StringUtil;

public class QrGopayActivity extends BaseActivity {

    Button btnSayaSudahBayar;
    TextView tv_nav_header;
    ImageView iv_back,iv;


    Intent intent;
    String strIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_gopay);

        findID();

    }

    void findID(){

        btnSayaSudahBayar = (Button)findViewById(R.id.btn_sudah_bayar);
        tv_nav_header = (TextView)findViewById(R.id.tv_nav_header);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv=(ImageView)findViewById(R.id.iv);


        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontBoldToButton(btnSayaSudahBayar);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get Intent
        //dari MetodePembayaranActivity.java
        intent = getIntent();

        //tampung informasi pembayaran
        strIntent = intent.getStringExtra("total");

        //tamppilkan qr code dalam imageview
        StringUtil.generateQrCode(strIntent, iv);

    }
}
