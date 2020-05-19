package com.kitadigi.poskita.activities.report.pembelian;

public interface IReportPembelianActivityContract {


    public interface IReportPembelianPresenter{
        void onDestroy();
        void onRefreshButtonClick();
        void requestDataFromServer();
    }



    public interface IReportPembelianMainView {
        void requestReport(String enkripIdUser, String tanggal);
        void setDataToView(ReportPembelianModel reportPembelianModel);
        void onError(Throwable throwable);

    }




    public interface IGetReportPembelianIntractor {

        public interface ReportPembelianOnFinishedListener {
            void onFinished(ReportPembelianModel reportPembelianModel);
            void onError(Throwable t);

        }



        void getReportModel(ReportPembelianOnFinishedListener reportPembelianOnFinishedListener);
    }

}
