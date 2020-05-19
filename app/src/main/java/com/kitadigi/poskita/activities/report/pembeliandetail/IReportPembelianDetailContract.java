package com.kitadigi.poskita.activities.report.pembeliandetail;

import com.kitadigi.poskita.activities.report.transactiondetail.TransactionDetailModel;

public interface IReportPembelianDetailContract {

    public interface IReportPembelianDetailPresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportPembelianDetailMainView {
        void requestReport(String enkripIdUser, String enkripTransaksi);
        void setDataToView(TransactionDetailModel transactionDetailModel);
        void onError(Throwable throwable);

    }




    public interface IReportPembelianDetailGetIntractor {

        public interface IReportPembelianDetailOnFinishedListener {
            void onFinished(TransactionDetailModel transactionDetailModel);
            void onError(Throwable t);

        }

        void getReportModel(IReportPembelianDetailOnFinishedListener onFinishedListener);
    }

}
