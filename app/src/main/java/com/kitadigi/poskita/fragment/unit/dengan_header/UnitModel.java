package com.kitadigi.poskita.fragment.unit.dengan_header;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.model.Status;

import java.util.List;

public class UnitModel {

    private List<UnitData> data = null;
    private Status status;


    public List<UnitData> getData() {
        return data;
    }

    public void setData(List<UnitData> data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
