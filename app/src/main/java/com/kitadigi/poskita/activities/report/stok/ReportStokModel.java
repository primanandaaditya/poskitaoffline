package com.kitadigi.poskita.activities.report.stok;

import java.util.List;

public class ReportStokModel {

    List<ReportStokData> data = null;
    String message, total_price;
    Integer total_sold, total_stock;

    public List<ReportStokData> getData() {
        return data;
    }

    public void setData(List<ReportStokData> data) {
        this.data = data;
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

    public Integer getTotal_sold() {
        return total_sold;
    }

    public void setTotal_sold(Integer total_sold) {
        this.total_sold = total_sold;
    }

    public Integer getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(Integer total_stock) {
        this.total_stock = total_stock;
    }
}


