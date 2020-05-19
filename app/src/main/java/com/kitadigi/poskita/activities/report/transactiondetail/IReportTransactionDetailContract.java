package com.kitadigi.poskita.activities.report.transactiondetail;

public interface IReportTransactionDetailContract {


    public interface Presenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface MainView {
        void requestReport(String enkripIdUser, String enkripTransaksi);
        void setDataToView(TransactionDetailModel transactionDetailModel);
        void onError(Throwable throwable);

    }




    public interface GetResultIntractor {

        public interface OnFinishedListener {
            void onFinished(TransactionDetailModel transactionDetailModel);
            void onError(Throwable t);

        }

        void getReportModel(IReportTransactionDetailContract.GetResultIntractor.OnFinishedListener onFinishedListener);
    }
}
