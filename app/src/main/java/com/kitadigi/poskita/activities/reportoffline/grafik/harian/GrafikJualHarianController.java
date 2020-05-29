package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.List;

public class GrafikJualHarianController implements IGrafikJualHarianRequest {

    Context context;
    IGrafikJualHarianResult result;

    JualMasterHelper jualMasterHelper;
    JualDetailHelper jualDetailHelper;

    List<JualMaster> jualMasters;

    public GrafikJualHarianController(Context context, IGrafikJualHarianResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getData() {

        try {

            String tanggal;
            String nomor;

            //init jualmasterhelper
            jualMasterHelper=new JualMasterHelper(context);

            //grup record by tanggal
            List<GrafikJualHarianModel> grafikJualHarianModels = jualMasterHelper.grupByTanggal();
            result.onGrafikJualHarianSuccess(grafikJualHarianModels);

        }catch (Exception e){
            result.onGrafikJualHarianError(e.getMessage());
        }



    }
}
