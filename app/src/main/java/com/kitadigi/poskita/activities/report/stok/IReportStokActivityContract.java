package com.kitadigi.poskita.activities.report.stok;

public interface IReportStokActivityContract {


    public interface IReportStokPresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportStokMainView {
        void requestReport(String enkripIdUser);
        void setDataToView(ReportStokModel reportStokModel);
        void onError(Throwable throwable);

    }




    public interface IGetReportStokIntractor {

        public interface IReportStokOnFinishedListener {
            void onFinished(ReportStokModel reportStokModel);
            void onError(Throwable t);
        }

        void getReportModel(IReportStokOnFinishedListener onFinishedListener);
    }

}
