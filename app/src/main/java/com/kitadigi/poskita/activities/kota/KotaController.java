package com.kitadigi.poskita.activities.kota;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class KotaController implements IKotaRequest {

    Context context;
    IKotaResult result;


    public KotaController(Context context, IKotaResult result) {
        this.context = context;
        this.result = result;
    }


    @Override
    public void getKota(String idPropinsi) {
        try{

            //get class dari json/kota.json di asset
            String json = "json/Kota.json";
            String isi = StringUtil.loadJSONFromAsset(context, json);
            Gson gson = new Gson();
            KotaModel kotaModel= gson.fromJson(isi, KotaModel.class);

            //karena harus di-filter bedasarkan
            //idpropinsi, jadi model harus difilter

            List<Datum> datumList = new ArrayList<>();
            List<Datum> data = kotaModel.getData();

            //looping untuk filter
            for (Datum datum: data){
                if (datum.getProvince_id().equals(idPropinsi)){
                    datumList.add(datum);
                }
            }

            kotaModel.setData(null);
            kotaModel.setData(datumList);

            result.onGetKotaSuccess(kotaModel);

        }catch (Exception e){
            result.onGetKotaError(e.getMessage());
        }


    }
}
