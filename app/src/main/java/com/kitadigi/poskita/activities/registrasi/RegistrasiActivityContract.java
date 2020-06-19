package com.kitadigi.poskita.activities.registrasi;
import com.kitadigi.poskita.activities.login.LoginResult;
import com.kitadigi.poskita.base.BaseResponse;

public interface RegistrasiActivityContract {

    public interface Presenter{
        void onDestroy();
        void onRegistrasiButtonClick();
        void requestDataFromServer();
    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    public interface RegistrasiView {
        void doRegistrasi();
        void showProgress();
        void hideProgress();
        void setDataToView(RegistrasiRespon registrasiRespon);
        void onResponseFailure(Throwable throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetRegistrasiResultIntractor {

        interface OnFinishedListener {
            void onFinished(RegistrasiRespon registrasiRespon);
            void onFailure(Throwable t);
        }

        void getResultModel(OnFinishedListener onFinishedListener);
    }
}
