package com.kitadigi.poskita.sinkron.jual.get_detail;

import java.util.List;

public class GetDetailModel {

    boolean status;
    List<PenjualanDetail> penjualan_detail = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PenjualanDetail> getPenjualan_detail() {
        return penjualan_detail;
    }

    public void setPenjualan_detail(List<PenjualanDetail> penjualan_detail) {
        this.penjualan_detail = penjualan_detail;
    }
}
