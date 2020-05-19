package com.kitadigi.poskita.fragment.deleteunit;

import com.kitadigi.poskita.base.BaseResponse;

public interface IDeleteUnitResult {
    void onDeleteUnitSuccess(BaseResponse baseResponse);
    void onDeleteUnitError(String error);
}
