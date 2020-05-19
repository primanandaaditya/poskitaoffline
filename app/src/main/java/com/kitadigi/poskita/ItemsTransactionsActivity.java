package com.kitadigi.poskita;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.kitadigi.poskita.adapter.ItemsTransactionsAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemsTransactionsActivity extends AppCompatActivity{

    private static final String TAG = ItemsTransactionsActivity.class.getName();
    Context context;

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

    /* Sqlite database */
    Database db;
    TimeZone tz;

    String jsonlistTransactionsDetail, totalBayar;

    Animation fadeIn, fadeOut;

    long countTransactions;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transactions);
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
        transactionsDetailsAdapter = new ItemsTransactionsAdapter(context, transactionsDetails, this);
        rv_items.setAdapter(transactionsDetailsAdapter);

        /* set fonts */
        tv_nav_header.setTypeface(fontsBold);
        tv_header.setTypeface(fonts);
        tv_remark_detail.setTypeface(fonts);
        tv_remark_subtotal.setTypeface(fonts);
        tv_subtotal.setTypeface(fonts);
        tv_add.setTypeface(fonts);
        tv_remark_total.setTypeface(fontsBold);
        tv_total.setTypeface(fontsBold);
        btn_save.setTypeface(fontsBold);

        tv_remark_total.setText("Total");
        btn_save.setText("SELANJUTNYA");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsTransactionsActivity.super.onBackPressed();
            }
        });

        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsTransactionsActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payment = new Intent(ItemsTransactionsActivity.this, ItemsPaymentMethodActivity.class);
                payment.putExtra("transaction", jsonlistTransactionsDetail);
                startActivity(payment);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        jsonlistTransactionsDetail = db.getListTransactionsDetail("0", "1");
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
            tv_subtotal.setText(price);
            tv_total.setText(price);
            transactionsDetailsAdapter.notifyDataSetChanged();
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            rv_items.setVisibility(View.VISIBLE);
            rl_save.setVisibility(View.VISIBLE);
            rl_add.setVisibility(View.VISIBLE);
            rl_detail.setVisibility(View.VISIBLE);
            rl_detail_data.setVisibility(View.VISIBLE);
            btn_save.setEnabled(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchTotal(String json){
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Total => "+array.toString());
            int total = 0;
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateData(String idItems, String itemsName, String qty, String price){
        int total = Integer.parseInt(qty) * Integer.parseInt(price);
//        DecimalFormat formatter = new DecimalFormat("#,###,###");
//        String prices            = formatter.format(total);
//        tv_subtotal.setText(prices);
//        tv_total.setText(prices);
        int check = db.updateTransactions("0", idItems, itemsName, qty, price, String.valueOf(total), "1");
        if(check == 1){
            jsonlistTransactionsDetail = db.getListTransactionsDetail("0", "1");
            fetchTotal(jsonlistTransactionsDetail);
        }

    }

    public void deleteData(String idItems, String itemsName){
            setDeleteItems("Hapus " + itemsName + " ?", idItems);
            showAlertDialog();
    }

    public void setDeleteItems(String message, final String idItems){
        sweetAlertDialog = new SweetAlertDialog(ItemsTransactionsActivity.this, SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText("KONFIRMASI")
                .setContentText(message)
                .setCancelText("BATAL")
                .setConfirmText("HAPUS")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        setLoadingDialog("Loading data ...");
                        showAlertDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideAlertDialog();
                                db.deleteTransactionsItems("0", idItems, "1");
                                countTransactions = db.fetchTransactionsItems("1");
                                if(countTransactions == 0){
                                    ItemsTransactionsActivity.super.onBackPressed();
                                    finish();
                                }
                                jsonlistTransactionsDetail = db.getListTransactionsDetail("0", "1");
                                setData(jsonlistTransactionsDetail);
                            }
                        }, 500);
                    }
                });
        sweetAlertDialog.setCancelable(false);
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


    private void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(ItemsTransactionsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(){
        sweetAlertDialog = new SweetAlertDialog(ItemsTransactionsActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("TRANSAKSI SUKSES")
                .setConfirmText("TUTUP")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ItemsTransactionsActivity.super.onBackPressed();
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
