package com.kitadigi.poskita.activities.report.revenue;

public class ReportRevenuePresenter implements IReportRevenueActivityContract.IReportRevenuePresenter, IReportRevenueActivityContract.IGetReportRevenueIntractor.IReportRevenueOnFinishedListener {


    private IReportRevenueActivityContract.IReportRevenueMainView mainView;
    private IReportRevenueActivityContract.IGetReportRevenueIntractor iGetReportRevenueIntractor;

    public ReportRevenuePresenter(IReportRevenueActivityContract.IReportRevenueMainView mainView, IReportRevenueActivityContract.IGetReportRevenueIntractor iGetReportRevenueIntractor) {
        this.mainView = mainView;
        this.iGetReportRevenueIntractor = iGetReportRevenueIntractor;
    }

    @Override
    public void onDestroy() {
        mainView=null;
    }

    @Override
    public void onRefreshButtonClick() {
        if(mainView != null){
        }
        iGetReportRevenueIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {

        iGetReportRevenueIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportRevenueModel reportRevenueModel) {
        if(mainView != null){
            mainView.setDataToView(reportRevenueModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(mainView != null){
            mainView.onError(t);
        }
    }
}
