package com.kitadigi.poskita.sinkron.jual.get_master;

import java.util.List;

public class MasterModel {

    boolean status;
    List<PenjualanMaster> penjualan_master;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PenjualanMaster> getPenjualan_master() {
        return penjualan_master;
    }

    public void setPenjualan_master(List<PenjualanMaster> penjualan_master) {
        this.penjualan_master = penjualan_master;
    }
}
