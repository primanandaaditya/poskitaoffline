package com.kitadigi.poskita.fragment.brand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandModel {

    private List<BrandData> data = null;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<BrandData> getData() {
        return data;
    }

    public void setData(List<BrandData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
