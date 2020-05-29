package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.util.ChartUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrafikJualHarianActivity extends BaseActivity implements IGrafikJualHarianResult {

   GrafikJualHarianController grafikJualHarianController;
   LineChart chart;
   ImageView iv_back;
   TextView tv_nav_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_jual_harian);

        findID();
        getData();

//        Toast.makeText(GrafikJualHarianActivity.this,String.valueOf(jualMasters.size()),Toast.LENGTH_SHORT).show();
    }

    void findID(){
        chart=(LineChart)findViewById(R.id.chart);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);

        //untuk tutup activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.applyFontBoldToTextView(tv_nav_header);
    }

    void getData(){

        grafikJualHarianController=new GrafikJualHarianController(GrafikJualHarianActivity.this, this);
        grafikJualHarianController.getData();

    }

    @Override
    public void onGrafikJualHarianSuccess(List<GrafikJualHarianModel> grafikJualHarianModels) {

        //tampilkan chart
        ChartUtil.LineChartFormat(chart,
                grafikJualHarianController.getMap(grafikJualHarianModels),
                grafikJualHarianController.getFloats(grafikJualHarianModels),
                "Tanggal");

    }

    @Override
    public void onGrafikJualHarianError(String error) {
        this.showToast(error);
    }
}
