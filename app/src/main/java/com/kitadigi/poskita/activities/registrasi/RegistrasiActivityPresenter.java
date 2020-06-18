package com.kitadigi.poskita.activities.registrasi;

import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.Presenter;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.GetRegistrasiResultIntractor;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.GetRegistrasiResultIntractor.OnFinishedListener;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.RegistrasiView;
import com.kitadigi.poskita.base.BaseResponse;

public class RegistrasiActivityPresenter implements Presenter,OnFinishedListener {

    private RegistrasiView registrasiView;
    private GetRegistrasiResultIntractor getRegistrasiResultIntractor;

    public RegistrasiActivityPresenter(RegistrasiView registrasiView, GetRegistrasiResultIntractor getRegistrasiResultIntractor) {
        this.registrasiView = registrasiView;
        this.getRegistrasiResultIntractor = getRegistrasiResultIntractor;
    }

    @Override
    public void onDestroy() {
        registrasiView=null;
    }

    @Override
    public void onLoginButtonClick() {
        if(registrasiView != null){
            registrasiView.showProgress();
        }
        getRegistrasiResultIntractor.getResultModel(this);
    }

    @Override
    public void requestDataFromServer() {
        registrasiView.showProgress();
        getRegistrasiResultIntractor.getResultModel(this);
    }

    @Override
    public void onFinished(BaseResponse baseResponse) {
        if(registrasiView != null){
            registrasiView.setDataToView(baseResponse);
            registrasiView.hideProgress();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        if(registrasiView != null){
            registrasiView.onResponseFailure(t);
            registrasiView.hideProgress();
        }
    }
}
