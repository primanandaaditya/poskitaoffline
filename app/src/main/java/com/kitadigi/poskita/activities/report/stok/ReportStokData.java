package com.kitadigi.poskita.activities.report.stok;

public class ReportStokData {

    String id_product,code_product,name_product,brands_name,name_category,image,units_name,current_price,additional;
    Integer qty_available,qty_sold;

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getCode_product() {
        return code_product;
    }

    public void setCode_product(String code_product) {
        this.code_product = code_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getBrands_name() {
        return brands_name;
    }

    public void setBrands_name(String brands_name) {
        this.brands_name = brands_name;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnits_name() {
        return units_name;
    }

    public void setUnits_name(String units_name) {
        this.units_name = units_name;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Integer getQty_available() {
        return qty_available;
    }

    public void setQty_available(Integer qty_available) {
        this.qty_available = qty_available;
    }

    public Integer getQty_sold() {
        return qty_sold;
    }

    public void setQty_sold(Integer qty_sold) {
        this.qty_sold = qty_sold;
    }
}
