package com.kitadigi.poskita;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.adapter.ItemsTransactionsFixAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportDetailsActivity extends AppCompatActivity{

    private static final String TAG = ReportDetailsActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_header, tv_remark_information, tv_invoice_wholesale, tv_date_wholesale,
            tv_user_wholesale, tv_payment_wholesale, tv_invoice, tv_date, tv_payment, tv_remark_subtotal,
            tv_subtotal, tv_remark_total, tv_total;
    ImageView iv_back;
    RecyclerView rv_items;
    RelativeLayout rl_close, rl_information, rl_information_data_wholesale, rl_information_data,
            rl_detail, rl_detail_data;
    Button btn_back;

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

    String jsonlistTransactionsDetail, jsonListTransaction, jsonListUsers, jsonListPayment, totalBayar, kodeTransaction, tipe;

    Animation fadeIn, fadeOut;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reports);
        context = this;


        /* init sqlite db */
        db                          = new Database(context);

        /* Calendar */
        tz                          = TimeZone.getTimeZone("GMT+0700");

        /* init fonts */
        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsBold                   = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_header                   = findViewById(R.id.tv_header);
        tv_remark_information       = findViewById(R.id.tv_remark_information);
        tv_invoice_wholesale        = findViewById(R.id.tv_invoice_wholesale);
        tv_date_wholesale           = findViewById(R.id.tv_date_wholesale);
        tv_user_wholesale           = findViewById(R.id.tv_user_wholesale);
        tv_payment_wholesale        = findViewById(R.id.tv_payment_wholesale);
        tv_invoice                  = findViewById(R.id.tv_invoice);
        tv_date                     = findViewById(R.id.tv_date);
        tv_payment                  = findViewById(R.id.tv_payment);
        tv_remark_subtotal          = findViewById(R.id.tv_remark_subtotal);
        tv_subtotal                 = findViewById(R.id.tv_subtotal);
        tv_remark_total             = findViewById(R.id.tv_remark_total);
        tv_total                    = findViewById(R.id.tv_total);

        /* init recycler view */
        rv_items                    = findViewById(R.id.rv_items);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /*Relative Layout */
        rl_close                        = findViewById(R.id.rl_close);
        rl_information                  = findViewById(R.id.rl_information);
        rl_information_data_wholesale   = findViewById(R.id.rl_information_data_wholesale);
        rl_information_data             = findViewById(R.id.rl_information_data);
        rl_detail                       = findViewById(R.id.rl_detail);
        rl_detail_data                  = findViewById(R.id.rl_detail_data);

        /* Button */
        btn_back                    = findViewById(R.id.btn_back);

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
        transactionsFixAdapter = new ItemsTransactionsFixAdapter(context, transactionsDetails);
        rv_items.setAdapter(transactionsFixAdapter);

        /* set fonts */
        tv_nav_header.setTypeface(fontsBold);
        tv_header.setTypeface(fonts);
        tv_remark_information.setTypeface(fonts);
        tv_invoice_wholesale.setTypeface(fonts);
        tv_date_wholesale.setTypeface(fonts);
        tv_user_wholesale.setTypeface(fonts);
        tv_payment_wholesale.setTypeface(fonts);
        tv_invoice.setTypeface(fonts);
        tv_date.setTypeface(fonts);
        tv_payment.setTypeface(fonts);
        tv_remark_subtotal.setTypeface(fonts);
        tv_subtotal.setTypeface(fonts);
        tv_remark_total.setTypeface(fontsBold);
        tv_total.setTypeface(fontsBold);

        btn_back.setTypeface(fontsBold);
        btn_back.setText("KEMBALI");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDetailsActivity.super.onBackPressed();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDetailsActivity.super.onBackPressed();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras                   = getIntent().getExtras();
        kodeTransaction                 = extras.getString("kodeTransaction");
        tipe                            = extras.getString("tipe");
        jsonListTransaction             = db.getListTransactions(kodeTransaction);
        jsonListPayment                 = db.getListPayment(kodeTransaction);

        if(tipe.equals("wholesale")){
            tv_header.setText("Pembelian Anda");
            jsonListUsers                   = db.getListAddress(kodeTransaction);
            jsonlistTransactionsDetail = db.getListTransactionsDetail(kodeTransaction, "2");
            setDataTransaction(jsonListTransaction, tv_invoice_wholesale, tv_date_wholesale);
            setDataTransactionAddress(jsonListUsers);
            setDataTransactionPayment(jsonListPayment, tv_payment_wholesale);
        }else if(tipe.equals("penjualan")){
            jsonlistTransactionsDetail = db.getListTransactionsDetail(kodeTransaction, "1");
            setDataTransaction(jsonListTransaction, tv_invoice, tv_date);
            setDataTransactionPayment(jsonListPayment, tv_payment);
        }


