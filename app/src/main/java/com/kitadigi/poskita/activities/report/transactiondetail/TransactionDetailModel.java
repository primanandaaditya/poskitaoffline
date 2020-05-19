package com.kitadigi.poskita.activities.report.transactiondetail;

import java.util.List;

public class TransactionDetailModel {

    String current_time,message,total_price,total_quantity;
    List<TransactionDetailData> data = null;

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public List<TransactionDetailData> getData() {
        return data;
    }

    public void setData(List<TransactionDetailData> data) {
        this.data = data;
    }
}
