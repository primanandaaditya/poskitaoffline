package com.kitadigi.poskita.dao.stok;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "stok")

public class Stok {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    private String kode_id;
    private String category_id;
    private String sub_category_id;
    private String units_id;
    private String brands_id;
    private String types;
    private String code_product;
    private String name_product;
    private String brands_name;
    private String name_category;
    private String name_sub_category;
    private String image;
    private Integer qty_available;
    private String units_name;
    private Float profits_precent;
    private String purchase_price;
    private String sell_price;
    private String additional;

    @NonNull
    public Long getId() {
        return id;
    }

    public String getKode_id() {
        return kode_id;
    }

    public void setKode_id(String kode_id) {
        this.kode_id = kode_id;
    }

    public void setId(@NonNull Long id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQty_available() {
        return qty_available;
    }

    public void setQty_available(Integer qty_available) {
        this.qty_available = qty_available;
    }

    public String getUnits_name() {
        return units_name;
    }

    public void setUnits_name(String units_name) {
        this.units_name = units_name;
    }

    public Float getProfits_precent() {
        return profits_precent;
    }

    public void setProfits_precent(Float profits_precent) {
        this.profits_precent = profits_precent;
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

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
