package com.kitadigi.poskita.sinkron.jual.get_master;

import com.kitadigi.poskita.model.Status;

import java.util.List;

public class MasterModel {

    Status status;
    List<PenjualanMaster> penjualan_master;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PenjualanMaster> getPenjualan_master() {
        return penjualan_master;
    }

    public void setPenjualan_master(List<PenjualanMaster> penjualan_master) {
        this.penjualan_master = penjualan_master;
    }
}
