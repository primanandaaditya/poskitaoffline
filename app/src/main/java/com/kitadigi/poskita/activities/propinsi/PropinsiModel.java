package com.kitadigi.poskita.activities.propinsi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropinsiModel {

    private List<Datum> data = null;
    private Status status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
