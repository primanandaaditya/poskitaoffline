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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.adapter.ItemsTransactionsFixAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.kitadigi.poskita.util.ScannerActivity;
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

public class ItemsPaymentMethodActivity extends AppCompatActivity{

    private static final String TAG = ItemsPaymentMethodActivity.class.getName();
    Context context;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
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
        transactionsFixAdapter = new ItemsTransactionsFixAdapter(context, transactionsDetails);
        rv_items.setAdapter(transactionsFixAdapter);

        /* set fonts */
        tv_nav_header.setTypeface(fontsBold);
        tv_header.setTypeface(fonts);
        tv_remark_payment.setTypeface(fonts);
        rb_cash.setTypeface(fonts);
        rb_soon.setTypeface(fonts);
        btn_save.setTypeface(fontsBold);
        btn_save.setText("SELANJUTNYA");

        int selectedId = rb_group.getCheckedRadioButtonId();

        if(selectedId == rb_cash.getId()){
            rb_cash.setTypeface(fontsBold);
            rb_soon.setTypeface(fonts);
        }else if(selectedId == rb_soon.getId()){
            rb_cash.setTypeface(fonts);
            rb_soon.setTypeface(fontsBold);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsPaymentMethodActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rb_group.getCheckedRadioButtonId();

                if(selectedId == rb_cash.getId()){
                    Intent payment = new Intent(ItemsPaymentMethodActivity.this, ItemsPaymentActivity.class);
                    payment.putExtra("total", totalBayar);
                    startActivity(payment);
                }else if(selectedId == rb_soon.getId()){
                    new IntentIntegrator(ItemsPaymentMethodActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }

            }
        });

    }

    public void onClickedRadioButton(View view){
        boolean  checked = ((RadioButton) view).isChecked();
        switch(view.getId()){

            case R.id.rb_cash:
                if(checked)
                    rb_cash.setTypeface(fontsBold);
                    rb_soon.setTypeface(fonts);
                break;

            case R.id.rb_soon:
                if(checked)
                    rb_soon.setTypeface(fontsBold);
                    rb_cash.setTypeface(fonts);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras                   = getIntent().getExtras();
        jsonlistTransactionsDetail      = extras.getString("transaction");
        rv_items.setVisibility(View.GONE);
        rl_save.setVisibility(View.GONE);
        rl_payment.setVisibility(View.GONE);
        rl_payment_data.setVisibility(View.GONE);
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
        transactionsDetails.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Hasil 2 => "+array.toString());
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
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            rv_items.setVisibility(View.VISIBLE);
            rl_save.setVisibility(View.VISIBLE);
            rl_payment.setVisibility(View.VISIBLE);
            rl_payment_data.setVisibility(View.VISIBLE);
            btn_save.setEnabled(true);
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
                        Toast.makeText(ItemsPaymentMethodActivity.this, "Result copied to clipboard", Toast.LENGTH_SHORT).show();

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
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentMethodActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(){
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentMethodActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("TRANSAKSI SUKSES")
                .setConfirmText("TUTUP")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ItemsPaymentMethodActivity.super.onBackPressed();
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
