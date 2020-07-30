package com.kitadigi.poskita.sinkron.unit.update;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinkronUpdateUnitController implements ISinkronUpdateUnitRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronUpdateUnitResult iSinkronUpdateUnitResult;
    ISinkronUpdateUnit iSinkronUpdateUnit;

    UnitHelper unitHelper;
    InternetChecker internetChecker;
    SessionManager sessionManager;
    String business_id;
    String auth_token;


    public SinkronUpdateUnitController(Context context, ISinkronUpdateUnitResult iSinkronUpdateUnitResult) {
        this.context = context;
        this.iSinkronUpdateUnitResult = iSinkronUpdateUnitResult;
        internetChecker=new InternetChecker();
        sessionManager=new SessionManager(context);
        business_id = sessionManager.getBussinessId();
        auth_token = sessionManager.getAuthToken();
    }



    @Override
    public void update_unit() {


        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
//                iSinkronUpdateUnitResult.onSinkronUpdateUnitError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

                iSinkronUpdateUnitResult.onSinkronUpdateUnitSuccess(null);
            }else{

                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();

                Log.d("update brand",data);

                //kumpulkan data yang mau di-sync
                iSinkronUpdateUnit = SinkronUpdateUnitUtil.getInterface();
                iSinkronUpdateUnit.update_unit(data, auth_token).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {
                        Log.d("sukses", call.request().url().toString());
                        iSinkronUpdateUnitResult.onSinkronUpdateUnitSuccess(response.body());
                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronUpdateUnitResult.onSinkronUpdateUnitError(t.getMessage().toString());
//                        sweetAlertDialog.dismissWithAnimation();

                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronUpdateUnitResult.onSinkronUpdateUnitError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }

    }


    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        unitHelper = new UnitHelper(context);

        //get semua list
        List<Unit> units = unitHelper.semuaUnit();

        //buat JSONArray
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        //looping brand list
        for (Unit unit : units){

            //jika ada yg belum sync update
            if (unit.getSync_update() == Constants.STATUS_BELUM_SYNC){

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {
                    //nama json
                    jsonObject.put("actual_name", unit.getName());
                    jsonObject.put("mobile_id", unit.getKode_id());
//                    jsonObject.put("business_id",business_id);
                    jsonObject.put("short_name", unit.getSingkatan());
//                    jsonObject.put("created_by","1");
                    jsonObject.put("allow_decimal","1");

                    //tambahkan ke JSON
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //cek apakah ada data yang di-sync atau tidak
        //jika ada buat JSON
        //jika tidak, return String kosongan
        if (jumlah == 0){
            //jika tidak ada data yang di-sync
            //return String kosongan
            hasil = "[]";
        }else{
            hasil =  jsonArray.toString();
        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {
        //init sqlite
        unitHelper = new UnitHelper(context);

        //get semua kategori
        List<Unit> units = unitHelper.semuaUnit();

        //looping kategori list
        for (Unit unit : units){

            //jika ada yg belum sync insert
            if (unit.getSync_update() == Constants.STATUS_BELUM_SYNC){
                unit.setSync_update(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                unitHelper.updateUnit(unit);
            }
        }
    }
}
