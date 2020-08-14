package com.kitadigi.poskita.activities.propinsi;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PropinsiController implements IPropinsiRequest {
    Context context;
    IPropinsiResult result;


    public PropinsiController(Context context, IPropinsiResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getPropinsi() {

        try{

            List<String> propinsi = new ArrayList<>();

            String jsonPropinsi = "json/Propinsi.json";
            String isiPropinsi = StringUtil.loadJSONFromAsset(context, jsonPropinsi);
            Gson gson = new Gson();
            Log.d("json", isiPropinsi);
            PropinsiModel propinsiModel = gson.fromJson(isiPropinsi, PropinsiModel.class);


            result.onGetPropinsiSuccess(propinsiModel);

        }catch (Exception e){
            result.onGetPropinsiError(e.getLocalizedMessage());
        }





    }
}
