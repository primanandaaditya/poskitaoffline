package com.kitadigi.poskita.activities.reportoffline.analisa;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.util.ChartUtil;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ROAnalisaController implements IROAnalisaRequest {

    Context context;
    IROAnalisaResult result;

    List<ROAnalisaModel> roAnalisaModels;
    List<ROAnalisaModel> roAnalisaModelJual;
    List<ROAnalisaModel> roAnalisaModelBeli;

    ROAnalisaModel roAnalisaModel;

    //init sqlite
    BeliMasterHelper beliMasterHelper;
    JualMasterHelper jualMasterHelper;


    public ROAnalisaController(Context context, IROAnalisaResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getReport(String tanggalDari, String tanggalSampai) {

        //init sqlite
        jualMasterHelper=new JualMasterHelper(context);
        beliMasterHelper=new BeliMasterHelper(context);

        //get analisa penjualan dan pembelian
        roAnalisaModelJual = jualMasterHelper.analisaModel(tanggalDari, tanggalSampai);
        roAnalisaModelBeli = beliMasterHelper.analisaModel(tanggalDari, tanggalSampai);

        //gabungkan dua array
        roAnalisaModels=new ArrayList<>();
        roAnalisaModels.addAll(roAnalisaModelJual);
        roAnalisaModels.addAll(roAnalisaModelBeli);



        result.onROAnalisaSuccess(roAnalisaModels);

//        try {
//
//            //init sqlite
//            jualMasterHelper=new JualMasterHelper(context);
//            beliMasterHelper=new BeliMasterHelper(context);
//
//            //get analisa penjualan dan pembelian
//            roAnalisaModelJual = jualMasterHelper.analisaModel(tanggalDari, tanggalSampai);
//            roAnalisaModelBeli = beliMasterHelper.analisaModel(tanggalDari, tanggalSampai);
//
//            //gabungkan dua array
//            roAnalisaModels=new ArrayList<>();
//            roAnalisaModels.addAll(roAnalisaModelJual);
//            roAnalisaModels.addAll(roAnalisaModelBeli);
//
//
//
//            result.onROAnalisaSuccess(roAnalisaModels);
//        }catch (Exception e){
//
//            result.onROAnalisaGagal(e.getMessage());
//        }
    }

    public void setGrafikGrandTotalPenjualan(LineChart lineChart){



        final HashMap<Integer, String> map = new HashMap<>();
        List<Float> floats = new ArrayList<Float>();


        int jml = roAnalisaModelJual.size();

        for (int i=0; i <= jml -1; i++){
            map.put(i+1, roAnalisaModelJual.get(i).getTanggal());
            floats.add((float) roAnalisaModelJual.get(i).getGrand_total_penjualan());
        }

        ChartUtil.LineChartFormat(lineChart, map, floats,"");
    }


    public void setGrafikTotalItemPenjualan(LineChart lineChart){



        final HashMap<Integer, String> map = new HashMap<>();
        List<Float> floats = new ArrayList<Float>();


        int jml = roAnalisaModelJual.size();

        for (int i=0; i <= jml -1; i++){
            map.put(i+1, roAnalisaModelJual.get(i).getTanggal());
            floats.add((float) roAnalisaModelJual.get(i).getTotal_item_penjualan());
        }

        ChartUtil.LineChartFormat(lineChart, map, floats,"");
    }

    public void setGrafikRasio(PieChart pieChart){


        //hitung total grand untuk penjualan
        Integer hasilPenjualan, hasilPembelian;
        Integer grandTotal;

        //looping model list
        hasilPenjualan = 0;
        hasilPembelian = 0;
        grandTotal = 0;

        for (ROAnalisaModel roAnalisaModel: roAnalisaModelJual){
            if (roAnalisaModel.getGrand_total_penjualan()==null){
            }else{
                grandTotal = roAnalisaModel.getGrand_total_penjualan();
                hasilPenjualan = hasilPenjualan + grandTotal;
            }
        }


        //hitung total grand pembelian
        for (ROAnalisaModel roAnalisaModel: roAnalisaModelBeli){
            if (roAnalisaModel.getGrand_total_pembelian()==null){
            }else{
                grandTotal = roAnalisaModel.getGrand_total_pembelian();
                hasilPembelian = hasilPembelian + grandTotal;
            }
        }


        //bentuk komponen chart lingkaran
        final HashMap<Integer, String> map = new HashMap<>();
        List<Float> floats = new ArrayList<Float>();

        //bentuk label
        map.put(0, "Pembelian");
        map.put(1, "Penjualan");

        //bentuk sumby
        floats.add((float) hasilPenjualan);
        floats.add((float) hasilPembelian);

        ChartUtil.PieChartFormat(pieChart, map, floats,"");
    }


    String getGrandTotalPenjualan(List<ROAnalisaModel> roAnalisaModels){

        Integer hasil;
        Integer grandTotalPenjualan;

        //looping model list
        hasil = 0;
        grandTotalPenjualan = 0;

        for (ROAnalisaModel roAnalisaModel: roAnalisaModels){

            if (roAnalisaModel.getGrand_total_penjualan()==null){

            }else{
                grandTotalPenjualan = roAnalisaModel.getGrand_total_penjualan();
                hasil = hasil + grandTotalPenjualan;
            }


        }

        String result = StringUtil.formatRupiah(hasil);
        return result;
    }


    String getTotalPenjualan(List<ROAnalisaModel> roAnalisaModels){

        Integer hasil;
        Integer grandTotalPenjualan;

        //looping model list
        hasil = 0;
        grandTotalPenjualan = 0;

        for (ROAnalisaModel roAnalisaModel: roAnalisaModels){

            if (roAnalisaModel.getGrand_total_penjualan()==null){

            }else{
                grandTotalPenjualan = roAnalisaModel.getTotal_item_penjualan();
                hasil = hasil + grandTotalPenjualan;
            }


        }

        String result = StringUtil.formatRupiah(hasil);
        return result;
    }


    String getGrandTotalPembelian(List<ROAnalisaModel> roAnalisaModels){

        Integer hasil;
        Integer grandTotalPembelian;

        //looping model list
        hasil = 0;
        grandTotalPembelian = 0;

        for (ROAnalisaModel roAnalisaModel: roAnalisaModels){

            //abaikan jika null
            if (roAnalisaModel.getGrand_total_pembelian() == null){

            }else{
                grandTotalPembelian = roAnalisaModel.getGrand_total_pembelian();
                hasil = hasil + grandTotalPembelian;
            }

        }


        String result = StringUtil.formatRupiah(hasil);
        return result;
    }
}
