package com.kitadigi.poskita.fragment.item;

public class Datum {


    String id,category_id,sub_category_id,units_id,brands_id,types,code_product,name_product,brands_name,name_category;
    String name_sub_category,units_name,image,purchase_price,sell_price,qty_stock,qty_minimum,additional;

    String mobile_id;

    public Datum(){

    }

    public Datum(String id, String category_id, String sub_category_id, String units_id, String brands_id, String types, String code_product, String name_product, String brands_name, String name_category, String name_sub_category, String units_name, String image, String purchase_price, String sell_price, String qty_stock, String qty_minimum, String additional) {
        this.id = id;
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
        this.units_id = units_id;
        this.brands_id = brands_id;
        this.types = types;
        this.code_product = code_product;
        this.name_product = name_product;
        this.brands_name = brands_name;
        this.name_category = name_category;
        this.name_sub_category = name_sub_category;
        this.units_name = units_name;
        this.image = image;
        this.purchase_price = purchase_price;
        this.sell_price = sell_price;
        this.qty_stock = qty_stock;
        this.qty_minimum = qty_minimum;
        this.additional = additional;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getUnits_id() {
        return units_id;
    }

    public void setUnits_id(String units_id) {
        this.units_id = units_id;
    }

    public String getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(String brands_id) {
        this.brands_id = brands_id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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

    public String getName_sub_category() {
        return name_sub_category;
    }

    public void setName_sub_category(String name_sub_category) {
        this.name_sub_category = name_sub_category;
    }

    public String getQty_stock() {
        return qty_stock;
    }

    public void setQty_stock(String qty_stock) {
        this.qty_stock = qty_stock;
    }

    public String getQty_minimum() {
        return qty_minimum;
    }

    public void setQty_minimum(String qty_minimum) {
        this.qty_minimum = qty_minimum;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getUnits_name() {
        return units_name;
    }

    public void setUnits_name(String units_name) {
        this.units_name = units_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }
}
