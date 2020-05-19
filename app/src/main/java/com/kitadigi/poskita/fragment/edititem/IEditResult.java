package com.kitadigi.poskita.fragment.edititem;

import com.kitadigi.poskita.fragment.additem.AddBarangResult;

public interface IEditResult {

    void onSuccess(AddBarangResult addBarangResult);
    void onError(String error);

}
