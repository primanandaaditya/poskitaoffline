package com.kitadigi.poskita.sinkron.produk.delete;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnit;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnitRequest;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnitResult;
import com.kitadigi.poskita.sinkron.unit.delete.SinkronDeleteUnitUtil;
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

public class SinkronDeleteProdukController implements ISinkronDeleteProdukRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    ISinkronDeleteProdukResult iSinkronDeleteProdukResult;
    ISinkronDeleteProduk iSinkronDeleteProduk;
    ItemHelper itemHelper;
    InternetChecker internetChecker;

    SessionManager sessionManager;
    String business_id;


    public SinkronDeleteProdukController(Context context, ISinkronDeleteProdukResult iSinkronDeleteProdukResult ) {
        this.context = context;
        this.iSinkronDeleteProdukResult = iSinkronDeleteProdukResult;
        internetChecker=new InternetChecker();

        sessionManager=new SessionManager(context);
        business_id = sessionManager.getBussinessId();
    }



    @Override
    public void delete_produk() {

        //cek apakah ada koneksi internet
        if (internetChecker.haveNetwork(context)){

            //jika ada koneksi internet
            //cek apakah ada data yang di-sync atau tidak
            //kumpulkan data yang mau di-sync
            String data = kumpulkan_data();

            if (data==""){
                //jika tidak ada data sama sekali
                //langsung result-kan error
                iSinkronDeleteProdukResult.onSinkronDeleteProdukError(context.getResources().getString(R.string.tidak_ada_data_sinkroni));

            }else{

                //munculkan progress
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//                sweetAlertDialog.show();

                Log.d("delete unit",data);

                //kumpulkan data yang mau di-sync
                iSinkronDeleteProduk = DeleteProdukUtil.getInterface();
                iSinkronDeleteProduk.delete_produk(data).enqueue(new Callback<SinkronResponse>() {
                    @Override
                    public void onResponse(Call<SinkronResponse> call, Response<SinkronResponse> response) {
                        Log.d("sukses", call.request().url().toString());
                        iSinkronDeleteProdukResult.onSinkronDeleteProdukSuccess(response.body());
//                        ubahStatusSudahSync();
//                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<SinkronResponse> call, Throwable t) {
                        Log.d("gagal", call.request().url().toString());
                        iSinkronDeleteProdukResult.onSinkronDeleteProdukError(t.getMessage().toString());
//                        sweetAlertDialog.dismissWithAnimation();

                    }
                });
            }

        }else{

            //jika tidak ada koneksi internet
            //langsung result error
            iSinkronDeleteProdukResult.onSinkronDeleteProdukError(context.getResources().getString(R.string.tidak_ada_koneksi_internet));
        }
    }

    @Override
    public String kumpulkan_data() {

        //var untuk return
        String hasil = "";

        //var untuk hitung jml record yang akan di-sync
        Integer jumlah = 0;

        //init sqlite
        itemHelper=new ItemHelper(context);

        //get semua brand
        List<Item> items = itemHelper.semuaItem();

        //buat JSONArray
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        //looping kategori list
        for (Item item : items){

            //jika ada yg belum sync delete
            if (item.getSync_delete() == Constants.STATUS_BELUM_SYNC){

                jumlah = jumlah + 1;

                jsonObject=new JSONObject();
                try {
                    //nama json
                    jsonObject.put("mobile_id", item.getKode_id());
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
        }

        return hasil;
    }

    @Override
    public void ubahStatusSudahSync() {

    }
}
