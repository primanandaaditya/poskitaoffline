package com.kitadigi.poskita.sinkron.beli.get_detail;

import com.kitadigi.poskita.model.Status;

import java.util.List;

public class GetBeliDetailModel {

    Status status;
    List<PembelianDetail> pembelian_detail = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PembelianDetail> getPembelian_detail() {
        return pembelian_detail;
    }

    public void setPembelian_detail(List<PembelianDetail> pembelian_detail) {
        this.pembelian_detail = pembelian_detail;
    }
}
