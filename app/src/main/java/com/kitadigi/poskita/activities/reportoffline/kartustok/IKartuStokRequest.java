package com.kitadigi.poskita.activities.reportoffline.kartustok;

public interface IKartuStokRequest {

    void getReport(String tanggalDari, String tanggalSampai);
    void hitungKeluar();
    void hitungMasuk();
    void hitungSisa();

}
