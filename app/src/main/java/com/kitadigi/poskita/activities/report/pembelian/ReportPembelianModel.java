package com.kitadigi.poskita.activities.report.pembelian;

import java.util.List;

public class ReportPembelianModel {

    List<ReportPembelianData> data = null;
    String message;
    String total_amount;
    Integer total_qty;

    public List<ReportPembelianData> getData() {
        return data;
    }

    public void setData(List<ReportPembelianData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(Integer total_qty) {
        this.total_qty = total_qty;
    }
}
