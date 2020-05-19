package com.kitadigi.poskita.activities.report.transactiondetail;

import com.kitadigi.poskita.activities.report.transactiondetail.IReportTransactionDetailContract.Presenter;
import com.kitadigi.poskita.activities.report.transactiondetail.IReportTransactionDetailContract.GetResultIntractor.OnFinishedListener;
import com.kitadigi.poskita.activities.report.transactiondetail.IReportTransactionDetailContract.MainView;
import com.kitadigi.poskita.activities.report.transactiondetail.IReportTransactionDetailContract.GetResultIntractor;

public class ReportTransactionDetailPresenter implements Presenter,OnFinishedListener {

    private MainView mainView;
    private GetResultIntractor getResultIntractor;

    public ReportTransactionDetailPresenter(MainView mainView, GetResultIntractor getResultIntractor) {
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
    public void onFinished(TransactionDetailModel transactionDetailModel) {
        if(mainView != null){
            mainView.setDataToView(transactionDetailModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(mainView != null){
            mainView.onError(t);
        }
    }
}
