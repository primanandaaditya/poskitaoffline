package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KartuStokController implements IKartuStokRequest {


    Context context;
    IKartuStokResult result;

    //var untuk parameter mobile_id item
    String kode_id;

    //init sqlite
    JualMasterHelper jualMasterHelper;
    JualDetailHelper jualDetailHelper;
    BeliMasterHelper beliMasterHelper;
    BeliDetailHelper beliDetailHelper;


    List<KartuStokModel> kartuStokModels;
    KartuStokModel kartuStokModel;

    //var untuk menghitung selisih hari
    Long selisihHari;
    String tanggal;

    String nomor;

    public KartuStokController(Context context, IKartuStokResult result, String kode_id) {
        this.context = context;
        this.result = result;
        this.kode_id = kode_id;
    }

    @Override
    public void getReport(String tanggalDari, String tanggalSampai) {


        //buat calendar, untuk menambah hari waktu looping selisih hari
        Calendar c = Calendar.getInstance();

        //parameter tanggalDari dan tanggalSampai
        //diambil dari datePicker di ROKartuStokActivity.java


        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //buat var untuk param, lalu dikonversi jadi date
        Date dateDari = null;
        Date dateSampai = null;

        //hitung selisih antara 2 tanggal
        try {
            dateDari = format.parse(tanggalDari);
            dateSampai = format.parse(tanggalSampai);

            //in milliseconds
            long diff = dateSampai.getTime() - dateDari.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            Log.d("s hari",String.valueOf(diffDays));

            selisihHari = diffDays;

        } catch (Exception e) {

            selisihHari = 0l;
            e.printStackTrace();

        }


        //buat list<kartustokmodel>
        //yang pertama kali diisi adalah kolom 'tanggal'
        //dengan looping berdasarkan selisih hari diatas

        kartuStokModels=new ArrayList<>();

        //looping selisih hari, sambil membuat list kartustokmodel
        for (int i = 0; i <= selisihHari; i++) {

            //buat object kartu stok model
            kartuStokModel = new KartuStokModel();

            //assign-kan calendar dengan tanggalDari
            c.setTime(dateDari);

            //tambahkan 1 hari dari tanggal
            c.add(Calendar.DATE,i);

            //buat tanggal dengan format yyyy-mm-dd
            //supaya sama dengan kolom tanggal di sqlite
            tanggal = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

            //set model kartustokmodel
            kartuStokModel.setTanggal(tanggal);
            kartuStokModel.setKeluar(0);
            kartuStokModel.setMasuk(0);
            kartuStokModel.setSisa(0);


            //tambahkan model di list
            kartuStokModels.add(kartuStokModel);

        }

        hitungKeluar();
        hitungMasuk();
        hitungSisa();
        result.onSuccessKartuStok(kartuStokModels);

//        try {
//
//
//
//            result.onSuccessKartuStok(kartuStokModels);
//        }catch (Exception e){
//            result.onErrorKartuStok(e.getMessage());
//        }
    }

    @Override
    public void hitungKeluar() {

        Log.d("hitungkeluar","hitungkeluar");
        //var untuk counter untuk jumlah keluar
        Integer keluar,qty;

        keluar = 0;

        //cek size array
        Integer jml = kartuStokModels.size();
//        Log.d("jml data", String.valueOf(jml));

        if (jml==0){

        }else{

            //jika ada data
            jualDetailHelper=new JualDetailHelper(context);
            jualMasterHelper=new JualMasterHelper(context);

            //looping list dulu
            for (KartuStokModel kartuStokModel: kartuStokModels){


                //nolkan jumlah keluar
                keluar = 0;


                //cari tanggal dulu
                tanggal = kartuStokModel.getTanggal();

                //get list jualmaster, karena kolom yang menyimpan tanggal adalahh jualmaster
                List<JualMaster> jualMasters = jualMasterHelper.getJualMasterByTanggal(tanggal);
//                Log.d(tanggal, String.valueOf(jualMasters.size()));

                //looping di jualmaster, untuk get nomor
                for (JualMaster jualMaster: jualMasters){

                    //tampung nomor trx
                    nomor = jualMaster.getNomor();

                    //get jual detail list
                    List<JualDetail> jualDetails = jualDetailHelper.getJualDetailByNomor(nomor);
//                    Log.d("jml jualDetail", String.valueOf(jualDetails.size()));

                    //looping jual detail
                    for (JualDetail jualDetail: jualDetails){

                        //cocokan dulu dengan kode_id
                        if (jualDetail.getKode_id().equals(kode_id)){

//                            Log.d("bandingkan", jualDetail.getKode_id().toString() + " - " + kode_id);
                            //tampung var qty
                            qty = jualDetail.getQty();

                            //tambah dengan counter
                            keluar = keluar + qty;
                        }
                    }
                }

                //set di properti
                kartuStokModel.setKeluar(keluar);

            }
        }
    }

    @Override
    public void hitungMasuk() {

        Log.d("hitungmasuk","hitungmasuk");
        //var untuk counter untuk jumlah keluar
        Integer masuk,qty;

        masuk = 0;

        //cek size array
        Integer jml = kartuStokModels.size();
        Log.d("jml data", String.valueOf(jml));

        if (jml==0){

        }else{

            //jika ada data
            beliDetailHelper=new BeliDetailHelper(context);
            beliMasterHelper=new BeliMasterHelper(context);

            //looping list dulu
            for (KartuStokModel kartuStokModel: kartuStokModels){

                //nolkan jumlah masuk
                masuk = 0;

                //cari tanggal dulu
                tanggal = kartuStokModel.getTanggal();

                //get list belimaster, karena kolom yang menyimpan tanggal adalah belimaster
                List<BeliMaster> beliMasters = beliMasterHelper.getBeliMasterByTanggal(tanggal);
//                Log.d(tanggal, String.valueOf(beliMasters.size()));

                //looping di belimaster, untuk get nomor
                for (BeliMaster beliMaster: beliMasters){

                    //tampung nomor trx
                    nomor = beliMaster.getNomor();

                    //get beli detail list
                    List<BeliDetail> beliDetails = beliDetailHelper.getBeliByNomor(nomor);
                    Log.d("jml beliDetail", String.valueOf(beliDetails.size()));

                    //looping belidetail
                    for (BeliDetail beliDetail: beliDetails){

                        //cocokan dulu dengan kode_id
                        if (beliDetail.getKode_id_produk().equals(kode_id)){

                            Log.d("bandingkan", beliDetail.getKode_id_produk().toString() + " - " + kode_id);
                            //tampung var qty
                            qty = Integer.parseInt(beliDetail.getQty());

                            //tambah dengan counter
                            masuk = masuk + qty;
                        }
                    }
                }

                //set jumlah masuk di kartustokmodel
                kartuStokModel.setMasuk(masuk);
                Log.d("masuk jml", masuk.toString());

            }
        }

    }

    @Override
    public void hitungSisa() {

        //fungsi ini hanya untuk menghitung selisih keluar dan masuk

        Integer masuk,keluar,sisa;

        //cek jumlah data
        if (kartuStokModels.size()==0){

        }else{
            for (KartuStokModel kartuStokModel: kartuStokModels){

                masuk=kartuStokModel.getMasuk();
                keluar = kartuStokModel.getKeluar();
                sisa = masuk - keluar;

                kartuStokModel.setSisa(sisa);
            }
        }
    }

    public void sumKeluar(List<KartuStokModel> kartuStokModels, TextView textView){

        if (kartuStokModels.size()==0){
            textView.setText("Total keluar: 0");
        }else{

            //var untuk counter dalam loopin
            Integer counter;
            Integer jml;

            //looping model
            counter = 0;
            for (KartuStokModel kartuStokModel: kartuStokModels){

                jml = kartuStokModel.getKeluar();
                counter = counter + jml;
            }

            textView.setText("Total keluar: " + counter.toString());
        }
    }


    public void sumMasuk(List<KartuStokModel> kartuStokModels, TextView textView){

        if (kartuStokModels.size()==0){
            textView.setText("Total keluar: 0");
        }else{

            //var untuk counter dalam loopin
            Integer counter;
            Integer jml;

            //looping model
            counter = 0;
            for (KartuStokModel kartuStokModel: kartuStokModels){

                jml = kartuStokModel.getMasuk();
                counter = counter + jml;
            }

            textView.setText("Total masuk: " + counter.toString());
        }
    }
}
