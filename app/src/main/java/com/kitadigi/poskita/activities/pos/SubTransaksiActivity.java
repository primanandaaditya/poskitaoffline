package com.kitadigi.poskita.activities.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsTransactionsActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.ItemsTransactionsAdapter;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.kitadigi.poskita.util.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubTransaksiActivity extends BaseActivity {

    private static final String TAG = ItemsTransactionsActivity.class.getName();
    Context context;

    private SessionManager sessionManager;

    private SubTransaksiAdapter subTransaksiAdapter;

    /* init ui */
    TextView tv_nav_header, tv_header, tv_remark_detail,
            tv_remark_subtotal, tv_subtotal, tv_remark_total, tv_total, tv_add;
    ImageView iv_back;
    RecyclerView rv_items;
    RelativeLayout rl_save, rl_add, rl_detail, rl_detail_data;
    Button btn_save;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    private AlertDialog alertDialog;
    private SweetAlertDialog sweetAlertDialog;
    LayoutInflater inflater = null;

    private List<TransactionsDetail> transactionsDetails = new ArrayList<TransactionsDetail>();
    private ItemsTransactionsAdapter transactionsDetailsAdapter;

    private  ListTransactionDetail listTransactionDetail;

    /* Sqlite database */
    Database db;
    TimeZone tz;

    String jsonlistTransactionsDetail, totalBayar;

    Animation fadeIn, fadeOut;

    long countTransactions;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_transaksi);


        context = this;


        /* init sqlite db */
        db                          = new Database(context);

        /* Calendar */
        tz                          = TimeZone.getTimeZone("GMT+0700");


        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_header                   = findViewById(R.id.tv_header);
        tv_remark_detail            = findViewById(R.id.tv_remark_detail);
        tv_remark_subtotal          = findViewById(R.id.tv_remark_subtotal);
        tv_subtotal                 = findViewById(R.id.tv_subtotal);
        tv_remark_total             = findViewById(R.id.tv_remark_total);
        tv_total                    = findViewById(R.id.tv_total);
        tv_add                      = findViewById(R.id.tv_add);


        /* init recycler view */
        rv_items                    = findViewById(R.id.rv_items);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /*Relative Layout */
        rl_save                     = findViewById(R.id.rl_save);
        rl_add                      = findViewById(R.id.rl_add);
        rl_detail                   = findViewById(R.id.rl_detail);
        rl_detail_data              = findViewById(R.id.rl_detail_data);

        /* Button */
        btn_save                    = findViewById(R.id.btn_save);

        /* Shimmer */
        mShimmerViewContainer       = findViewById(R.id.shimmer_view_container);


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

//        transactionsDetailsAdapter = new ItemsTransactionsAdapter(context, transactionsDetails, this);
//        rv_items.setAdapter(transactionsDetailsAdapter);

        /* set fonts */
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_header);
        this.applyFontRegularToTextView(tv_remark_detail);
        this.applyFontRegularToTextView(tv_remark_subtotal);
        this.applyFontRegularToTextView(tv_subtotal);
        this.applyFontBoldToTextView(tv_remark_total);
        this.applyFontBoldToTextView(tv_total);
        this.applyFontBoldToButton(btn_save);

        tv_remark_total.setText(getResources().getString(R.string.total));
        btn_save.setText(R.string.selanjutnya);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubTransaksiActivity.super.onBackPressed();
            }
        });

        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubTransaksiActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save();
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        //start
        sessionManager=new SessionManager(SubTransaksiActivity.this);

        //dapatkan intent
        //berupa list dari detail transaksi
        Gson gson = new Gson();
        String list_transaksi_detail = getIntent().getStringExtra("list_transaksi_detail");
        listTransactionDetail = gson.fromJson(list_transaksi_detail,ListTransactionDetail.class);
        subTransaksiAdapter=new SubTransaksiAdapter(SubTransaksiActivity.this,listTransactionDetail,this,null,true);
        rv_items.setAdapter(subTransaksiAdapter);


        //tampilkan total
       hitungTotalPenjualan();



//        jsonlistTransactionsDetail = db.getListTransactionsDetail("0", "1");
        rv_items.setVisibility(View.GONE);
        rl_save.setVisibility(View.GONE);
        rl_add.setVisibility(View.GONE);
        rl_detail.setVisibility(View.GONE);
        rl_detail_data.setVisibility(View.GONE);
        btn_save.setEnabled(false);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        setData(jsonlistTransactionsDetail);
    }

    public void setData(final String json) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListItems(json);
            }
        }, 200);

    }


    public void fetchListItems(String json){


        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        rv_items.setVisibility(View.VISIBLE);
        rl_save.setVisibility(View.VISIBLE);
        rl_add.setVisibility(View.VISIBLE);
        rl_detail.setVisibility(View.VISIBLE);
        rl_detail_data.setVisibility(View.VISIBLE);
        btn_save.setEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();

        subTransaksiAdapter.notifyDataSetChanged();
        sessionManager.gantiPenjualanOffline(listTransactionDetail.getTransactionsDetails());
    }



    @Override
    protected void onResume() {
        super.onResume();

        //ini berguna jika layar MetodePembayara ditutup
        //dan kembali ke layar ini
         listTransactionDetail= sessionManager.penjualanOfflineToTransactionDetail();
        subTransaksiAdapter=new SubTransaksiAdapter(SubTransaksiActivity.this,listTransactionDetail,this,null,true);
        rv_items.setAdapter(subTransaksiAdapter);
        hitungTotalPenjualan();

    }

    public void hitungTotalPenjualan(){
        int qty =0;
        int subtotal = 0;
        int harga=0;
        int total = 0;

        if (listTransactionDetail.getTransactionsDetails().size()==0){
            total =0;
        }else{
            for (TransactionsDetail transactionsDetail: listTransactionDetail.getTransactionsDetails()){
                qty =  Integer.parseInt(transactionsDetail.getQty());
                harga = Integer.parseInt(transactionsDetail.getItemsPrice());
                subtotal = qty * harga;
                total = total + subtotal;
            }

        }


        //tampilkan total
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price            = formatter.format(total);

        totalBayar              = String.valueOf(total);
        tv_subtotal.setText(price);
        tv_total.setText(price);
    }



    private void save(){
        //jika penjualan = 0
        //tidak dapat melakukan metode pembayaran
        if (listTransactionDetail.getTransactionsDetails().size()==0){
            this.showToast(getResources().getString(R.string.tidak_ada_penjualan));
        }else{

            //update list transaksi di session
            subTransaksiAdapter.notifyDataSetChanged();
            sessionManager.gantiPenjualanOffline(listTransactionDetail.getTransactionsDetails());


            //pindah ke layar Metode Pembayaran
            //sekaligus intentnya
            Gson gson = new Gson();
            Intent intent = new Intent(SubTransaksiActivity.this, MetodePembayaranActivity.class);
            intent.putExtra("list_transaction_detail", gson.toJson(listTransactionDetail));
            startActivity(intent);
        }
    }

}
