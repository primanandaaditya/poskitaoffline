package com.kitadigi.poskita.sinkron.beli.insert;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.sinkron.jual.insert.AddJualUtil;
import com.kitadigi.poskita.sinkron.jual.insert.ISinkronAddJualRequest;
import com.kitadigi.poskita.sinkron.jual.insert.ISinkronAddJualResult;
import com.kitadigi.poskita.sinkron.jual.insert.ISinkronInsertJual;
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

public class SinkronInsertBeliController implements ISinkronAddBeliRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronAddBeliResult iSinkronAddBeliResult;
    ISinkronInsertBeli iSinkronInsertBeli;
    BeliMasterHelper beliMasterHelper;
    BeliDetailHelper beliDetailHelper;
    InternetChecker internetChecker;

    //untuk get business id
    SessionManager sessionManager;
    String business_id;
    String nomorBeliMaster, nomorBeliDetail;

    String auth_token;

    public SinkronInsertBeliController(Context context, ISinkronAddBeliResult iSinkronAddBeliResult) {
        this.context = context;
        this.iSinkronAddBeliResult = iSinkronAddBeliResult;

        internetChecker=new InternetChecker();
        sessionManager = new SessionManager(context);
        business_id = sessionManager.getBussinessId();
        auth_token = sessionManager.getAuthToken();
    }

    @Override
    public void insert_beli() {

        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();
            Log.d("data beli",data);

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
//                iSinkronAddBeliResult.onSinkronAddBeliError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

                iSinkronAddBeliResult.onSinkronAddBeliSuccess(null);
            }else{
                //jika ada data yg mau di-sync
                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();


                //kumpulkan data yang mau di-sync
                iSinkronInsertBeli = AddBeliUtil.getInterface();
                iSinkronInsertBeli.insert_beli(auth_token,data).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {

                        iSinkronAddBeliResult.onSinkronAddBeliSuccess(response.body());
                        Log.d("sukses",call.request().url().toString());
                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronAddBeliResult.onSinkronAddBeliError(t.getMessage());
//                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronAddBeliResult.onSinkronAddBeliError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }


    }

    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        beliMasterHelper = new BeliMasterHelper(context);
        beliDetailHelper = new BeliDetailHelper(context);

        //get semua jual
        List<BeliMaster> beliMasters = beliMasterHelper.semuaBeli();
        List<BeliDetail> beliDetails = beliDetailHelper.semuaBeli();

//
//        Log.d("jml master",String.valueOf(beliMasters.size()));
//        Log.d("jml detail",String.valueOf(beliDetails.size()));
//
//        for(BeliDetail beliDetail : beliDetails){
//            Log.d("detail",String.valueOf(beliDetail.getNomor()));
//            Log.d("detail",String.valueOf(beliDetail.getKode_id_produk()));
//
//        }

        //buat JSONArray
        JSONArray jsonBeliMaster = new JSONArray();
        JSONArray jsonBeliDetail;
        JSONObject jsonObject;

        //looping brand list
        for (BeliMaster beliMaster : beliMasters){

            //jika ada yg belum sync insert
            if (beliMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){

                //simpan var nomor
                nomorBeliMaster = beliMaster.getNomor();

                //looping pada penjualan detail yang nomor-nya sama dengan nomorBeliMaster
                List<BeliDetail> beliDetailList = beliDetailHelper.getBeliByNomor(nomorBeliMaster);

                //reset json jual detail sebelum di-looping
                jsonBeliDetail = new JSONArray();

                for (BeliDetail beliDetail : beliDetailList){

                    //isi nomor penjualan di variabel
                    nomorBeliDetail = beliDetail.getNomor();

                    //init json
                    jsonObject = new JSONObject();

                    try {
                        //nama json
//                        jsonObject.put("nomor_trx", nomorBeliDetail);
                        jsonObject.put("mobile_id_produk",beliDetail.getKode_id_produk());
                        jsonObject.put("qty",beliDetail.getQty().toString());
                        jsonObject.put("price",beliDetail.getPrice().toString());

                        //tambahkan ke JSON
                        jsonBeliDetail.put(jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {

                    //nama json
                    jsonObject.put("supplier_id",beliMaster.getSupplier_id());
                    jsonObject.put("mobile_id", beliMaster.getNomor());
                    jsonObject.put("ref_no",beliMaster.getRef_no());
                    jsonObject.put("total_pay",beliMaster.getTotal_pay().toString());
                    jsonObject.put("total_price",beliMaster.getTotal_price().toString());
                    jsonObject.put("transaction_date", beliMaster.getTanggal());
//                    jsonObject.put("contact_id",beliMaster.getContact_id());
                    jsonObject.put("detail", jsonBeliDetail);
                    //tambahkan ke JSON
                    jsonBeliMaster.put(jsonObject);

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
            hasil =  jsonBeliMaster.toString();

        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {

        //init sqlite
        beliMasterHelper = new BeliMasterHelper(context);

        //get semua beli
        List<BeliMaster> beliMasters = beliMasterHelper.semuaBeli();

        //looping penjualan
        for (BeliMaster beliMaster:beliMasters){

            //jika ada yg belum sync insert
            if (beliMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){
                beliMaster.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                //commit perubahan
                beliMasterHelper.updateBeli(beliMaster);
            }
        }
    }


}
