package com.kitadigi.poskita.activities.kecamatan;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class KecamatanController implements IKecamatanRequest {

    Context context;
    IKecamatanResult result;


    public KecamatanController(Context context, IKecamatanResult result) {
        this.context = context;
        this.result = result;
    }


    @Override
    public void getKecamatan(String idKota) {

        if (idKota==null){
            result.onGetKecamatanError("Tidak ada idKota");
        }else{

            try{

                //get class dari json/kota.json di asset
                String json = "json/Kecamatan.json";
                String isi = StringUtil.loadJSONFromAsset(context, json);
                Gson gson = new Gson();
                KecamatanModel kecamatanModel= gson.fromJson(isi, KecamatanModel.class);

                //karena harus di-filter bedasarkan
                //idkota, jadi model harus difilter

                List<Datum> datumList = new ArrayList<>();
                List<Datum> data = kecamatanModel.getData();

                Log.d("jml kecamatan", idKota);

                //looping untuk filter
                for (Datum datum: data){
                    if (datum.getCity_id().equals(idKota)){
                        datumList.add(datum);
                    }
                }

                kecamatanModel.setData(null);
                kecamatanModel.setData(datumList);

                result.onGetKecamatanSuccess(kecamatanModel);

            }catch (Exception e){
                result.onGetKecamatanError("Error");
            }

        }





    }
}
