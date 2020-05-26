package com.kitadigi.poskita.fragment.addkategori;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddKategoriController implements IAddKategoriRequest {
    Context context;
    SweetAlertDialog sweetAlertDialog;
    IAddKategori iAddKategori;
    IAddKategoriResult iAddKategoriResult;
    SessionManager sessionManager;
    String enkripIdUsers;

    KategoriHelper roomHelper;
    boolean offlineMode;

    public AddKategoriController(Context context, IAddKategoriResult iAddKategoriResult) {
        this.context = context;
        this.iAddKategoriResult = iAddKategoriResult;

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

    }

    public AddKategoriController(Context context, IAddKategoriResult iAddKategoriResult, boolean offlineMode) {
        this.context = context;
        this.iAddKategoriResult = iAddKategoriResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();
    }

    @Override
    public void addKategori(final String nama_kategori, final String kode_kategori) {

        roomHelper=new KategoriHelper(context);

        if (offlineMode){


//            roomHelper.insertKategoriBuilder(false,nama_kategori,kode_kategori);
//            iAddKategoriResult.onError("");
            simpanOffline(false,nama_kategori,kode_kategori);
        }else{

            //cek dulu apakah ada internet
            InternetChecker internetChecker = new InternetChecker();
            if (internetChecker.haveNetwork(context)){

                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                iAddKategori=AddKategoriUtil.getInterface();
                iAddKategori.addKategori(enkripIdUsers,nama_kategori,kode_kategori)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                //jika sukses nembak API
                                //simpan di sqlite
                                roomHelper.insertKategoriBuilder(true,nama_kategori,kode_kategori);
                                iAddKategoriResult.onSuccess(response.body());
                                sweetAlertDialog.dismissWithAnimation();
                                Log.d("url kategori sukses", call.request().url().toString());
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {

                                //jika gagal nembak API
                                //simpan di sqlite
                                simpanOffline(false,nama_kategori,kode_kategori);
                                sweetAlertDialog.dismissWithAnimation();


                                Log.d("url kategori eror", call.request().url().toString());

                            }
                        });


            }else{
                //jika tidak ada internet
                //simpan di sqlite
                simpanOffline(false,nama_kategori, kode_kategori);
            }
        }
    }

    @Override
    public void simpanOffline(boolean sudahSync,String name_category, String code_category) {
        //cek duplikasi
        boolean adaRow = roomHelper.cekNamaKategori(name_category);

        //jika tidak ada duplikasi
        if (adaRow==false){

            roomHelper.insertKategoriBuilder(sudahSync,name_category,code_category);
            iAddKategoriResult.onError(context.getResources().getString(R.string.tersimpan_offline));
        }else{
            //jika ada duplikasi
            iAddKategoriResult.onError(context.getResources().getString(R.string.duplikasi_kategori));
        }
    }


}

