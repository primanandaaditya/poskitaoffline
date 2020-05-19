package com.kitadigi.poskita.fragment.editkategori;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKategoriController implements IEditKategoriRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IEditKategori iEditKategori;
    IEditKategoriResult iEditKategoriResult;
    SessionManager sessionManager;
    String enckripIdUsers;
    String enkripIdKategori;
    SimpleMD5 simpleMD5;


    //
    KategoriHelper kategoriHelper;
    InternetChecker internetChecker;

    boolean offlineMode;

    public EditKategoriController(Context context, IEditKategoriResult iEditKategoriResult) {
        this.context = context;
        this.iEditKategoriResult = iEditKategoriResult;
        sessionManager=new SessionManager(context);
        enckripIdUsers=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();

    }

    public EditKategoriController(Context context, IEditKategoriResult iEditKategoriResult, boolean offlineMode) {
        this.context = context;
        this.iEditKategoriResult = iEditKategoriResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enckripIdUsers=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
    }

    @Override
    public void editKategori(String id_kategori, final String nama_kategori, final String kode_kategori) {


        //siapkan variabel kategori
        //untuk menemukan record kategori di sqlite
        //jika sukses, edit record tsb dgn sync_update=SUKSES
        //jika gagal, edit record tsb dgn sync_update=GAGAL
        final Kategori kategori = kategoriHelper.getKategoriById(Long.parseLong(id_kategori));
        Log.d("nama kat", kategori.getName_category());
        Log.d("kode kat", kategori.getCode_category());

        if (offlineMode){


            //jika gagal, edit record kategori di sqlite
            kategori.setName_category(nama_kategori);
            kategori.setCode_category(kode_kategori);
            kategori.setSync_update(Constants.STATUS_BELUM_SYNC);

            //commit di sqlite
            kategoriHelper.updateKategori(kategori);

            iEditKategoriResult.onEditError("");
        }else{

            //cek dulu apakah ada koneksi internet
            if (internetChecker.haveNetwork(context)){


                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //siapkan enkrip buat idkategori
                enkripIdKategori = simpleMD5.generate(id_kategori);

                //mulai nembak API
                iEditKategori=EditKategoriUtil.getInterface();
                iEditKategori.editKategori(enkripIdKategori, enckripIdUsers, nama_kategori, kode_kategori)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                sweetAlertDialog.dismissWithAnimation();
                                Log.d("edit kategori suksis", call.request().url().toString());

                                //jika sukses, edit record kategori di sqlite
                                kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
                                kategori.setName_category(nama_kategori);
                                kategori.setCode_category(kode_kategori);

                                //commit di sqlite
                                kategoriHelper.updateKategori(kategori);

                                iEditKategoriResult.onEditSuccess(response.body());
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.d("edit kategori gagal", call.request().url().toString());
                                sweetAlertDialog.dismissWithAnimation();

                                //jika gagal, edit record kategori di sqlite
                                kategori.setName_category(nama_kategori);
                                kategori.setCode_category(kode_kategori);
                                kategori.setSync_update(Constants.STATUS_BELUM_SYNC);

                                //commit di sqlite
                                kategoriHelper.updateKategori(kategori);

                                iEditKategoriResult.onEditError(t.getMessage().toString());
                            }
                        });

                //jika ada
            }else{

                //jika tidak ada koneksi internet
                //jika gagal, edit record kategori di sqlite
                kategori.setName_category(nama_kategori);
                kategori.setCode_category(kode_kategori);
                kategori.setSync_update(Constants.STATUS_BELUM_SYNC);

                //commit di sqlite
                kategoriHelper.updateKategori(kategori);

                iEditKategoriResult.onEditError("");

            }


        }


    }
}
