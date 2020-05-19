package com.kitadigi.poskita;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.database.Database;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemsPaymentActivity extends BaseActivity {

    private static final String TAG = ItemsPaymentActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_header, tv_payment_currency, tv_payment_total,
            tv_remark_payment, tv_remark_payment_change;
    EditText et_payment, et_change;
    ImageView iv_back;
    RelativeLayout rl_save;
    Button btn_save;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    private AlertDialog alertDialog;
    private SweetAlertDialog sweetAlertDialog;
    LayoutInflater inflater = null;

    /* Sqlite database */
    Database db;
    String total;
    Animation fadeIn, fadeOut;

    String change, payment;

    Calendar calendar;
    SimpleDateFormat today;
    String now, kodeTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_transactions);
        context = this;


        /* init sqlite db */
        db                          = new Database(context);

        TimeZone tz                 = TimeZone.getTimeZone("GMT+0700");
        calendar                    = Calendar.getInstance(tz);

        today                       = new SimpleDateFormat("yyyy-MM-dd");
        now                         = today.format(calendar.getTime());

        HashMap<String, String> getKode = db.getTransactionsNo(now);
        kodeTransaction = getKode.get("no");

        if(kodeTransaction != null){
            kodeTransaction = String.valueOf(Integer.parseInt(kodeTransaction) + 1);
        }else{
            kodeTransaction = "1";
        }

        Log.d(TAG, "Kode, " + kodeTransaction);

        /* init fonts */
//        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
//        fontsBold                   = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
//        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_header                   = findViewById(R.id.tv_header);
        tv_payment_currency         = findViewById(R.id.tv_payment_currency);
        tv_payment_total            = findViewById(R.id.tv_payment_total);
        tv_remark_payment           = findViewById(R.id.tv_remark_payment);
        tv_remark_payment_change    = findViewById(R.id.tv_remark_payment_change);

        /* init edit text */
        et_payment                  = findViewById(R.id.et_payment);
        et_change                   = findViewById(R.id.et_change);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /*Relative Layout */
        rl_save                     = findViewById(R.id.rl_save);

        /* Button */
        btn_save                    = findViewById(R.id.btn_save);

        /* animation */
        fadeIn                      = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut                     = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        /* set fonts */
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_header);
        this.applyFontRegularToTextView(tv_payment_currency);
        this.applyFontRegularToTextView(tv_payment_total);
        this.applyFontRegularToTextView(tv_remark_payment);
        this.applyFontRegularToTextView(tv_remark_payment_change);
        this.applyFontRegularToEditText(et_change);
        this.applyFontRegularToEditText(et_payment);
        this.applyFontBoldToButton(btn_save);

        btn_save.setText(getResources().getString(R.string.bayar));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsPaymentActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingDialog(getResources().getString(R.string.now_loading));
                showAlertDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideAlertDialog();
//                        Log.d(TAG, "Total, " + total);
                        change = et_change.getText().toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", "");
                        payment = et_payment.getText().toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", "");
//                        Log.d(TAG, "change, " + change);
                        db.addTransactions(kodeTransaction, now, total, "1", "penjualan");
                        db.updateNoTransactionDetail("0", kodeTransaction, "1");
                        db.addPayment(kodeTransaction, "tunai", total, payment, change);
                        Intent success = new Intent(ItemsPaymentActivity.this, ItemsPaymentResultActivity.class);

                        //buat integer untuk total penjualan dan uang yang diberikan pembeli
                        Integer total_penjualan = Integer.parseInt(tv_payment_total.getText().toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", ""));
                        Integer dibayar = Integer.parseInt(et_payment.getText().toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", ""));

                        //kedua integer tadi di-intent ke ItemsPaymentResultActivity
                        success.putExtra("change", change);
                        success.putExtra("total_penjualan",total_penjualan);
                        success.putExtra("dibayar", dibayar);
                        startActivity(success);
                    }
                }, 0);
            }
        });
    }

    private void setSuccessDialog(String message){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText(getResources().getString(R.string.sukses))
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();

                    }
                });
        sweetAlertDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras                   = getIntent().getExtras();
        total                           = extras.getString("total");
        DecimalFormat formatter         = new DecimalFormat("#,###,###");
        String price                    = formatter.format(Integer.parseInt(total));
        rl_save.setVisibility(View.GONE);
        tv_payment_total.setText(price);
        et_payment.requestFocus();

        et_change.setText("0");
        et_payment.addTextChangedListener(new NumberTextWatcher(et_payment));
        et_change.addTextChangedListener(new NumberTextWatcher(et_change));
        calculate(et_payment, et_change, Integer.parseInt(total), btn_save);
    }

    private void calculate(EditText editText1, final EditText editText2, int amount, final Button btn_checkout) {
        final int[] value1 = {0};
        final int[] value2 = {amount};
        final int[] total = {0};
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    value1[0] = Integer.parseInt(s.toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", ""));
                    rl_save.setVisibility(View.VISIBLE);
                    if(value1[0] < value2[0]){
                        btn_checkout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedDisabled));
                        btn_checkout.setEnabled(false);
                    }else{
                        btn_checkout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                        btn_checkout.setEnabled(true);
                    }
                    total[0] = value1[0]- value2[0];
                    editText2.setText(String.valueOf(total[0]));
                }else{
                    editText2.setText("0");
                    rl_save.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public class NumberTextWatcher implements TextWatcher {

        private DecimalFormat df;
        private DecimalFormat dfnd;
        private boolean hasFractionalPart;

        private EditText et;

        public NumberTextWatcher(EditText et)
        {
            df = new DecimalFormat("#,###.##");
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,###");
            this.et = et;
            hasFractionalPart = false;
        }

        @SuppressWarnings("unused")
        private static final String TAG = "ReportActivity";

        @Override
        public void afterTextChanged(Editable s)
        {
            et.removeTextChangedListener(this);
            try {
                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    et.setText(df.format(n));
                } else {
                    et.setText(dfnd.format(n));
                }
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (ParseException e) {
                // do nothing?
            }

            et.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
            {
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
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


    private void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(){
        sweetAlertDialog = new SweetAlertDialog(ItemsPaymentActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText(getResources().getString(R.string.transaksi_berhasil))
                .setConfirmText(getResources().getString(R.string.tutup))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ItemsPaymentActivity.super.onBackPressed();
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
