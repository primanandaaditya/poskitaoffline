package com.kitadigi.poskita.activities.report.stokdetail;

public class ReportStokDetailPresenter implements IReportStokDetailActivityContract.IReportStokDetailPresenter, IReportStokDetailActivityContract.IGetReportStokDetailIntractor.IReportStokDetailOnFinishedListener {

    IReportStokDetailActivityContract.IReportStokDetailMainView mainView;
    IReportStokDetailActivityContract.IGetReportStokDetailIntractor iGetReportStokDetailIntractor;


    public ReportStokDetailPresenter(IReportStokDetailActivityContract.IReportStokDetailMainView mainView, IReportStokDetailActivityContract.IGetReportStokDetailIntractor iGetReportStokDetailIntractor) {
        this.mainView = mainView;
        this.iGetReportStokDetailIntractor = iGetReportStokDetailIntractor;
    }

    @Override
    public void onDestroy() {
        mainView=null;
    }

    @Override
    public void onRefreshButtonClick() {
        if(mainView != null){
        }
        iGetReportStokDetailIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {
        iGetReportStokDetailIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportDetailModel reportDetailModel) {
        if(mainView != null){
            mainView.setDataToView(reportDetailModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(mainView != null){
            mainView.onError(t);
        }
    }
}
