package com.kitadigi.poskita.activities.report.revenue;

public interface IReportRevenueActivityContract {

    public interface IReportRevenuePresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportRevenueMainView {
        void requestReport(String enkripIdUser, String start_date, String end_date);
        void setDataToView(ReportRevenueModel reportRevenueModel);
        void onError(Throwable throwable);

    }




    public interface IGetReportRevenueIntractor {

        public interface IReportRevenueOnFinishedListener {
            void onFinished(ReportRevenueModel reportRevenueModel);
            void onError(Throwable t);

        }



        void getReportModel(IReportRevenueOnFinishedListener onFinishedListener);
    }

}
