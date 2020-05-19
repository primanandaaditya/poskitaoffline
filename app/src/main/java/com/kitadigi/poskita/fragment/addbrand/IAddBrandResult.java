package com.kitadigi.poskita.fragment.addbrand;

import com.kitadigi.poskita.base.BaseResponse;

public interface IAddBrandResult {
    void onBrandSuccess(BaseResponse baseResponse);
    void onBrandError(String error);
}
