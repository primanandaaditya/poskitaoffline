package com.kitadigi.poskita.base;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class PosAdapter extends BaseAdapter {

    SweetAlertDialog sweetAlertDialog;
    Context context;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;
    SessionManager sessionManager;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }
    public void applyFontRegularToTextView(TextView textView){
        initFont();
        textView.setTypeface(fonts);
    }

    public void applyFontBoldToTextView(TextView textView){
        initFont();
        textView.setTypeface(fontsBold);
    }

    public void applyFontRegularToEditText(EditText editText){
        initFont();
        editText.setTypeface(fonts);
    }

    public void applyFontBoldToEditText(EditText editText){
        initFont();
        editText.setTypeface(fontsBold);
    }

    public void applyFontRegularToButton(Button button){
        initFont();
        button.setTypeface(fonts);
    }

    public void applyFontBoldToButton(Button button){
        initFont();
        button.setTypeface(fontsBold);
    }

    public void initBaseActivity(){
        sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.CONFIRM_TYPE);
    }

    public void showToast(String pesan){
        new SweetAlertDialog(context,SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText(pesan)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    public int sessionExpired(String message){

        int hasil = 0;
        if (message.equals(context.getResources().getString(R.string.session_expired))){
            hasil =1;
            new SweetAlertDialog(context,SweetAlertDialog.CONFIRM_TYPE)
                    .setTitleText(context.getResources().getString(R.string.silakan_login_lagi))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            sessionManager=new SessionManager(context);
                            sessionManager.logoutUser();

                        }
                    })
                    .show();
        }else{
            hasil = 0;
        }

        return hasil;

    }

}
