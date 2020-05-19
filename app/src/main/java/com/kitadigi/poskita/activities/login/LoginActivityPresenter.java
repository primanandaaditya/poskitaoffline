package com.kitadigi.poskita.activities.login;

import com.kitadigi.poskita.activities.login.LoginActivityContract.Presenter;
import com.kitadigi.poskita.activities.login.LoginActivityContract.GetLoginResultIntractor;
import com.kitadigi.poskita.activities.login.LoginActivityContract.GetLoginResultIntractor.OnFinishedListener;
import com.kitadigi.poskita.activities.login.LoginActivityContract.LoginView;

public class LoginActivityPresenter implements Presenter,OnFinishedListener {

    private LoginView loginView;
    private GetLoginResultIntractor getLoginResultIntractor;

    public LoginActivityPresenter(LoginView loginView, GetLoginResultIntractor getLoginResultIntractor) {
        this.loginView = loginView;
        this.getLoginResultIntractor = getLoginResultIntractor;
    }

    @Override
    public void onDestroy() {
        loginView=null;
    }

    @Override
    public void onLoginButtonClick() {
        if(loginView != null){
            loginView.showProgress();
        }
        getLoginResultIntractor.getResultModel(this);
    }

    @Override
    public void requestDataFromServer() {
        loginView.showProgress();
        getLoginResultIntractor.getResultModel(this);
    }

    @Override
    public void onFinished(LoginResult loginResult) {
        if(loginView != null){
            loginView.setDataToView(loginResult);
            loginView.hideProgress();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        if(loginView != null){
            loginView.onResponseFailure(t);
            loginView.hideProgress();
        }
    }
}
