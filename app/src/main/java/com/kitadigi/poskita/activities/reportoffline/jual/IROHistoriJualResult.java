package com.kitadigi.poskita.activities.reportoffline.jual;

import com.kitadigi.poskita.dao.jualmaster.JualMaster;

import java.util.List;

public interface IROHistoriJualResult {
    void onHistoriJualSuccess(List<JualMaster> jualMasters);
    void onHistoriJualError(String error);
}
