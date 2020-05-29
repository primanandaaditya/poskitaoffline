package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.util.List;

public class GrafikJualHarianActivity extends BaseActivity implements IGrafikJualHarianResult {

   GrafikJualHarianController grafikJualHarianController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_jual_harian);

        grafikJualHarianController=new GrafikJualHarianController(GrafikJualHarianActivity.this, this);
        grafikJualHarianController.getData();

//        Toast.makeText(GrafikJualHarianActivity.this,String.valueOf(jualMasters.size()),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGrafikJualHarianSuccess(List<GrafikJualHarianModel> grafikJualHarianModels) {

        for (GrafikJualHarianModel grafikJualHarianModel: grafikJualHarianModels){
            Log.d("tanggal", grafikJualHarianModel.getTanggal());
            Log.d("jumlah", String.valueOf(grafikJualHarianModel.getJumlah()));
        }

    }

    @Override
    public void onGrafikJualHarianError(String error) {
        this.showToast(error);
    }
}
