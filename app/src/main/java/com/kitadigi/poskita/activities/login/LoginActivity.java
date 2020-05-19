package com.kitadigi.poskita.activities.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.login.LoginActivityContract.LoginView;
import com.kitadigi.poskita.activities.login.LoginActivityContract.Presenter;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends BaseActivity implements LoginView {


    SessionManager sessionManager;

    SweetAlertDialog progressDialog;
    Presenter presenter;

    SimpleMD5 simpleMD5;
    EditText etUserName,etPassword;
    Button btnLogin;

    Context context;

    /* typeface fonts */
    Typeface fonts, fontsItalic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //siapkan enkripsi
        simpleMD5=new SimpleMD5();

        context=this;

        if (new InternetChecker().haveNetwork(context)){

        } else {
            Toast.makeText(LoginActivity.this, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        sessionManager=new SessionManager(context);

        progressDialog=new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText(getString(R.string.now_loading));

        etUserName=(EditText)findViewById(R.id.etUserName);
        etPassword=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);


        /* init fonts */
        this.applyFontBoldToButton(btnLogin);
        this.applyFontRegularToEditText(etUserName);
        this.applyFontRegularToEditText(etPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUserName.getText().toString().matches("") || etPassword.getText().toString().matches("")){
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.validasi_login),Toast.LENGTH_LONG).show();
                }else{

                    presenter=new LoginActivityPresenter(LoginActivity.this,new LoginActivityImpl(etUserName.getText().toString(),etPassword.getText().toString()));
                    presenter.onLoginButtonClick();
                }

            }
        });

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setDataToView(LoginResult loginResult) {
       if (loginResult.getMessage().equals(getString(R.string.login_true))){

           //simpan full_name dan email di session
           sessionManager.createLoginSession(loginResult.getData_user().full_name, loginResult.getData_user().email);
           sessionManager.createIdUsers(loginResult.data_user.id_users);

           //buat bussiness id
           sessionManager.createBussinessId(loginResult.getData_user().getBusiness_id());

           //pindah ke Main Activity setelah login sukses
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);
           finish();
       }else{
           Toast.makeText(LoginActivity.this, loginResult.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(LoginActivity.this, throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
