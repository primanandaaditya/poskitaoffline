package com.kitadigi.poskita.util;

import android.content.Context;

import android.view.MenuItem;

import android.widget.ImageView;

import android.widget.PopupMenu;

import android.widget.Toast;



import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.components.AxisBase;

import com.github.mikephil.charting.components.XAxis;

import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.BarData;

import com.github.mikephil.charting.data.BarDataSet;

import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.LineData;

import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.data.PieData;

import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.kitadigi.poskita.R;


import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

public class ChartUtil {


    public  static void PieChartFormat(PieChart pieChart, final HashMap<Integer, String> SumbuX, List<Float> SumbuY, String label){

        List<PieEntry> entries1 = new ArrayList<PieEntry>();
        int awal,akhir;
        awal= 1 ;
        akhir=SumbuX.size();

        for(int num =awal; num <= akhir; num++){
            entries1.add(new PieEntry( SumbuY.get(num-1), SumbuX.get(num) ));

        }

        PieDataSet pieDataSet = new PieDataSet(entries1, label);
        PieData data = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(data);
        pieChart.invalidate();

    }

    public  static void BarChartFormat(BarChart barChart, final HashMap<Integer, String> SumbuX, List<Float> SumbuY, String label){

        barChart.setScaleMinima(5,0);
        List<BarEntry> entries1 = new ArrayList<BarEntry>();
        int awal,akhir;
        awal= 1 ;

        akhir=SumbuX.size();

        if (akhir==1){

            awal=0;

            final String labelX = SumbuX.get(1);

            entries1.add(new BarEntry(1, SumbuY.get(0)));

            BarDataSet barDataSet = new BarDataSet(entries1, label);
            BarData barData = new BarData(barDataSet);
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);



            XAxis xAxis = barChart.getXAxis();

            xAxis.setDrawLabels(true);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);

            xAxis.setLabelCount(1);

            xAxis.setLabelRotationAngle(90);

            xAxis.setGranularity(1);

            xAxis.setValueFormatter(new IAxisValueFormatter() {



                @Override

                public String getFormattedValue(float value, AxisBase axis) {



                    return labelX;

                }



            });



            barChart.setData(barData);

            barChart.invalidate();



        }else{



            for(int num =awal; num <= akhir; num++){

                entries1.add(new BarEntry(num, SumbuY.get(num-1)));

            }





            BarDataSet barDataSet = new BarDataSet(entries1, label);

            BarData barData = new BarData(barDataSet);



            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);



            XAxis xAxis = barChart.getXAxis();

            xAxis.setGranularity(1);

            xAxis.setDrawLabels(true);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);

            xAxis.setLabelRotationAngle(90);

            xAxis.setValueFormatter(new IAxisValueFormatter() {



                @Override

                public String getFormattedValue(float value, AxisBase axis) {



                    return SumbuX.get((int)value);

                }



            });



            barChart.setData(barData);

            barChart.invalidate();



        }





    }

    public  static void LineChartFormat(LineChart lineChart, final HashMap<Integer, String> SumbuX, List<Float> SumbuY, String label){

        lineChart.setScaleMinima(2,0);
        List<Entry> entries1 = new ArrayList<Entry>();

        int awal,akhir;
        akhir=SumbuX.size();


        if (akhir==1){
            //kalau datanya hanya 1
            awal= 0 ;

            final String labelX = SumbuX.get(1);
            entries1.add(new Entry(1,SumbuY.get(0)));


            LineDataSet dataSet = new LineDataSet(entries1, label);
            dataSet.setDrawFilled(true);
            LineData data = new LineData(dataSet);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setDrawLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setLabelCount(1);
            xAxis.setLabelRotationAngle(90);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return labelX;
                }
            });

            lineChart.setData(data);
            lineChart.invalidate();

        }else{

            awal= 1 ;
            for(int num =awal; num <= akhir; num++){
                entries1.add(new Entry(num, SumbuY.get(num-1)));
            }

            LineDataSet dataSet = new LineDataSet(entries1, label);
            dataSet.setDrawFilled(true);
            LineData data = new LineData(dataSet);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(1);
            xAxis.setDrawLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            xAxis.setLabelRotationAngle(90);
            xAxis.setValueFormatter(new IAxisValueFormatter() {



                @Override

                public String getFormattedValue(float value, AxisBase axis) {
                    return SumbuX.get((int)value);

                }



            });

            lineChart.setData(data);
            lineChart.invalidate();
        }
    }

}
