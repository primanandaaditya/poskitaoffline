package com.kitadigi.poskita.activities.report.analisa;

public class ReportAnalisaPresenter implements IReportAnalisaActivityContract.IReportAnalisaPresenter, IReportAnalisaActivityContract.IGetReportAnalisaIntractor.IReportAnalisaOnFinishedListener {

    private IReportAnalisaActivityContract.IReportAnalisaMainView iReportAnalisaMainView;
    private IReportAnalisaActivityContract.IGetReportAnalisaIntractor iGetReportAnalisaIntractor;

    public ReportAnalisaPresenter(IReportAnalisaActivityContract.IReportAnalisaMainView iReportAnalisaMainView, IReportAnalisaActivityContract.IGetReportAnalisaIntractor iGetReportAnalisaIntractor) {
        this.iReportAnalisaMainView = iReportAnalisaMainView;
        this.iGetReportAnalisaIntractor = iGetReportAnalisaIntractor;
    }



    @Override
    public void onDestroy() {
        iReportAnalisaMainView=null;
    }

    @Override
    public void onRefreshButtonClick() {
        if(iReportAnalisaMainView != null){
        }
        iGetReportAnalisaIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {
        iGetReportAnalisaIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel) {
        if(iReportAnalisaMainView != null){
            iReportAnalisaMainView.setDataToView(reportRingkasanAnalisaModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(iReportAnalisaMainView != null){
            iReportAnalisaMainView.onError(t);
        }
    }
}
