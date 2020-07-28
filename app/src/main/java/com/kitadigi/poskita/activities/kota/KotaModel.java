package com.kitadigi.poskita.activities.kota;

import java.util.List;

public class KotaModel {

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
