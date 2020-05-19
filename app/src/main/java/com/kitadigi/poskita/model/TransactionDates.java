package com.kitadigi.poskita.model;

import java.util.ArrayList;

public class TransactionDates {
    private String date;
    private ArrayList<Transactions> transactionsDetailArrayList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Transactions> getTransactionsDetailArrayList() {
        return transactionsDetailArrayList;
    }

    public void setTransactionsDetailArrayList(ArrayList<Transactions> transactionsDetailArrayList) {
        this.transactionsDetailArrayList = transactionsDetailArrayList;
    }
}