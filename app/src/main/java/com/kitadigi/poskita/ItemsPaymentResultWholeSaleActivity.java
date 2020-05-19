package com.kitadigi.poskita;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsPaymentResultWholeSaleActivity extends AppCompatActivity{

    private static final String TAG = ItemsPaymentResultWholeSaleActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_remark_transaksi, tv_remark_change, tv_change;
    ImageView iv_back;
    Button btn_finish;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    String change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_results);
        context                     = this;

        /* init fonts */
        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsBold                   = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_remark_transaksi         = findViewById(R.id.tv_remark_transaksi);
        tv_remark_change            = findViewById(R.id.tv_remark_change);
        tv_change                   = findViewById(R.id.tv_change);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /* Button */
        btn_finish                  = findViewById(R.id.btn_finish);

        /* set fonts */
        tv_nav_header.setTypeface(fontsBold);
        tv_remark_transaksi.setTypeface(fonts);
        tv_remark_change.setTypeface(fonts);
        tv_change.setTypeface(fontsBold);

        tv_remark_change.setVisibility(View.GONE);
        tv_change.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(ItemsPaymentResultWholeSaleActivity.this, MainActivity.class);
                startActivity(homeActivity);
                finish();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(ItemsPaymentResultWholeSaleActivity.this, MainActivity.class);
                homeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeActivity);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
