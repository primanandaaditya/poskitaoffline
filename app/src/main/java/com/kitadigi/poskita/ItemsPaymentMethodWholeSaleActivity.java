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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.adapter.ItemsSelectAddressWholeSaleAdapter;
import com.kitadigi.poskita.adapter.ItemsTransactionsFixAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.Address;
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

public class ItemsPaymentMethodWholeSaleActivity extends AppCompatActivity{

    private static final String TAG = ItemsPaymentMethodWholeSaleActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_header, tv_remark_payment, tv_remark_address_user, tv_divide_address_user,
            tv_address_user, tv_remark_address_data, tv_divide_address_data, tv_address_data, tv_remark_address_phone,
            tv_divide_address_phone, tv_address_phone;
    ImageView iv_back, iv_edit;
    RecyclerView rv_items;
    RadioGroup rb_group;
    RadioButton rb_soon;
    RelativeLayout rl_save, rl_payment, rl_payment_data, rl_address, rl_address_data;
    Button btn_save;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    private AlertDialog alertDialog;
    private SweetAlertDialog sweetAlertDialog;
    LayoutInflater inflater = null;

    private List<TransactionsDetail> transactionsDetails = new ArrayList<TransactionsDetail>();
    private ItemsTransactionsFixAdapter transactionsFixAdapter;

    private List<Address> addresses = new ArrayList<Address>();
    private ItemsSelectAddressWholeSaleAdapter addressListAdapter;

    /* Sqlite database */
    Database db;
    TimeZone tz;

    String jsonlistTransactionsDetail, jsonListAddress, totalBayar, recipient, address, phone;

