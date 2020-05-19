package com.kitadigi.poskita.fragment.deletekategori;

import com.kitadigi.poskita.base.BaseResponse;

public interface IDeleteResult {
    void onDeleteSuccess(BaseResponse baseResponse);
    void onDeleteError(String error);
}
