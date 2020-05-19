package com.kitadigi.poskita.activities.report.analisa;

public interface IReportAnalisaActivityContract {

    public interface IReportAnalisaPresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportAnalisaMainView {
        void requestReport(String enkripIdUser, String tanggal);
        void setDataToView(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel);
        void onError(Throwable throwable);

    }




    public interface IGetReportAnalisaIntractor {

        public interface IReportAnalisaOnFinishedListener {
            void onFinished(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel);
            void onError(Throwable t);

        }



        void getReportModel(IReportAnalisaOnFinishedListener onFinishedListener);
    }

}
