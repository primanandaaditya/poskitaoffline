package com.kitadigi.poskita.model;

import java.util.ArrayList;

public class TransactionsList {
    private ArrayList<TransactionDates> transactionDatesArrayList = new ArrayList<>();

    public ArrayList<TransactionDates> getTransactionDatesArrayList() {
        return transactionDatesArrayList;
    }

    public void setTransactionDatesArrayList(ArrayList<TransactionDates> transactionDatesArrayList) {
        this.transactionDatesArrayList = transactionDatesArrayList;
    }
}
