package com.kitadigi.poskita.sinkron.jual.insert;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.sinkron.brand.insert.AddBrandUtil;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandRequest;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandResult;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronInsertBrand;
import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinkronInsertJualController implements ISinkronAddJualRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronAddJualResult iSinkronAddJualResult;
    ISinkronInsertJual iSinkronInsertJual;
    JualMasterHelper jualMasterHelper;
    JualDetailHelper jualDetailHelper;
    InternetChecker internetChecker;

    //untuk get business id
    SessionManager sessionManager;
    String business_id;
    String nomorJualMaster, nomorJualDetail;
    String auth_token;
    String mobile_id;

    public SinkronInsertJualController(Context context, ISinkronAddJualResult iSinkronAddJualResult) {
        this.context = context;
        this.iSinkronAddJualResult = iSinkronAddJualResult;

        internetChecker=new InternetChecker();
        sessionManager = new SessionManager(context);
        business_id = sessionManager.getBussinessId();
        auth_token = sessionManager.getAuthToken();
    }

    @Override
    public void insert_jual() {

        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();
            //Log.d("data jual",data);

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
//                iSinkronAddJualResult.onSinkronAddJualError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

                iSinkronAddJualResult.onSinkronAddJualSuccess(null);
            }else{
                //jika ada data yg mau di-sync
                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();


                //kumpulkan data yang mau di-sync
                iSinkronInsertJual = AddJualUtil.getInterface();
                iSinkronInsertJual.insert_jual(auth_token,data).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {

                        iSinkronAddJualResult.onSinkronAddJualSuccess(response.body());
                        Log.d("sukses",call.request().url().toString());
                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronAddJualResult.onSinkronAddJualError(t.getMessage());
//                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronAddJualResult.onSinkronAddJualError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }


    }

    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        jualMasterHelper = new JualMasterHelper(context);
        jualDetailHelper = new JualDetailHelper(context);

        //get semua jual
        List<JualMaster> jualMasters = jualMasterHelper.semuaJual();


        //buat JSONArray
        JSONArray jsonJualMaster = new JSONArray();
        JSONArray jsonJualDetail;
        JSONObject jsonObject;

        //looping brand list
        for (JualMaster jualMaster : jualMasters){

            //jika ada yg belum sync insert
            if (jualMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){

                //simpan var nomor
                nomorJualMaster = jualMaster.getNomor();
                Log.d("no jual master", nomorJualMaster);

                //looping pada penjualan detail yang nomor-nya sama dengan nomorJualMaster
                List<JualDetail> jualDetailList = jualDetailHelper.getJualDetailByNomor(nomorJualMaster);
                Log.d("jmldetail", String.valueOf(jualDetailList.size()));

                //reset json jual detail sebelum di-looping
                jsonJualDetail = new JSONArray();

                for (JualDetail jualDetail : jualDetailList){

                    //isi nomor penjualan di variabel
                    nomorJualDetail = jualDetail.getNomor();

                    //init json
                    jsonObject = new JSONObject();

                    try {
                        //nama json
//                        jsonObject.put("nomor_trx", nomorJualDetail);
                        jsonObject.put("mobile_id_produk",jualDetail.getKode_id());
                        jsonObject.put("qty",jualDetail.getQty().toString());
                        jsonObject.put("price",jualDetail.getPrice().toString());

                        //tambahkan ke JSON
                        jsonJualDetail.put(jsonObject);
                        Log.d("jmldetail", jsonJualDetail.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {

                    //nama json
                    mobile_id = StringUtil.getRandomString(Constants.randomString);
                    jsonObject.put("contact_id",jualMaster.getContact_id());
                    jsonObject.put("mobile_id", mobile_id);
                    jsonObject.put("total_pay",jualMaster.getTotal_pay().toString());
                    jsonObject.put("total_price",jualMaster.getTotal_price().toString());
                    jsonObject.put("transaction_date", jualMaster.getTanggal());
//                    jsonObject.put("total_item", jualMaster.getTotal_item().toString());

                    jsonObject.put("detail", jsonJualDetail);
                    //tambahkan ke JSON
                    jsonJualMaster.put(jsonObject);

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
            hasil =  jsonJualMaster.toString();
            Log.d("penjualan", hasil);
        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {

        //init sqlite
        jualMasterHelper = new JualMasterHelper(context);

        //get semua jual
        List<JualMaster> jualMasters = jualMasterHelper.semuaJual();

        //looping penjualan
        for (JualMaster jualMaster : jualMasters){

            //jika ada yg belum sync insert
            if (jualMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){
                jualMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                jualMasterHelper.updateJualMaster(jualMaster);
            }
        }
    }


}
