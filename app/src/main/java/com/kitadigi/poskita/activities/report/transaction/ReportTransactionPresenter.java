package com.kitadigi.poskita.activities.report.transaction;

import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.MainView;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.Presenter;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.GetResultIntractor;
import com.kitadigi.poskita.activities.report.transaction.IReportTransactionActivityContract.GetResultIntractor.OnFinishedListener;


public class ReportTransactionPresenter implements Presenter, OnFinishedListener {

    private MainView mainView;
    private GetResultIntractor getResultIntractor;

    public ReportTransactionPresenter(MainView mainView, GetResultIntractor getResultIntractor) {
        this.mainView = mainView;
        this.getResultIntractor = getResultIntractor;
    }

    @Override
    public void onDestroy() {
        mainView=null;
    }

    @Override
    public void onRefreshButtonClick() {

        if(mainView != null){
        }
        getResultIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {

        getResultIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportTransactionModel reportTransactionModel) {
        if(mainView != null){
            mainView.setDataToView(reportTransactionModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(mainView != null){
            mainView.onError(t);
        }
    }
}
