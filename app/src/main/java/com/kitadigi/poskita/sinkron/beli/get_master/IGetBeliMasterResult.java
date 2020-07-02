package com.kitadigi.poskita.sinkron.beli.get_master;

public interface IGetBeliMasterResult {

    void onGetBeliMasterSuccess(GetBeliMasterModel getBeliMasterModel);
    void onGetBeliMasterError(String error);

}
