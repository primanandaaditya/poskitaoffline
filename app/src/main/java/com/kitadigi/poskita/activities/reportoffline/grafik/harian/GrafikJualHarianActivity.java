package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

   //init chart
   LineChart line_chart;
   PieChart pie_chart;
   BarChart bar_chart;


   ImageView iv_back, iv_line_chart, iv_bar_chart, iv_pie_chart;
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
        //init chart
        line_chart=(LineChart)findViewById(R.id.line_chart);
        bar_chart=(BarChart)findViewById(R.id.bar_chart);
        pie_chart=(PieChart)findViewById(R.id.pie_chart);


        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_line_chart=(ImageView)findViewById(R.id.iv_line_chart);
        iv_bar_chart=(ImageView)findViewById(R.id.iv_bar_chart);
        iv_pie_chart=(ImageView)findViewById(R.id.iv_pie_chart);

        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);

        //untuk tutup activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //untuk gontaganti chart
        iv_line_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_chart.setVisibility(View.VISIBLE);
                pie_chart.setVisibility(View.GONE);
                bar_chart.setVisibility(View.GONE);
            }
        });

        iv_pie_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_chart.setVisibility(View.GONE);
                pie_chart.setVisibility(View.VISIBLE);
                bar_chart.setVisibility(View.GONE);
            }
        });

        iv_bar_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_chart.setVisibility(View.GONE);
                pie_chart.setVisibility(View.GONE);
                bar_chart.setVisibility(View.VISIBLE);
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
        ChartUtil.LineChartFormat(line_chart,
                grafikJualHarianController.getMap(grafikJualHarianModels),
                grafikJualHarianController.getFloats(grafikJualHarianModels),
                "Tanggal");

        ChartUtil.BarChartFormat(bar_chart,
                grafikJualHarianController.getMap(grafikJualHarianModels),
                grafikJualHarianController.getFloats(grafikJualHarianModels),
                "Tanggal");

        ChartUtil.PieChartFormat(pie_chart,
                grafikJualHarianController.getMap(grafikJualHarianModels),
                grafikJualHarianController.getFloats(grafikJualHarianModels),
                "Tanggal");
    }

    @Override
    public void onGrafikJualHarianError(String error) {
        this.showToast(error);
    }
}
