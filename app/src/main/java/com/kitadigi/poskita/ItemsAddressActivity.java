package com.kitadigi.poskita;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.database.Database;

import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemsAddressActivity extends AppCompatActivity{

    private static final String TAG = ItemsAddressActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_remark_address_user, tv_remark_address, tv_remark_address_phone;
    EditText et_address_user, et_address, et_address_phone;
    ImageView iv_back;
    Button btn_save;

    /* typeface fonts */
    Typeface fonts, fontsItalic;

    private SweetAlertDialog sweetAlertDialog;

    /* Sqlite database */
    private Database db;

    TimeZone tz;

    Animation fadeIn, fadeOut;

    //init variable
    String idAddress, recipien, address, phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_data);
        context = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /* init sqlite db */
        db                          = new Database(context);

        /* Calendar */
        tz                          = TimeZone.getTimeZone("GMT+0700");

        /* init fonts */
        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_remark_address_user      = findViewById(R.id.tv_remark_address_user);
        tv_remark_address           = findViewById(R.id.tv_remark_address);
        tv_remark_address_phone     = findViewById(R.id.tv_remark_address_phone);

        /* init edittext */
        et_address_user             = findViewById(R.id.et_address_user);
        et_address                  = findViewById(R.id.et_address);
        et_address_phone            = findViewById(R.id.et_address_phone);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /* init button */
        btn_save                    = findViewById(R.id.btn_save);

        /* animation */
        fadeIn                      = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut                     = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        /* set fonts */
        tv_nav_header.setTypeface(fonts);
        tv_remark_address_user.setTypeface(fontsItalic);
        tv_remark_address.setTypeface(fontsItalic);
        tv_remark_address_phone.setTypeface(fontsItalic);
        btn_save.setTypeface(fonts);
        et_address_user.setTypeface(fonts);
        et_address.setTypeface(fonts);
        et_address_phone.setTypeface(fonts);

        tv_remark_address_user.setText("(*) diisi nama penerima");
        tv_remark_address.setText("(*) diisi alamat penerima");
        tv_remark_address_phone.setText("(*) diisi no hp penerima");

        Bundle extras                   = getIntent().getExtras();
        if(extras != null){
            Log.d(TAG, "not null");
            idAddress                       = extras.getString("idAddress");
            recipien                        = extras.getString("recipien");
            address                         = extras.getString("address");
            phone                           = extras.getString("phone");

            et_address_user.setText(recipien);
            et_address.setText(address);
            et_address_phone.setText(phone);
            tv_nav_header.setText("Edit Alamat Pengiriman");
            btn_save.setText("Ubah");
        }else{
            Log.d(TAG, "null");
        }

        et_address_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_address_user, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_address, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_address_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_address_phone, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsAddressActivity.super.onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_text = btn_save.getText().toString();
                if(btn_text.toLowerCase().equals("save")){
                    setLoadingDialog("Loading");
                    showAlertDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideAlertDialog();
                            recipien    = et_address_user.getText().toString();
                            address     = et_address.getText().toString();
                            phone       = et_address_phone.getText().toString();
                            db.addAdress(recipien, address, phone);
                            setSuccessDialog("SIMPAN DATA BERHASIL");
                            showAlertDialog();
                        }
                    }, 1500);
                }else if(btn_text.toLowerCase().equals("ubah")){
                    setLoadingDialog("Loading");
                    showAlertDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideAlertDialog();
                            recipien    = et_address_user.getText().toString();
                            address     = et_address.getText().toString();
                            phone       = et_address_phone.getText().toString();
                            db.updateAddress(idAddress, recipien, address, phone);
                            setSuccessDialog("UBAH DATA BERHASIL");
                            showAlertDialog();
                        }
                    }, 1500);
                }

                //saveInvoice(username, category.getId(), imageFilePath, "bowlee");
            }
        });
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
        sweetAlertDialog = new SweetAlertDialog(ItemsAddressActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(String title){
        sweetAlertDialog = new SweetAlertDialog(ItemsAddressActivity.this, SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText(title)
                .setConfirmText("TUTUP")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        ItemsAddressActivity.super.onBackPressed();
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
