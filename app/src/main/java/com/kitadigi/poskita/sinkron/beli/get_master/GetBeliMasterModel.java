package com.kitadigi.poskita.sinkron.beli.get_master;

import java.util.List;

public class GetBeliMasterModel {

    Boolean status;
    List<PembelianMaster> pembelian_master = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<PembelianMaster> getPembelian_master() {
        return pembelian_master;
    }

    public void setPembelian_master(List<PembelianMaster> pembelian_master) {
        this.pembelian_master = pembelian_master;
    }
}
