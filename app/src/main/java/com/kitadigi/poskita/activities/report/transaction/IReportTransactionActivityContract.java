package com.kitadigi.poskita.activities.report.transaction;

public interface IReportTransactionActivityContract {


    public interface Presenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface MainView {
        void requestReport(String enkripIdUser, String tanggal);
        void setDataToView(ReportTransactionModel reportTransactionModel);
        void onError(Throwable throwable);

    }




    public interface GetResultIntractor {

        public interface OnFinishedListener {
            void onFinished(ReportTransactionModel reportTransactionModel);
            void onError(Throwable t);

        }

        void getReportModel(OnFinishedListener onFinishedListener);
    }


}