    Animation fadeIn, fadeOut;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_wholesale);
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
        tv_remark_address_user      = findViewById(R.id.tv_remark_address_user);
        tv_divide_address_user      = findViewById(R.id.tv_divide_address_user);
        tv_address_user             = findViewById(R.id.tv_address_user);
        tv_remark_address_data      = findViewById(R.id.tv_remark_address_data);
        tv_divide_address_data      = findViewById(R.id.tv_divide_address_data);
        tv_address_data             = findViewById(R.id.tv_address_data);
        tv_remark_address_phone     = findViewById(R.id.tv_remark_address_phone);
        tv_divide_address_phone     = findViewById(R.id.tv_divide_address_phone);
        tv_address_phone            = findViewById(R.id.tv_address_phone);

        /* init recycler view */
        rv_items                    = findViewById(R.id.rv_items);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);
        iv_edit                     = findViewById(R.id.iv_edit);
        /*Relative Layout */
        rl_save                     = findViewById(R.id.rl_save);
        rl_payment                  = findViewById(R.id.rl_payment);
        rl_payment_data             = findViewById(R.id.rl_payment_data);
        rl_address                  = findViewById(R.id.rl_address);
        rl_address_data             = findViewById(R.id.rl_address_data);

        /* Button */
        btn_save                    = findViewById(R.id.btn_save);

        /* Shimmer */
        mShimmerViewContainer       = findViewById(R.id.shimmer_view_container);

        /* Radio */
        rb_group                    = findViewById(R.id.rb_group);
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
        tv_remark_address_user.setTypeface(fonts);
        tv_divide_address_user.setTypeface(fonts);
        tv_address_user.setTypeface(fonts);
        tv_remark_address_data.setTypeface(fonts);
        tv_divide_address_data.setTypeface(fonts);
        tv_address_data.setTypeface(fonts);
        tv_remark_address_phone.setTypeface(fonts);
        tv_divide_address_phone.setTypeface(fonts);
        tv_address_phone.setTypeface(fonts);

        rb_soon.setTypeface(fonts);
        btn_save.setTypeface(fontsBold);

        tv_header.setText("Pembelian Anda");
        btn_save.setText("SELANJUTNYA");

        rb_soon.setTypeface(fontsBold);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsPaymentMethodWholeSaleActivity.super.onBackPressed();
            }
        });
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingDialog("Loading ...");
                showAlertDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideAlertDialog();
                        editAddress();
                    }
                }, 300);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipient   = tv_address_user.getText().toString();
                address     = tv_address_data.getText().toString();
                phone       = tv_address_phone.getText().toString();
                Intent payment = new Intent(ItemsPaymentMethodWholeSaleActivity.this, EntryPinActivity.class);
                payment.putExtra("total", totalBayar);
                payment.putExtra("recipient", recipient);
                payment.putExtra("address", address);
                payment.putExtra("phone", phone);
                startActivity(payment);
            }
        });

    }

    public void onClickedRadioButton(View view){
        boolean  checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.rb_soon:
                if(checked)
                    rb_soon.setTypeface(fontsBold);
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
        rl_address.setVisibility(View.GONE);
        rl_address_data.setVisibility(View.GONE);
        btn_save.setEnabled(false);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        setData(jsonlistTransactionsDetail);

    }

    public void editAddress(){

        DecimalFormat formatter             = new DecimalFormat("#,###,###");
        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(context);
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_add_items_dialog, null);
        EditText et_search                  = dialogView.findViewById(R.id.et_search);
        ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_view_container);
        RecyclerView recyclerView           = dialogView.findViewById(R.id.rv_items);
        RelativeLayout rl_content           = dialogView.findViewById(R.id.rl_content);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(context, R.drawable.item_decorator)));
        addressListAdapter                  = new ItemsSelectAddressWholeSaleAdapter(context, addresses, this);
        recyclerView.setAdapter(addressListAdapter);

        et_search.setHint("Cari Alamat");
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressListAdapter.getFilter().filter(s.toString());
            }
        });

        jsonListAddress                               = db.getListAddress();
        setDataAddressDialog(jsonListAddress, shimmerFrameLayout, recyclerView, rl_content);

        et_search.setTypeface(fonts);

        dialogBuilder.setView(dialogView);

        alertDialog                         = dialogBuilder.create();

        Window window                       = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center

        Button btn_close                      = dialogView.findViewById(R.id.btn_close);
        btn_close.setTypeface(fontsBold);
        btn_close.setText("Tutup");
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                onResume();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

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
            rl_address.setVisibility(View.VISIBLE);
            btn_save.setEnabled(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAddress(String name, String address, String phone){
        hide();
        tv_address_user.setText(name);
        tv_address_data.setText(address);
        tv_address_phone.setText(phone);
        rl_address_data.setVisibility(View.VISIBLE);
    }

    public void hide(){
        alertDialog.dismiss();
        alertDialog.hide();
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

    public void setDataAddressDialog(final String json, final ShimmerFrameLayout shimmerFrameLayout, final RecyclerView recyclerView, final RelativeLayout relativeLayout) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListAddressDialog(json, shimmerFrameLayout, recyclerView, relativeLayout);
            }
        }, 500);

    }
    public void fetchListAddressDialog(String json, ShimmerFrameLayout shimmerFrameLayout, RecyclerView recyclerView, RelativeLayout rl_content){
        addresses.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            //Log.d(TAG, "Hasil 2 => "+array.toString());
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    Address item = new Address();
                    item.setIdAddress(obj.getString("idAddress"));
                    item.setNameAddress(obj.getString("nameAddress"));
                    item.setUserAddress(obj.getString("userAddress"));
                    item.setUsersPhone(obj.getString("usersPhone"));
                    addresses.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            addressListAdapter.notifyDataSetChanged();
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rl_content.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBg));
        } catch (JSONException e) {
            e.printStackTrace();
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
                        Toast.makeText(ItemsPaymentMethodWholeSaleActivity.this, "Result copied to clipboard", Toast.LENGTH_SHORT).show();

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


    public void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentMethodWholeSaleActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(){
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentMethodWholeSaleActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("TRANSAKSI SUKSES")
                .setConfirmText("TUTUP")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ItemsPaymentMethodWholeSaleActivity.super.onBackPressed();
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
