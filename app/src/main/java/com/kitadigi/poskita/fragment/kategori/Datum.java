package com.kitadigi.poskita.fragment.kategori;

public class Datum {

    private String id;
    private String business_id;
    private String name;
    private String code_category;
    private String additional;
    private String mobile_id;

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

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode_category() {
        return code_category;
    }

    public void setCode_category(String code_category) {
        this.code_category = code_category;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
