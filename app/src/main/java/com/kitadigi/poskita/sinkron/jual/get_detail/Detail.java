package com.kitadigi.poskita.sinkron.jual.get_detail;

public class Detail {


    String id;
    String nomor;
    private String id_transaction_detail;
    private String id_transaction;
    private String mobile_id_transaction;
    String mobile_id_produk;
    String qty;
    String price;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getMobile_id_produk() {
        return mobile_id_produk;
    }

    public void setMobile_id_produk(String mobile_id_produk) {
        this.mobile_id_produk = mobile_id_produk;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
