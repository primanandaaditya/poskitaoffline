package com.kitadigi.poskita.fragment.editunit;

import com.kitadigi.poskita.base.BaseResponse;

public interface IEditUnitResult {
    void onEditUnitSuccess(BaseResponse baseResponse);
    void onEditUnitError(String error);
}
