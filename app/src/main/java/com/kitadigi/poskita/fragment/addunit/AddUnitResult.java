package com.kitadigi.poskita.fragment.addunit;

import com.kitadigi.poskita.base.BaseResponse;

public interface AddUnitResult {

    void onAddUnitSuccess(BaseResponse baseResponse);
    void onAddUnitError(String error);
}