//        if(tipe == "1"){
//
//        }else if(tipe == "2"){
//            jsonlistTransactionsDetail = db.getListTransactionsDetail(kodeTransaction, tipe);
//        }
        rv_items.setVisibility(View.GONE);
        rl_close.setVisibility(View.GONE);
        rl_information.setVisibility(View.GONE);
        rl_information_data_wholesale.setVisibility(View.GONE);
        rl_information_data.setVisibility(View.GONE);
        rl_detail.setVisibility(View.GONE);
        rl_detail_data.setVisibility(View.GONE);
        btn_back.setEnabled(false);
        tv_remark_total.setText("Total");
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        setData(jsonlistTransactionsDetail);


    }

    public void setData(final String json) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListItems(json, tipe);
            }
        }, 200);

    }

    public void setDataTransaction(final String json, final TextView tv_inv, final TextView tv_dat) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListTransaction(json, tv_inv, tv_dat);
            }
        }, 200);
    }

    public void fetchListTransaction(String json, final TextView tv_inv, final TextView tv_dat){
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "List Transaction => "+array.toString());
            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                tv_inv.setText(obj.getString("no"));
                tv_dat.setText(obj.getString("date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataTransactionAddress(final String json) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListTransactionAddress(json);
            }
        }, 200);
    }

    public void fetchListTransactionAddress(String json){
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "List Address => "+array.toString());
            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                tv_user_wholesale.setText(obj.getString("userAddress").toUpperCase());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataTransactionPayment(final String json, final TextView tv_pay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListTransactionPayment(json, tv_pay);
            }
        }, 200);
    }

    public void fetchListTransactionPayment(String json, TextView tv_pay){
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "List Payment => "+array.toString());
            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                tv_pay.setText(obj.getString("method").toUpperCase());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void fetchListItems(String json, String tipe){
        transactionsDetails.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "List Items => "+array.toString());
            int total = 0;
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    TransactionsDetail item = new TransactionsDetail();
                    item.setIdItems(obj.getString("idItems"));
                    item.setItemsName(obj.getString("itemsName"));
                    item.setQty(obj.getString("qty"));
                    item.setItemsPrice(obj.getString("itemsPrice"));
                    item.setSubTotal(obj.getString("subTotal"));
                    transactionsDetails.add(item);
                    total += Integer.parseInt(obj.getString("subTotal"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String price            = formatter.format(total);

            totalBayar              = String.valueOf(total);
            tv_subtotal.setText(price);
            tv_total.setText(price);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            rv_items.setVisibility(View.VISIBLE);
            rl_close.setVisibility(View.VISIBLE);
            rl_information.setVisibility(View.VISIBLE);
            rl_detail.setVisibility(View.VISIBLE);
            rl_detail_data.setVisibility(View.VISIBLE);

            if(tipe.equals("penjualan")){
                rl_information_data.setVisibility(View.VISIBLE);
                rl_information_data_wholesale.setVisibility(View.GONE);
            }else if(tipe.equals("wholesale")){
                rl_information_data.setVisibility(View.GONE);
                rl_information_data_wholesale.setVisibility(View.VISIBLE);
            }
            btn_back.setEnabled(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Dibatalkan", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                showResultDialogue(result.getContents());
                //Toast.makeText(this, "Scan Dibatalkan", Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //method to construct dialogue with scan results
    public void showResultDialogue(final String result) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Scan Result")
                .setMessage("Scanned result is " + result)
                .setPositiveButton("Copy result", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Scan Result", result);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ReportDetailsActivity.this, "Result copied to clipboard", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(ReportDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(){
        sweetAlertDialog = new SweetAlertDialog(ReportDetailsActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("TRANSAKSI SUKSES")
                .setConfirmText("TUTUP")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ReportDetailsActivity.super.onBackPressed();
                    }
                });
        sweetAlertDialog.setCancelable(false);
    }

    public void showAlertDialog(){
        if(!sweetAlertDialog.isShowing()){
            sweetAlertDialog.show();
        }
    }

    public void hideAlertDialog(){
        if(sweetAlertDialog != null && sweetAlertDialog.isShowing()){
            sweetAlertDialog.dismiss();
        }
    }

}
