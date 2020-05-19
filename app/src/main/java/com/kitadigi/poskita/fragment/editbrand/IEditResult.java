package com.kitadigi.poskita.fragment.editbrand;

import com.kitadigi.poskita.base.BaseResponse;

public interface IEditResult {
    void onEditSuccess(BaseResponse baseResponse);
    void onEditError(String error);
}
