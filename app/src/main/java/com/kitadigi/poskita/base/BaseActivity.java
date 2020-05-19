package com.kitadigi.poskita.base;

import android.arch.persistence.room.Room;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.dao.kategori.KategoriDAO;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseActivity extends AppCompatActivity {

    SweetAlertDialog sweetAlertDialog;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;
    SessionManager sessionManager;


    //untuk sqlite room
    Db database;
    KategoriDAO kategoriDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    public void initRoom() {
        database = Room.databaseBuilder(this, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
    }


    public void initFont() {
        //init fonts
        fonts = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    public void applyFontRegularToTextView(TextView textView) {
        initFont();
        textView.setTypeface(fonts);
    }

    public void applyFontBoldToTextView(TextView textView) {
        initFont();
        textView.setTypeface(fontsBold);
    }

    public void applyFontItalicToTextView(TextView textView) {
        initFont();
        textView.setTypeface(fontsItalic);
    }

    public void applyFontItalicToRadioButton(RadioButton radioButton) {
        initFont();
        radioButton.setTypeface(fontsItalic);
    }

    public void applyFontRegularToRadioButton(RadioButton radioButton) {
        initFont();
        radioButton.setTypeface(fonts);
    }

    public void applyFontBoldToRadioButton(RadioButton radioButton) {
        initFont();
        radioButton.setTypeface(fontsBold);
    }

    public void applyFontRegularToEditText(EditText editText) {
        initFont();
        editText.setTypeface(fonts);
    }

    public void applyFontBoldToEditText(EditText editText) {
        initFont();
        editText.setTypeface(fontsBold);
    }

    public void applyFontItalicToEditText(EditText editText) {
        initFont();
        editText.setTypeface(fontsItalic);
    }

    public void applyFontRegularToButton(Button button) {
        initFont();
        button.setTypeface(fonts);
    }

    public void applyFontBoldToButton(Button button) {
        initFont();
        button.setTypeface(fontsBold);
    }

    public void applyFontItalicToButton(Button button) {
        initFont();
        button.setTypeface(fontsItalic);
    }

    public void initBaseActivity() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CONFIRM_TYPE);
    }

    public void showToast(String pesan) {
        new SweetAlertDialog(this, SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText(pesan)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void dismissToast() {
        sweetAlertDialog.dismissWithAnimation();
    }

    public void tutupActivity(View view) {
        this.finish();
    }

    public int sessionExpired(String message) {

        int hasil = 0;
        if (message.equals(getResources().getString(R.string.session_expired))) {
            hasil = 1;
            new SweetAlertDialog(this, SweetAlertDialog.CONFIRM_TYPE)
                    .setTitleText(getResources().getString(R.string.silakan_login_lagi))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            sessionManager = new SessionManager(BaseActivity.this);
                            sessionManager.logoutUser();

                        }
                    })
                    .show();
        } else {
            hasil = 0;
        }

        return hasil;

    }

}
