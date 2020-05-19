package com.kitadigi.poskita.fragment.kategori;
import java.util.List;

public class KategoriModel {

    private List<Datum> data = null;
    private String message;


    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}