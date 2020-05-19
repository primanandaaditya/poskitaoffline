package com.kitadigi.poskita.sinkron.brand.insert;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
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

public class SinkronInsertBrandController implements ISinkronAddBrandRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronAddBrandResult iSinkronAddBrandResult;
    ISinkronInsertBrand iSinkronInsertBrand;
    BrandHelper brandHelper;
    InternetChecker internetChecker;

    //untuk get business id
    SessionManager sessionManager;
    String business_id;

    public SinkronInsertBrandController(Context context, ISinkronAddBrandResult iSinkronAddBrandResult) {
        this.context = context;
        this.iSinkronAddBrandResult = iSinkronAddBrandResult;

        internetChecker=new InternetChecker();
        sessionManager = new SessionManager(context);
        business_id = sessionManager.getBussinessId();
    }

    @Override
    public void insert_brand() {

        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();
            Log.d("data brand",data);

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
                iSinkronAddBrandResult.onSinkronAddBrandError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

            }else{
                //jika ada data yg mau di-sync
                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();


                //kumpulkan data yang mau di-sync
                iSinkronInsertBrand = AddBrandUtil.getInterface();
                iSinkronInsertBrand.insert_brand(data).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {

                        iSinkronAddBrandResult.onSinkronAddBrandSuccess(response.body());
                        Log.d("sukses",call.request().url().toString());
                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronAddBrandResult.onSinkronAddBrandError(t.getMessage());
//                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronAddBrandResult.onSinkronAddBrandError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }


    }

    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        brandHelper=new BrandHelper(context);

        //get semua brand
        List<Brand> brands = brandHelper.semuaBrand();

        //buat JSONArray
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        //looping brand list
        for (Brand brand : brands){

            //jika ada yg belum sync insert
            if (brand.getSync_insert() == Constants.STATUS_BELUM_SYNC){

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {
                    //nama json
                    jsonObject.put("nama", brand.getName());
                    jsonObject.put("mobile_id",brand.getKode_id());
                    jsonObject.put("business_id",business_id);
                    jsonObject.put("description",brand.getDescription());
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
            hasil = "";
        }else{
            hasil =  jsonArray.toString();
            Log.d("data brand", hasil);
        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {

        //init sqlite
        brandHelper=new BrandHelper(context);

        //get semua brand
        List<Brand> brands = brandHelper.semuaBrand();

        //looping kategori list
        for (Brand brand:brands){

            //jika ada yg belum sync insert
            if (brand.getSync_insert() == Constants.STATUS_BELUM_SYNC){
                brand.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                brandHelper.updateBrand(brand);
            }
        }
    }


}
