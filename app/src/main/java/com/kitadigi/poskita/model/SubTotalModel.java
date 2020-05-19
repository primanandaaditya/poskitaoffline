package com.kitadigi.poskita.model;

public class SubTotalModel {
    int sum_qty;
    int sum_total;

    public SubTotalModel() {
    }

    public SubTotalModel(int sum_qty, int sum_total) {
        this.sum_qty = sum_qty;
        this.sum_total = sum_total;
    }

    public int getSum_qty() {
        return sum_qty;
    }

    public void setSum_qty(int sum_qty) {
        this.sum_qty = sum_qty;
    }

    public int getSum_total() {
        return sum_total;
    }

    public void setSum_total(int sum_total) {
        this.sum_total = sum_total;
    }
}
