package com.kitadigi.poskita.sinkron.beli.get_master;

import com.kitadigi.poskita.model.Status;

import java.util.List;

public class GetBeliMasterModel {

    Status status;
    List<PembelianMaster> pembelian_master = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PembelianMaster> getPembelian_master() {
        return pembelian_master;
    }

    public void setPembelian_master(List<PembelianMaster> pembelian_master) {
        this.pembelian_master = pembelian_master;
    }
}
