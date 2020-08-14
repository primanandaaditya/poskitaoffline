package com.kitadigi.poskita.sinkron.beli.get_detail;

public interface IGetBeliDetailResult {
    void onGetBeliDetailSuccess(GetBeliDetailModel getBeliDetailModel);
    void onGetBeliDetailError(String error);
}
