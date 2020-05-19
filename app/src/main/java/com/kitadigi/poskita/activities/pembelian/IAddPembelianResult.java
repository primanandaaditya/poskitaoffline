package com.kitadigi.poskita.activities.pembelian;

import com.kitadigi.poskita.base.BaseResponse;

public interface IAddPembelianResult {

    void onAddPembelianSuccess(BaseResponse baseResponse);
    void onAddPembelianError(String error);

}
