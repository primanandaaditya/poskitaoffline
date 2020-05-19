package com.kitadigi.poskita.fragment.pos;
import java.util.List;

public class StokModel {

    private List<StokDatum> data = null;
    private String message;

    public List<StokDatum> getData() {
        return data;
    }

    public void setData(List<StokDatum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
