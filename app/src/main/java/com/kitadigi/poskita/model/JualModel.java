package com.kitadigi.poskita.model;



import java.util.Comparator;

public class JualModel {

    //catatan:pada class ini, rencananya properti id akan diisi oleh kode_id,
    //bukan id produk
    //karena ada mode OFFLINE

    String id;
    String name_product;
    Integer sell_price;
    Integer qty_available;
    Integer qty;

    public JualModel(String id, String name_product, Integer sell_price, Integer qty_available, Integer qty) {
        this.id = id;
        this.name_product = name_product;
        this.sell_price = sell_price;
        this.qty_available = qty_available;
        this.qty = qty;
    }

    public Integer getSell_price() {
        return sell_price;
    }

    public void setSell_price(Integer sell_price) {
        this.sell_price = sell_price;
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

    public static Comparator<JualModel> jualModelComparator = new Comparator<JualModel>() {
        public int compare(JualModel s1, JualModel s2) {
            String dt1 = s1.getId().toUpperCase();
            String dt2 = s2.getId().toUpperCase();

            //ascending order
            return dt1.compareTo(dt2);

            //descending order
            //return StudentName2.compareTo(StudentName1);

        }

    };

}


