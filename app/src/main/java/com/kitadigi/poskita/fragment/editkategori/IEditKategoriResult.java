package com.kitadigi.poskita.fragment.editkategori;

import com.kitadigi.poskita.base.BaseResponse;

public interface IEditKategoriResult {

    void onEditSuccess(BaseResponse baseResponse);
    void onEditError(String error);
}
