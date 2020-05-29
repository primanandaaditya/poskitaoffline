package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrafikJualHarianController implements IGrafikJualHarianRequest {

    Context context;
    IGrafikJualHarianResult result;

    JualMasterHelper jualMasterHelper;


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

    public HashMap<Integer,String> getMap(List<GrafikJualHarianModel> grafikJualHarianModels){

        HashMap<Integer,String> hashMap = new HashMap<>();

        //tampung variabel tanggal dan jumlah
        String tanggal;

        //get jumlah array untuk jumlah sumbu chart
        int jml = grafikJualHarianModels.size();

        //looping array untuk persiapan data
        for (int i=0; i <= jml -1; i++){
            tanggal = grafikJualHarianModels.get(i).getTanggal();
            hashMap.put(i+1,tanggal);
        }

        return hashMap;
    }


    public List<Float> getFloats(List<GrafikJualHarianModel> grafikJualHarianModels){

        List<Float> hasil = new ArrayList<>();

        Integer jumlah;

        //get jumlah array untuk jumlah sumbu chart
        int jml = grafikJualHarianModels.size();

        //looping array untuk persiapan data
        for (int i=0; i <= jml -1; i++){
            jumlah = grafikJualHarianModels.get(i).getJumlah();
            hasil.add((float) jumlah);
        }

        return hasil;
    }

}
