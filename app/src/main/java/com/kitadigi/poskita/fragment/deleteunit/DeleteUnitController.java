package com.kitadigi.poskita.fragment.deleteunit;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUnitController implements IDeleteUnitRequest {

    Context context;
    IDeleteUnit iDeleteUnit;
    SweetAlertDialog sweetAlertDialog;
    IDeleteUnitResult iDeleteUnitResult;
    SessionManager sessionManager;
    String enkripIdUser;


    //untuk keperluan sync
    String IdUnitMD5;
    SimpleMD5 simpleMD5;
    UnitHelper unitHelper;
    InternetChecker internetChecker;

    boolean offlineMode;

    public DeleteUnitController(Context context, IDeleteUnitResult iDeleteUnitResult, boolean offlineMode) {
        this.context = context;
        this.iDeleteUnitResult = iDeleteUnitResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enkripIdUser=sessionManager.getEncryptedIdUsers();
        simpleMD5=new SimpleMD5();
        unitHelper=new UnitHelper(context);
        internetChecker=new InternetChecker();
    }

    public DeleteUnitController(Context context, IDeleteUnitResult iDeleteUnitResult) {
        this.context = context;
        this.iDeleteUnitResult = iDeleteUnitResult;

        sessionManager=new SessionManager(context);
        enkripIdUser=sessionManager.getEncryptedIdUsers();
        simpleMD5=new SimpleMD5();
        unitHelper=new UnitHelper(context);
        internetChecker=new InternetChecker();
    }

    @Override
    public void deleteKategori(String enkripIdUnit) {


        //siapkan variabel unit
        //untuk menemukan record unit di sqlite
        //jika sukses, edit record tsb dgn sync_delete=SUKSES
        //jika gagal, edit record tsb dgn sync_delete=GAGAL
        final Unit unit = unitHelper.getUnitById(Long.parseLong(enkripIdUnit));

        //siapkan enkrip buat idkategori
        IdUnitMD5 = simpleMD5.generate(enkripIdUnit);


        if (offlineMode){

            //jika offline mode
            //jika gagal, edit sync_delete_status record kategori di sqlite
            unit.setSync_delete(Constants.STATUS_BELUM_SYNC);
            //commit di sqlite
            unitHelper.updateUnit(unit);

            iDeleteUnitResult.onDeleteUnitError("");
        }else{

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.edit_unit));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //mulai nembak API
                iDeleteUnit=DeleteUnitUtil.getInterface();
                iDeleteUnit.deleteUnit(IdUnitMD5,enkripIdUser,"kosong").enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.d("url sukses",call.request().url().toString());

                        //jika sukses, edit record kategori di sqlite
                        unit.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                        //commit di sqlite
                        unitHelper.updateUnit(unit);

                        iDeleteUnitResult.onDeleteUnitSuccess(response.body());
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("url gagal",call.request().url().toString());

                        //jika gagal, edit sync_delete_status record kategori di sqlite
                        unit.setSync_delete(Constants.STATUS_BELUM_SYNC);
                        //commit di sqlite
                        unitHelper.updateUnit(unit);

                        iDeleteUnitResult.onDeleteUnitError(t.getMessage());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada internet
                //jika gagal, edit sync_delete_status record kategori di sqlite
                unit.setSync_delete(Constants.STATUS_BELUM_SYNC);
                //commit di sqlite
                unitHelper.updateUnit(unit);

                iDeleteUnitResult.onDeleteUnitError("");
            }


        }



    }
}
