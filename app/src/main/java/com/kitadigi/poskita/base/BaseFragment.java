package com.kitadigi.poskita.base;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseFragment extends Fragment {

    SweetAlertDialog sweetAlertDialog;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;
    SessionManager sessionManager;




    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    public void applyFontRegularToTextView(TextView textView){
        initFont();
        textView.setTypeface(fonts);
    }

    public void applyFontItalicToTextView(TextView textView){
        initFont();
        textView.setTypeface(fontsItalic);
    }


    public void applyFontBoldToTextView(TextView textView){
        initFont();
        textView.setTypeface(fontsBold);
    }

    public void applyFontRegularToEditText(EditText editText){
        initFont();
        editText.setTypeface(fonts);
    }

    public void applyFontItalicToEditText(EditText editText){
        initFont();
        editText.setTypeface(fontsItalic);
    }

    public void applyFontBoldToEditText(EditText editText){
        initFont();
        editText.setTypeface(fontsBold);
    }

    public void applyFontRegularToButton(Button button){
        initFont();
        button.setTypeface(fonts);
    }

    public void applyFontItalicToButton(Button button){
        initFont();
        button.setTypeface(fontsItalic);
    }

    public void applyFontBoldToButton(Button button){
        initFont();
        button.setTypeface(fontsBold);
    }

    public void showToast(String pesan){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText(pesan)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void dismissToast(){
        sweetAlertDialog.dismissWithAnimation();
    }

    public int sessionExpired(String message){

        //fungsi ini berarti
        //jika result = 0, berarti sesi belum kadaluwarsa
        //jika result = 1, berarti sesi sudah kadaluwarsa, dan otomatis balik ke hal login

        int hasil =0;

        if (message.equals(getActivity().getResources().getString(R.string.session_expired))){
            //sesi sudah kadaluwarsa
            //result = 1
            hasil=1;
            new SweetAlertDialog(getActivity(),SweetAlertDialog.CONFIRM_TYPE)
                    .setTitleText(getResources().getString(R.string.silakan_login_lagi))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            sessionManager=new SessionManager(getActivity());
                            sessionManager.logoutUser();

                        }
                    })
                    .show();
        }else{
            hasil=0;
        }

        return hasil;
    }

}
