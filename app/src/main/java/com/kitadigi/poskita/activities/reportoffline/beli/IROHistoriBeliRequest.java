package com.kitadigi.poskita.activities.reportoffline.beli;

import com.kitadigi.poskita.dao.belimaster.BeliMaster;

import java.util.List;

public interface IROHistoriBeliRequest {
    void getReport(String tanggal);
    Integer getCount(List<BeliMaster> beliMasters);
    Integer getSum(List<BeliMaster> beliMasters);
}
