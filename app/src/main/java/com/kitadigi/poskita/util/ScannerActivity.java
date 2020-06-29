package com.kitadigi.poskita.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsPaymentResultActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.database.Database;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class ScannerActivity extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton, finishButton;
    private boolean isFlashLightOn = false;

    ImageView iv_back;
    private TextView tv_nav_header;
    Context context;
    /* typeface fonts */
    Typeface fonts, fontsItalic;

    /* Sqlite database */
    Database db;
    Calendar calendar;
    SimpleDateFormat today;
    String now, kodeTransaction, total, jsonlistTransactionsDetail, totalBayar, idBalance, balance, balanceNew ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
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

        tv_nav_header       = findViewById(R.id.tv_nav_header);
        iv_back             = findViewById(R.id.iv_back);

        tv_nav_header.setTypeface(fonts);
        //Initialize barcode scanner view
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        //set torch listener
        barcodeScannerView.setTorchListener(this);

        //switch flashlight button
        switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);
        finishButton            = (Button) findViewById(R.id.buttn_finish);
        switchFlashlightButton.setTypeface(fonts);
        finishButton.setTypeface(fonts);

        // jika hpnya tidak ada flashlight,
        // tombol flash di-hide
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        } else {
            switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                }
            });
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerActivity.super.onBackPressed();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addTransactions(kodeTransaction, now, totalBayar, "1", "penjualan");
                db.updateNoTransactionDetail("0", kodeTransaction, "1");
                db.addPayment(kodeTransaction, "bayar nanti", totalBayar, "0", "0");
                HashMap<String, String> balancedata = db.getBalance();
                idBalance   = balancedata.get("id");
                balance     = balancedata.get("balance");
                int newBalance = Integer.parseInt(balance) + Integer.parseInt(totalBayar);
                db.updateBalance(idBalance, String.valueOf(newBalance), totalBayar);
                Intent success = new Intent(ScannerActivity.this, ItemsPaymentResultActivity.class);
                success.putExtra("change", "");
                startActivity(success);
            }
        });

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }


   //fungsi untuk cek apakah HP punya flash atau tidak
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashLightOn) {
            barcodeScannerView.setTorchOff();
            isFlashLightOn = false;
        } else {
            barcodeScannerView.setTorchOn();
            isFlashLightOn = true;
        }

    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }
    @Override
    public void onStart() {
        super.onStart();
        jsonlistTransactionsDetail = db.getListTransactionsDetail("0", "1");
        fetchListItems(jsonlistTransactionsDetail);

    }
    public void fetchListItems(String json){
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            int total = 0;
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    total += Integer.parseInt(obj.getString("subTotal"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            totalBayar              = String.valueOf(total);

            Log.d("Scanner", "total bayar " + totalBayar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
