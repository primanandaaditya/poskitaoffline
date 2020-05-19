package com.kitadigi.poskita.activities.report.stok;

public class ReportStokPresenter implements IReportStokActivityContract.IReportStokPresenter, IReportStokActivityContract.IGetReportStokIntractor.IReportStokOnFinishedListener {

    private IReportStokActivityContract.IReportStokMainView mainView;
    private IReportStokActivityContract.IGetReportStokIntractor intractor;

    public ReportStokPresenter(IReportStokActivityContract.IReportStokMainView iReportStokMainView, IReportStokActivityContract.IGetReportStokIntractor iGetReportStokIntractor) {
        this.mainView = iReportStokMainView;
        this.intractor = iGetReportStokIntractor;
    }



    @Override
    public void onDestroy() {
        mainView=null;
    }

    @Override
    public void onRefreshButtonClick() {

        if(mainView != null){
        }
        intractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {

        intractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportStokModel reportStokModel) {
        if(mainView != null){
            mainView.setDataToView(reportStokModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(mainView != null){
            mainView.onError(t);
        }
    }
}
