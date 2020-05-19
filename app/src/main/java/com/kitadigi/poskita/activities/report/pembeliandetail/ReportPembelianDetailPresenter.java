package com.kitadigi.poskita.activities.report.pembeliandetail;


import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailPresenter;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailGetIntractor;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailGetIntractor.IReportPembelianDetailOnFinishedListener;
import com.kitadigi.poskita.activities.report.pembeliandetail.IReportPembelianDetailContract.IReportPembelianDetailMainView;
import com.kitadigi.poskita.activities.report.transactiondetail.TransactionDetailModel;


public class ReportPembelianDetailPresenter implements IReportPembelianDetailPresenter,IReportPembelianDetailOnFinishedListener  {

    IReportPembelianDetailMainView iReportPembelianDetailMainView;
    IReportPembelianDetailGetIntractor iReportPembelianDetailGetIntractor;

    public ReportPembelianDetailPresenter(IReportPembelianDetailMainView iReportPembelianDetailMainView, IReportPembelianDetailGetIntractor iReportPembelianDetailGetIntractor) {
        this.iReportPembelianDetailMainView = iReportPembelianDetailMainView;
        this.iReportPembelianDetailGetIntractor = iReportPembelianDetailGetIntractor;
    }

    @Override
    public void onDestroy() {
        iReportPembelianDetailMainView=null;
    }

    @Override
    public void onRefreshButtonClick() {

        if(iReportPembelianDetailMainView != null){
        }
        iReportPembelianDetailGetIntractor.getReportModel(this);
    }

    @Override
    public void requestDataFromServer() {
        iReportPembelianDetailGetIntractor.getReportModel(this);
    }

    @Override
    public void onFinished(TransactionDetailModel transactionDetailModel) {
        if(iReportPembelianDetailMainView != null){
            iReportPembelianDetailMainView.setDataToView(transactionDetailModel);
        }
    }

    @Override
    public void onError(Throwable t) {
        if(iReportPembelianDetailMainView != null){
            iReportPembelianDetailMainView.onError(t);
        }
    }
}
