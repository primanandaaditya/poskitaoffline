package com.kitadigi.poskita.sinkron.beli.get_detail;

import java.util.List;

public class GetBeliDetailModel {

    Boolean status;
    List<PembelianDetail> pembelian_detail = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<PembelianDetail> getPembelian_detail() {
        return pembelian_detail;
    }

    public void setPembelian_detail(List<PembelianDetail> pembelian_detail) {
        this.pembelian_detail = pembelian_detail;
    }
}
