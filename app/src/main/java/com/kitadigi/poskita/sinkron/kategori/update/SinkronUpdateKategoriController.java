package com.kitadigi.poskita.sinkron.kategori.update;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
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

public class SinkronUpdateKategoriController implements ISinkronUpdateKategoriRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronUpdateKategoriResult iSinkronUpdateKategoriResult;
    ISinkronUpdateKategori iSinkronUpdateKategori;
    KategoriHelper kategoriHelper;
    InternetChecker internetChecker;
    SessionManager sessionManager;
    String business_id;
    String auth_token;


    public SinkronUpdateKategoriController(Context context, ISinkronUpdateKategoriResult iSinkronUpdateKategoriResult) {
        this.context = context;
        this.iSinkronUpdateKategoriResult = iSinkronUpdateKategoriResult;
        internetChecker=new InternetChecker();
        sessionManager=new SessionManager(context);
        business_id=sessionManager.getBussinessId();
        auth_token = sessionManager.getAuthToken();

    }



    @Override
    public void update_kategori() {


        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
//                iSinkronUpdateKategoriResult.onSinkronUpdateKategoriError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

                iSinkronUpdateKategoriResult.onSinkronUpdateKategoriSuccess(null);
            }else{

                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();

                Log.d("update",data);

                //kumpulkan data yang mau di-sync
                iSinkronUpdateKategori = SinkronUpdateKategoriUtil.getInterface();
                iSinkronUpdateKategori.update_kategori(data, auth_token).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {
                        Log.d("sukses", call.request().url().toString());
                        iSinkronUpdateKategoriResult.onSinkronUpdateKategoriSuccess(response.body());
                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronUpdateKategoriResult.onSinkronUpdateKategoriError(t.getMessage().toString());
//                        sweetAlertDialog.dismissWithAnimation();

                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronUpdateKategoriResult.onSinkronUpdateKategoriError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }

    }


    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        kategoriHelper=new KategoriHelper(context);

        //get semua kategori
        List<Kategori> kategoris = kategoriHelper.semuaKategori();

        //buat JSONArray
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        //looping kategori list
        for (Kategori kategori:kategoris){

            //jika ada yg belum sync update
            if (kategori.getSync_update() == Constants.STATUS_BELUM_SYNC){

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {
                    //nama json
                    jsonObject.put("nama", kategori.getName_category());
                    jsonObject.put("mobile_id",kategori.getKode_id());
                    jsonObject.put("business_id",business_id);
                    jsonObject.put("short_code","");
                    jsonObject.put("parent_id","0");
                    jsonObject.put("created_by","1");

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
        kategoriHelper=new KategoriHelper(context);

        //get semua kategori
        List<Kategori> kategoris = kategoriHelper.semuaKategori();

        //looping kategori list
        for (Kategori kategori:kategoris){

            //jika ada yg belum sync insert
            if (kategori.getSync_update() == Constants.STATUS_BELUM_SYNC){
                kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                kategoriHelper.updateKategori(kategori);
            }
        }
    }
}
