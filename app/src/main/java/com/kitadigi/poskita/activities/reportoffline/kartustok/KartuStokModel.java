package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.Intent;

public class KartuStokModel {

    String tanggal;
    Integer masuk;
    Integer keluar;
    Integer sisa;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getMasuk() {
        return masuk;
    }

    public void setMasuk(Integer masuk) {
        this.masuk = masuk;
    }

    public Integer getKeluar() {
        return keluar;
    }

    public void setKeluar(Integer keluar) {
        this.keluar = keluar;
    }

    public Integer getSisa() {
        return sisa;
    }

    public void setSisa(Integer sisa) {
        this.sisa = sisa;
    }
}
