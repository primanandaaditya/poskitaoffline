package com.kitadigi.poskita.activities.reportoffline.terlaris;

import java.util.Comparator;

public class ROTerlarisModel {

    Long id;
    private String kode_id;
    private String image;
    private String id_users;
    private String name_product;
    private String types;
    private String brand_name,category_name,unit_name;
    private String additional;
    private Integer brand_id;
    private Integer category_id;
    private Integer unit_id;
    private Integer purchase_price;
    private Integer sell_price;
    private String code_product;
    private Integer qty_stock;
    private Integer qty_minimum;

    private Float jumlah_terjual;
    private Float persentase;
    private Float bintang;

    public Float getBintang() {
        return bintang;
    }

    public void setBintang(Float bintang) {
        this.bintang = bintang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKode_id() {
        return kode_id;
    }

    public void setKode_id(String kode_id) {
        this.kode_id = kode_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Integer getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(Integer purchase_price) {
        this.purchase_price = purchase_price;
    }

    public Integer getSell_price() {
        return sell_price;
    }

    public void setSell_price(Integer sell_price) {
        this.sell_price = sell_price;
    }

    public String getCode_product() {
        return code_product;
    }

    public void setCode_product(String code_product) {
        this.code_product = code_product;
    }

    public Integer getQty_stock() {
        return qty_stock;
    }

    public void setQty_stock(Integer qty_stock) {
        this.qty_stock = qty_stock;
    }

    public Integer getQty_minimum() {
        return qty_minimum;
    }

    public void setQty_minimum(Integer qty_minimum) {
        this.qty_minimum = qty_minimum;
    }

    public Float getJumlah_terjual() {
        return jumlah_terjual;
    }

    public void setJumlah_terjual(Float jumlah_terjual) {
        this.jumlah_terjual = jumlah_terjual;
    }

    public Float getPersentase() {
        return persentase;
    }

    public void setPersentase(Float persentase) {
        this.persentase = persentase;
    }


    public static Comparator<ROTerlarisModel> modelComparator = new Comparator<ROTerlarisModel>() {

        public int compare(ROTerlarisModel s1, ROTerlarisModel s2) {

            Float dt1 = s1.getJumlah_terjual();
            Float dt2 = s2.getJumlah_terjual();

            //order descending
            return dt2.compareTo(dt1);

        }

    };
}
