package com.kitadigi.poskita.activities.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsPaymentActivity;
import com.kitadigi.poskita.ItemsPaymentMethodActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.ItemsTransactionsFixAdapter;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.SubTotalModel;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.kitadigi.poskita.util.ScannerActivity;
import com.kitadigi.poskita.util.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MetodePembayaranActivity extends BaseActivity {

    private static final String TAG = ItemsPaymentMethodActivity.class.getName();
    Context context;

    //init list transaksi
    ListTransactionDetail listTransactionDetail;

    SessionManager sessionManager;

    /* init ui */
    TextView tv_nav_header, tv_header, tv_remark_payment;
    ImageView iv_back;
    RecyclerView rv_items;
    RadioGroup rb_group;
    RadioButton rb_cash, rb_soon;
    RelativeLayout rl_save, rl_payment, rl_payment_data;
    Button btn_save;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    private AlertDialog alertDialog;
    private SweetAlertDialog sweetAlertDialog;
    LayoutInflater inflater = null;

    private List<TransactionsDetail> transactionsDetails = new ArrayList<TransactionsDetail>();
    private ItemsTransactionsFixAdapter transactionsFixAdapter;

    /* Sqlite database */
    Database db;
    TimeZone tz;

    String jsonlistTransactionsDetail, totalBayar;

    Animation fadeIn, fadeOut;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode_pembayaran);


        context = this;

        //init session
        sessionManager=new SessionManager(context);

        /* init sqlite db */
        db                          = new Database(context);

        /* Calendar */
        tz                          = TimeZone.getTimeZone("GMT+0700");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_header                   = findViewById(R.id.tv_header);
        tv_remark_payment           = findViewById(R.id.tv_remark_payment);

        /* init recycler view */
        rv_items                    = findViewById(R.id.rv_items);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /*Relative Layout */
        rl_save                     = findViewById(R.id.rl_save);
        rl_payment                  = findViewById(R.id.rl_payment);
        rl_payment_data             = findViewById(R.id.rl_payment_data);

        /* Button */
        btn_save                    = findViewById(R.id.btn_save);

        /* Shimmer */
        mShimmerViewContainer       = findViewById(R.id.shimmer_view_container);

        /* Radio */
        rb_group                    = findViewById(R.id.rb_group);
        rb_cash                     = findViewById(R.id.rb_cash);
        rb_soon                     = findViewById(R.id.rb_soon);


        /* animation */
        fadeIn                      = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut                     = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        rv_items.setHasFixedSize(true);
        rv_items.setItemViewCacheSize(20);
        rv_items.setDrawingCacheEnabled(true);
        rv_items.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv_items.setLayoutManager(mLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(context, R.drawable.item_decorator)));


        /* set fonts */
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_header);
        this.applyFontRegularToTextView(tv_remark_payment);
        this.applyFontRegularToRadioButton(rb_cash);
        this.applyFontRegularToRadioButton(rb_soon);
        this.applyFontBoldToButton(btn_save);

        btn_save.setText(getResources().getString(R.string.selanjutnya));

        int selectedId = rb_group.getCheckedRadioButtonId();

        if(selectedId == rb_cash.getId()){
            this.applyFontBoldToRadioButton(rb_cash);
            this.applyFontRegularToRadioButton(rb_soon);
        }else if(selectedId == rb_soon.getId()){
            this.applyFontBoldToRadioButton(rb_soon);
            this.applyFontRegularToRadioButton(rb_cash);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetodePembayaranActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rb_group.getCheckedRadioButtonId();

                if(selectedId == rb_cash.getId()){

                    //sebelum ganti layar ke ItemsPaymentActivity
                    //cari total penjualan offline dari session

                    SubTotalModel subTotalModel= sessionManager.sumTotalPenjualanOffline();
                    totalBayar=String.valueOf(subTotalModel.getSum_total());

                    Intent payment = new Intent(MetodePembayaranActivity.this, ItemsPaymentActivity.class);
                    payment.putExtra("total", totalBayar);
                    startActivity(payment);
                }else if(selectedId == rb_soon.getId()){
                    new IntentIntegrator(MetodePembayaranActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }

            }
        });


        //init session, untuk nyimpan list transaksi
        //bila activity di-back/pause
        sessionManager=new SessionManager(MetodePembayaranActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Gson gson = new Gson();
        String list_transaksi_detail = intent.getStringExtra("list_transaction_detail");
        listTransactionDetail = gson.fromJson(list_transaksi_detail,ListTransactionDetail.class);

        mShimmerViewContainer.setVisibility(View.GONE);
        rv_items.setVisibility(View.VISIBLE);

        MetodePembayaranAdapter metodePembayaranAdapter=new MetodePembayaranAdapter(MetodePembayaranActivity.this, listTransactionDetail);
        rv_items.setAdapter(metodePembayaranAdapter);

        rl_payment_data.setVisibility(View.VISIBLE);
        rl_payment.setVisibility(View.VISIBLE);
        rl_save.setVisibility(View.VISIBLE);
        btn_save.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sessionManager.gantiPenjualanOffline(listTransactionDetail.getTransactionsDetails());

    }

    public void onClickedRadioButton(View view){
        boolean  checked = ((RadioButton) view).isChecked();
        switch(view.getId()){

            case R.id.rb_cash:
                if(checked)
                    this.applyFontBoldToRadioButton(rb_cash);
                    this.applyFontRegularToRadioButton(rb_soon);
                break;

            case R.id.rb_soon:
                if(checked)
                    this.applyFontBoldToRadioButton(rb_soon);
                    this.applyFontRegularToRadioButton(rb_cash);
                break;
        }
    }
}
