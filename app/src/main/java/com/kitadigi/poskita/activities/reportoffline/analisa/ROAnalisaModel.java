package com.kitadigi.poskita.activities.reportoffline.analisa;

public class ROAnalisaModel {

    String tanggal;
    Integer total_item_penjualan;
    Integer total_item_pembelian;
    Integer grand_total_penjualan;
    Integer grand_total_pembelian;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getTotal_item_penjualan() {
        return total_item_penjualan;
    }

    public void setTotal_item_penjualan(Integer total_item_penjualan) {
        this.total_item_penjualan = total_item_penjualan;
    }

    public Integer getTotal_item_pembelian() {
        return total_item_pembelian;
    }

    public void setTotal_item_pembelian(Integer total_item_pembelian) {
        this.total_item_pembelian = total_item_pembelian;
    }

    public Integer getGrand_total_penjualan() {
        return grand_total_penjualan;
    }

    public void setGrand_total_penjualan(Integer grand_total_penjualan) {
        this.grand_total_penjualan = grand_total_penjualan;
    }

    public Integer getGrand_total_pembelian() {
        return grand_total_pembelian;
    }

    public void setGrand_total_pembelian(Integer grand_total_pembelian) {
        this.grand_total_pembelian = grand_total_pembelian;
    }
}
