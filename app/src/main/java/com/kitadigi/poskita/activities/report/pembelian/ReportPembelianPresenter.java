package com.kitadigi.poskita.activities.report.pembelian;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IGetReportPembelianIntractor;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IGetReportPembelianIntractor.ReportPembelianOnFinishedListener;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IReportPembelianMainView;
import com.kitadigi.poskita.activities.report.pembelian.IReportPembelianActivityContract.IReportPembelianPresenter;

public class ReportPembelianPresenter implements IReportPembelianPresenter,ReportPembelianOnFinishedListener {


    private IReportPembelianMainView iReportPembelianMainView;
    private IGetReportPembelianIntractor iGetReportPembelianIntractor;

    public ReportPembelianPresenter(IReportPembelianMainView iReportPembelianMainView, IGetReportPembelianIntractor iGetReportPembelianIntractor) {
        this.iReportPembelianMainView = iReportPembelianMainView;
        this.iGetReportPembelianIntractor = iGetReportPembelianIntractor;
    }

    @Override
    public void onDestroy() {
        iReportPembelianMainView = null;
    }

    @Override
    public void onRefreshButtonClick() {
        if(iReportPembelianMainView != null){
        }
        iGetReportPembelianIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {
        iGetReportPembelianIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(ReportPembelianModel reportPembelianModel) {
        if(iReportPembelianMainView != null){
            iReportPembelianMainView.setDataToView(reportPembelianModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(iReportPembelianMainView != null){
            iReportPembelianMainView.onError(t);
        }
    }
}
