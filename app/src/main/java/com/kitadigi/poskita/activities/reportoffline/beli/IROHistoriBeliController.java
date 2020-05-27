package com.kitadigi.poskita.activities.reportoffline.beli;

import android.content.Context;

import com.kitadigi.poskita.activities.reportoffline.jual.IROHistoriJualRequest;
import com.kitadigi.poskita.activities.reportoffline.jual.IROHistoriJualResult;
import com.kitadigi.poskita.dao.beli.Beli;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.List;

public class IROHistoriBeliController implements IROHistoriBeliRequest {

    Context context;
    IROHistoriBeliResult iroHistoriBeliResult;
    BeliMasterHelper beliMasterHelper;
    List<BeliMaster> beliMasters;

    public IROHistoriBeliController(Context context, IROHistoriBeliResult iroHistoriBeliResult) {
        this.context = context;
        this.iroHistoriBeliResult = iroHistoriBeliResult;
    }

    @Override
    public void getReport(String tanggal) {

        beliMasterHelper=new BeliMasterHelper(context);
        try {
            beliMasters = beliMasterHelper.getBeliMasterByTanggal(tanggal);
            iroHistoriBeliResult.onHistoriBeliSuccess(beliMasters);
        }catch (Exception e){
            iroHistoriBeliResult.onHistoriBeliError(e.getMessage());
        }
    }

    @Override
    public Integer getCount(List<BeliMaster> beliMasters) {
        Integer jml;
        jml = beliMasters.size();
        return jml;
    }

    @Override
    public Integer getSum(List<BeliMaster> beliMasters) {
        Integer jml;
        if (beliMasters.size()==0){
            jml = 0;
        }else{
            Integer counter = 0;
            for (BeliMaster beliMaster: beliMasters){
                counter = counter + beliMaster.getTotal_price();
            }
            jml = counter;
        }
        return jml;
    }
}
