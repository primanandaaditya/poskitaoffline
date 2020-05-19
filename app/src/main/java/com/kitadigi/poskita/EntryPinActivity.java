package com.kitadigi.poskita;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.kitadigi.poskita.database.Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EntryPinActivity extends AppCompatActivity {

    private final int MAX_PIN = 6;
    private TextView tv_title;
    private PinEntryEditText pinEntry;

    private Context context;

    private SweetAlertDialog sweetAlertDialog;

    /* Sqlite database */
    Database db;
    Calendar calendar;
    SimpleDateFormat today;
    String now, kodeTransaction, total, recipient, address, phone, idBalance, balance, balanceNew;

    /* typeface fonts */
    Typeface fonts, fontsItalic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_entry);
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


        /* init fonts */
        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        tv_title = findViewById(R.id.tv_title);
        pinEntry = findViewById(R.id.txt_pin_entry);

        tv_title.setTypeface(fonts);
        pinEntry.setTypeface(fonts);

        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length() == MAX_PIN) {
                        login(str.toString(), kodeTransaction, total);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras                   = getIntent().getExtras();
        total                           = extras.getString("total");
        recipient                       = extras.getString("recipient");
        address                         = extras.getString("address");
        phone                           = extras.getString("phone");
    }

    public void onKeyboardClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                keyPress('1');
                break;
            case R.id.tv_2:
                keyPress('2');
                break;
            case R.id.tv_3:
                keyPress('3');
                break;
            case R.id.tv_4:
                keyPress('4');
                break;
            case R.id.tv_5:
                keyPress('5');
                break;
            case R.id.tv_6:
                keyPress('6');
                break;
            case R.id.tv_7:
                keyPress('7');
                break;
            case R.id.tv_8:
                keyPress('8');
                break;
            case R.id.tv_9:
                keyPress('9');
                break;
            case R.id.tv_0:
                keyPress('0');
                break;
            case R.id.tv_backspace:
                deleteChar();
                break;
        }
    }

    private void keyPress(char c) {
        if (pinEntry.getText().length() < MAX_PIN) {
            pinEntry.setText(pinEntry.getText().append(c).toString());
            tv_title.setText(getString(R.string.input_pin));
        }

    }

    private void deleteChar() {
        if (pinEntry.getText().length() > 0) {
            String s = pinEntry.getText().delete(pinEntry.getText().length() - 1, pinEntry.getText().length()).toString();
            pinEntry.setText(s);
        }
    }
    private void login(String pin, final String kodeTransaction, final String total) {
        setLoadingDialog("Loading ...");
        showAlertDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideAlertDialog();
                db.addTransactions(kodeTransaction, now, total, "1", "wholesale");
                db.updateNoTransactionDetail("0", kodeTransaction, "2");
                db.addPayment(kodeTransaction, "bayar nanti", total, "0", "0");
                db.addTransactionAddress(kodeTransaction, recipient, address, phone);
                HashMap<String, String> balancedata = db.getBalance();
                idBalance   = balancedata.get("id");
                balance     = balancedata.get("balance");
                int newBalance = Integer.parseInt(balance) - Integer.parseInt(total);
                db.updateBalance(idBalance, String.valueOf(newBalance), total);
                Intent success = new Intent(EntryPinActivity.this, ItemsPaymentResultWholeSaleActivity.class);
                startActivity(success);
            }
        }, 2000);
    }

    public void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(EntryPinActivity.this, SweetAlertDialog.PROGRESS_TYPE).setTitleText(message);
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
