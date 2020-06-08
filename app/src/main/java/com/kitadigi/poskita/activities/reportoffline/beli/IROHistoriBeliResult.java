package com.kitadigi.poskita.activities.reportoffline.beli;

import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;

import java.util.List;

public interface IROHistoriBeliResult {
    void onHistoriBeliSuccess(List<BeliMaster> beliMasters);
    void onHistoriBeliError(String error);
}
