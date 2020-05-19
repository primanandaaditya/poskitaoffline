package com.kitadigi.poskita.activities.report.stokdetail;

import java.util.List;

public class ReportDetailModel {

    String accumulated_stock,message;
    Integer total_current_stock;
    List<ReportDetailData> data = null;



    public String getAccumulated_stock() {
        return accumulated_stock;
    }

    public void setAccumulated_stock(String accumulated_stock) {
        this.accumulated_stock = accumulated_stock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotal_current_stock() {
        return total_current_stock;
    }

    public void setTotal_current_stock(Integer total_current_stock) {
        this.total_current_stock = total_current_stock;
    }

    public List<ReportDetailData> getData() {
        return data;
    }

    public void setData(List<ReportDetailData> data) {
        this.data = data;
    }
}
