package com.kitadigi.poskita.model;

//model ini dibuat untuk interface retrofit
//IAddTransaction.java
//untuk menyimpan penjualan online
//lihat ItemsPaymentResultActivity.java

public class ArrayPosModel {


    String arrayIdProductMaster; // untuk @Field("id_product_master[]") String id_product_master
    String arrayQty;             // untuk @Field("qty[]") String qty
    String arrayPrice;           // untuk @Field("price[]") String price



    public String getArrayIdProductMaster() {
        return arrayIdProductMaster;
    }

    public void setArrayIdProductMaster(String arrayIdProductMaster) {
        this.arrayIdProductMaster = arrayIdProductMaster;
    }

    public String getArrayQty() {
        return arrayQty;
    }

    public void setArrayQty(String arrayQty) {
        this.arrayQty = arrayQty;
    }

    public String getArrayPrice() {
        return arrayPrice;
    }

    public void setArrayPrice(String arrayPrice) {
        this.arrayPrice = arrayPrice;
    }
}
