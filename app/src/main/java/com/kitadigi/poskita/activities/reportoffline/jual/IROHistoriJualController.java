package com.kitadigi.poskita.activities.reportoffline.jual;

import android.content.Context;

import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.List;

public class IROHistoriJualController implements IROHistoriJualRequest {

    Context context;
    IROHistoriJualResult iroHistoriJualResult;
    JualMasterHelper jualMasterHelper;
    List<JualMaster> jualMasters;

    public IROHistoriJualController(Context context, IROHistoriJualResult iroHistoriJualResult) {
        this.context = context;
        this.iroHistoriJualResult = iroHistoriJualResult;
    }

    @Override
    public void getReport(String tanggal) {

        jualMasterHelper=new JualMasterHelper(context);
        try {
            jualMasters = jualMasterHelper.getJualMasterByTanggal(tanggal);
            iroHistoriJualResult.onHistoriJualSuccess(jualMasters);
        }catch (Exception e){
            iroHistoriJualResult.onHistoriJualError(e.getMessage());
        }
    }
}
