package com.kitadigi.poskita.fragment.addkategori;

import com.kitadigi.poskita.base.BaseResponse;

public interface IAddKategoriResult {
    void onSuccess(BaseResponse baseResponse);
    void onError(String error);
}
