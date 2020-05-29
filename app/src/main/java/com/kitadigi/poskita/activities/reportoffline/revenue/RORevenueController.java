package com.kitadigi.poskita.activities.reportoffline.revenue;

import android.content.Context;

import com.kitadigi.poskita.activities.reportoffline.revenue.IRORevenueRequest;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.List;

public class RORevenueController implements IRORevenueRequest {

    Context context;
    IRORevenueResult result;
    JualMasterHelper jualMasterHelper;
    List<RevenueModel> revenueModels;

    public RORevenueController(Context context, IRORevenueResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getRevenue(String tanggalDari, String tanggalHingga) {

        jualMasterHelper=new JualMasterHelper(context);

        try{
            revenueModels = jualMasterHelper.reportRevenue(tanggalDari, tanggalHingga);
            result.onRORevenueSuccess(revenueModels);
        }catch (Exception e){
            result.onRORevenueError(e.getMessage());
        }

    }
}
