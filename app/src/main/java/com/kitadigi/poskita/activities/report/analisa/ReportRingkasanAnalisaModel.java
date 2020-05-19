package com.kitadigi.poskita.activities.report.analisa;

import java.util.List;

public class ReportRingkasanAnalisaModel {


    String message,report_date,total_keuntungan,total_pembelian,
            total_purchase_order,total_transaksi,total_penjualan;
    List<ProdukTerlakuModel> produk_terlaku=null;
    Integer produk_terjual;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getTotal_keuntungan() {
        return total_keuntungan;
    }

    public void setTotal_keuntungan(String total_keuntungan) {
        this.total_keuntungan = total_keuntungan;
    }

    public String getTotal_pembelian() {
        return total_pembelian;
    }

    public void setTotal_pembelian(String total_pembelian) {
        this.total_pembelian = total_pembelian;
    }

    public String getTotal_purchase_order() {
        return total_purchase_order;
    }

    public void setTotal_purchase_order(String total_purchase_order) {
        this.total_purchase_order = total_purchase_order;
    }

    public String getTotal_transaksi() {
        return total_transaksi;
    }

    public void setTotal_transaksi(String total_transaksi) {
        this.total_transaksi = total_transaksi;
    }

    public String getTotal_penjualan() {
        return total_penjualan;
    }

    public void setTotal_penjualan(String total_penjualan) {
        this.total_penjualan = total_penjualan;
    }

    public List<ProdukTerlakuModel> getProduk_terlaku() {
        return produk_terlaku;
    }

    public void setProduk_terlaku(List<ProdukTerlakuModel> produk_terlaku) {
        this.produk_terlaku = produk_terlaku;
    }

    public Integer getProduk_terjual() {
        return produk_terjual;
    }

    public void setProduk_terjual(Integer produk_terjual) {
        this.produk_terjual = produk_terjual;
    }
}
