package com.kitadigi.poskita.activities.pos.addtransaction;

public interface IAddTransactionRequest {

    void addTransaction(
            String contact_id,
            int total_pay,
            int total_price
    );


}
