package com.kitadigi.poskita.fragment.kategori.dengan_header;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.fragment.kategori.IKategoriRequest;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriController implements IKategoriRequest {

    IKategori iHeaderKategori;
    Context context;
    IKategoriResult iKategoriHeaderResult;
    SweetAlertDialog sweetAlertDialog;
    SessionManager sessionManager;
    String encrytedIdUsers;
    KategoriHelper kategoriHelper;
    Kategori kategori;
    Boolean showDialog=true;
    boolean offlineMode = false;


    //untuk di-destroy
    Call<KategoriModel> kategoriModelCall;

    public KategoriController(Context context, IKategoriResult iKategoriHeaderResult) {
        this.context = context;
        this.iKategoriHeaderResult = iKategoriHeaderResult;
        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);

    }

    public KategoriController(Context context, IKategoriResult iKategoriHeaderResult, boolean offlineMode) {
        this.context = context;
        this.iKategoriHeaderResult = iKategoriHeaderResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);

    }



    @Override
    public void getKategoriList() {

        if (offlineMode){
            //jika offline, ambil data dari sqlite langsung
            iKategoriHeaderResult.onKategoriError(context.getResources().getString(R.string.tidak_ada_koneksi_internet), kategoriHelper.getAllKategori());
            Log.d("kategori get","Offline mode");
        }else{


            //cek koneksi internet
            InternetChecker internetChecker =new InternetChecker();

            if (internetChecker.haveNetwork(context)){

                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
//            if ((showDialog == true)) {
//                sweetAlertDialog.show();
//            } else {
//
//            }

                String auth_token = sessionManager.getAuthToken();


                iHeaderKategori = KategoriUtil.getInterface();
                iHeaderKategori.getKategoriList(auth_token).enqueue(new Callback<KategoriModel>() {
                    @Override
                    public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {
                        Log.d("url kategori sukses", call.request().url().toString());

                        //untuk batalkan panggilan HTTP
                        kategoriModelCall=call;

                        if (response.body().getData() == null){

                        }else{

                            //jika ada datanya
                            //ini langkah untuk memasukkan data online
                            //ke database offline
                            //hapus dulu semua kategori di sqlite
                            kategoriHelper.deleteAllKategori();

                            //looping hasil kategori
                            //masukkan semua data online ke sqlite
                            if (response.body().getData().size()==0){

                            }else{

                                for (Datum datum:response.body().getData()){

                                    Log.d("kategori",datum.getId().toString());
                                    kategori=new Kategori();
                                    kategori.setName_category(datum.getName());
                                    kategori.setCode_category(datum.getCode_category());
                                    kategori.setKode_id(datum.getMobile_id());
                                    kategori.setId(Long.parseLong(datum.getId()));
                                    kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                    kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
                                    kategori.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                                    kategoriHelper.addKategori(kategori);
                                }

                            }


                            iKategoriHeaderResult.onKategoriSuccess(response.body(),kategoriHelper.getAllKategori());
//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }

                        }



                    }

                    @Override
                    public void onFailure(Call<KategoriModel> call, Throwable t) {
                        Log.d("url kategori gagal", call.request().url().toString());

                        iKategoriHeaderResult.onKategoriError(t.getMessage(), kategoriHelper.getAllKategori());
//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }

                    }
                });

            }else{
                //tidak ada internet
                iKategoriHeaderResult.onKategoriError(context.getResources().getString(R.string.tidak_ada_koneksi_internet), kategoriHelper.getAllKategori());
            }

        }



    }

    @Override
    public void cancelCall() {

    }

}

