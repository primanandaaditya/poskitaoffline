package com.kitadigi.poskita.fragment.deletekategori;

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

public class DeleteKategoriController implements IDeleteRequest {

    Context context;
    IDeleteKategori iDeleteKategori;
    SweetAlertDialog sweetAlertDialog;
    IDeleteResult iDeleteResult;
    SessionManager sessionManager;
    String enkripIdUser;

    //untuk keperluan sync
    String IdKategoriMD5;
    SimpleMD5 simpleMD5;

    KategoriHelper kategoriHelper;
    InternetChecker internetChecker;

    boolean offlineMode;

    public DeleteKategoriController(Context context, IDeleteResult iDeleteResult) {
        this.context = context;
        this.iDeleteResult = iDeleteResult;

        sessionManager=new SessionManager(context);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
    }

    public DeleteKategoriController(Context context, IDeleteResult iDeleteResult, boolean offlineMode) {
        this.context = context;
        this.iDeleteResult = iDeleteResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        kategoriHelper=new KategoriHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
    }

    @Override
    public void deleteKategori(String enkripIdKategori) {

        //siapkan variabel kategori
        //untuk menemukan record kategori di sqlite
        //jika sukses, edit record tsb dgn sync_delete=SUKSES
        //jika gagal, edit record tsb dgn sync_delete=GAGAL
        final Kategori kategori = kategoriHelper.getKategoriById(Long.parseLong(enkripIdKategori));


        if (offlineMode == true){

            Log.d("mode","offline");
            //jika gagal, edit sync_delete_status record kategori di sqlite
            kategori.setSync_delete(Constants.STATUS_BELUM_SYNC);
            //commit di sqlite
            kategoriHelper.updateKategori(kategori);
            iDeleteResult.onDeleteError("");


        }else{

            Log.d("mode","online");

            //cek internet
            if (internetChecker.haveNetwork(context)){

                //kalau ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //siapkan enkrip buat idkategori
                IdKategoriMD5 = simpleMD5.generate(enkripIdKategori);

                //mulai nembak API
                iDeleteKategori=DeleteKategoriUtil.getInterface();
                iDeleteKategori.deleteKategori(IdKategoriMD5,enkripIdUser,"kosong")
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                iDeleteResult.onDeleteSuccess(response.body());

                                //jika sukses, edit record kategori di sqlite
                                kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                //commit di sqlite
                                kategoriHelper.updateKategori(kategori);

                                sweetAlertDialog.dismissWithAnimation();
                                Log.d("sukses url", call.request().url().toString());
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {


                                //jika gagal, edit sync_delete_status record kategori di sqlite
                                kategori.setSync_delete(Constants.STATUS_BELUM_SYNC);
                                //commit di sqlite
                                kategoriHelper.updateKategori(kategori);


                                iDeleteResult.onDeleteError(t.getMessage().toString());
                                sweetAlertDialog.dismissWithAnimation();

                                Log.d("sukses error", call.request().url().toString());
                            }
                        });
            }else{

                //kalau tidak ada internet

                //jika gagal, edit sync_delete_status record kategori di sqlite
                kategori.setSync_delete(Constants.STATUS_BELUM_SYNC);
                //commit di sqlite
                kategoriHelper.updateKategori(kategori);
                iDeleteResult.onDeleteError("");
            }
        }
    }
}
