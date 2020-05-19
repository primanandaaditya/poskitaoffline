package com.kitadigi.poskita.activities.report.revenue;

import java.util.List;

public class ReportRevenueModel {


    List<ReportRevenueData> data = null;

    String message,total_amount;
     Integer total_qty;

    public List<ReportRevenueData> getData() {
        return data;
    }

    public void setData(List<ReportRevenueData> data) {
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
