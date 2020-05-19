package com.kitadigi.poskita.fragment.unit;
import java.util.List;

public class UnitModel {

    private List<UnitData> data = null;
    private String message;


    public List<UnitData> getData() {
        return data;
    }

    public void setData(List<UnitData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
