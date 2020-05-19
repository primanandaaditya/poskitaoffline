package com.kitadigi.poskita.activities.login;

public interface LoginActivityContract {

    public interface Presenter{

        void onDestroy();

        void onLoginButtonClick();

        void requestDataFromServer();

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    public interface LoginView {

        void showProgress();

        void hideProgress();

        void setDataToView(LoginResult loginResult);

        void onResponseFailure(Throwable throwable);



    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetLoginResultIntractor {

        interface OnFinishedListener {
            void onFinished(LoginResult loginResult);
            void onFailure(Throwable t);
        }

        void getResultModel(OnFinishedListener onFinishedListener);
    }
}
