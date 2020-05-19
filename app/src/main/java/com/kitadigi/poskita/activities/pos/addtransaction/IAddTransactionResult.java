package com.kitadigi.poskita.activities.pos.addtransaction;

import com.kitadigi.poskita.base.BaseResponse;

public interface IAddTransactionResult {

    void onAddTransactionSuccess(BaseResponse baseResponse);
    void onAddTransactionError(String error);
}
