package com.kitadigi.poskita.sinkron.jual.get_master;

public interface IGetJualMasterResult {
    void onGetJualMasterSuccess(MasterModel masterModel);
    void onGetJualMasterError(String error);
}
