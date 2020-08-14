package com.kitadigi.poskita.sinkron.jual.get_detail;

import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;

public interface IGetJualDetailResult {
    void onGetJualDetailSuccess(GetDetailModel getDetailModel);
    void onGetJualDetailError(String error);
}
