package com.kitadigi.poskita.activities.pos.addtransaction;

public class AddTransactionModel {

    String id_product_master;
    Integer qty;
    Integer price;
    String contact_id;
    int total_pay;
    int total_price;

    public AddTransactionModel(String id_product_master, Integer qty, Integer price, String contact_id, int total_pay, int total_price) {
        this.id_product_master = id_product_master;
        this.qty = qty;
        this.price = price;
        this.contact_id = contact_id;
        this.total_pay = total_pay;
        this.total_price = total_price;
    }

    public String getId_product_master() {
        return id_product_master;
    }

    public void setId_product_master(String id_product_master) {
        this.id_product_master = id_product_master;
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

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public int getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(int total_pay) {
        this.total_pay = total_pay;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
