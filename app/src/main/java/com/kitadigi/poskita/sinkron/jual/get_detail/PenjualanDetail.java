package com.kitadigi.poskita.sinkron.jual.get_detail;

public class PenjualanDetail {

    private String id_transaction_detail;
    private String id_transaction;
    private String mobile_id_transaction;
    private String mobile_id_produk;
    private Integer qty;
    private Integer price;

    public String getId_transaction_detail() {
        return id_transaction_detail;
    }

    public void setId_transaction_detail(String id_transaction_detail) {
        this.id_transaction_detail = id_transaction_detail;
    }

    public String getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(String id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getMobile_id_transaction() {
        return mobile_id_transaction;
    }

    public void setMobile_id_transaction(String mobile_id_transaction) {
        this.mobile_id_transaction = mobile_id_transaction;
    }

    public String getMobile_id_produk() {
        return mobile_id_produk;
    }

    public void setMobile_id_produk(String mobile_id_produk) {
        this.mobile_id_produk = mobile_id_produk;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
