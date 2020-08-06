package com.kitadigi.poskita.sinkron.jual.get_detail;

import com.kitadigi.poskita.model.Status;

import java.util.List;

public class GetDetailModel {

    Status status;
    List<PenjualanDetail> penjualan_detail = null;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PenjualanDetail> getPenjualan_detail() {
        return penjualan_detail;
    }

    public void setPenjualan_detail(List<PenjualanDetail> penjualan_detail) {
        this.penjualan_detail = penjualan_detail;
    }
}
