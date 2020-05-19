package com.kitadigi.poskita.activities.report.transaction;

public class ReportTransactionData {

    String id;
    String invoice_no;
    String transaction_date;
    String payment_method;
    String payment_ref_no;
    String amount;
    Integer qty;
    String additional;

    public String getPayment_ref_no() {
        return payment_ref_no;
    }

    public void setPayment_ref_no(String payment_ref_no) {
        this.payment_ref_no = payment_ref_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
