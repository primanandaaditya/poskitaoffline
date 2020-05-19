package com.kitadigi.poskita.fragment.item;

import java.util.List;

public class BarangResult {

    private String message;
    private List<Datum> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}