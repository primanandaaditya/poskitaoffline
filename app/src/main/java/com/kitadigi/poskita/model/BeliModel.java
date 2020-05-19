package com.kitadigi.poskita.model;

import java.util.Comparator;

public class BeliModel {

    String id;
    String name_product;
    Integer purchase_price;
    Integer qty_available;
    Integer qty;

    public BeliModel(String id, String name_product, Integer purchase_price, Integer qty_available, Integer qty) {
        this.id = id;
        this.name_product = name_product;
        this.purchase_price = purchase_price;
        this.qty_available = qty_available;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public Integer getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(Integer purchase_price) {
        this.purchase_price = purchase_price;
    }

    public Integer getQty_available() {
        return qty_available;
    }

    public void setQty_available(Integer qty_available) {
        this.qty_available = qty_available;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public static Comparator<BeliModel> beliModelComparator = new Comparator<BeliModel>() {
        public int compare(BeliModel s1, BeliModel s2) {
            String dt1 = s1.getId().toUpperCase();
            String dt2 = s2.getId().toUpperCase();

            //ascending order
            return dt1.compareTo(dt2);

            //descending order
            //return StudentName2.compareTo(StudentName1);

        }

    };
}
