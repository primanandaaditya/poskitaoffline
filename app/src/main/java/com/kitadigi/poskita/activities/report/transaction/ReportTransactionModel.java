package com.kitadigi.poskita.activities.report.transaction;

import java.util.List;

public class ReportTransactionModel {

    String current_time;
    List<ReportTransactionData> data = null;
    String message;
    String total_amount;
    String total_qty;

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public List<ReportTransactionData> getData() {
        return data;
    }

    public void setData(List<ReportTransactionData> data) {
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

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }
}
