package com.kitadigi.poskita.activities.report.stokdetail;

public interface IReportStokDetailActivityContract {


    public interface IReportStokDetailPresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportStokDetailMainView {
        void requestReport(String enkripIdUser, String enkripIdTransaksi, String start_date, String end_date);
        void setDataToView(ReportDetailModel reportDetailModel);
        void onError(Throwable throwable);

    }




    public interface IGetReportStokDetailIntractor {

        public interface IReportStokDetailOnFinishedListener {
            void onFinished(ReportDetailModel reportDetailModel);
            void onError(Throwable t);

        }



        void getReportModel(IReportStokDetailActivityContract.IGetReportStokDetailIntractor.IReportStokDetailOnFinishedListener onFinishedListener);
    }

}
