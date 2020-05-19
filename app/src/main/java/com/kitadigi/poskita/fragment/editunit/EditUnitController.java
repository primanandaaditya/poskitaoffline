package com.kitadigi.poskita.fragment.editunit;

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

public class EditUnitController implements IEditUnitRequest {


    Context context;
    SweetAlertDialog sweetAlertDialog;
    IEditUnit iEditUnit;
    IEditUnitResult iEditUnitResult;
    SessionManager sessionManager;
    String enckripIdUsers;

    //untuk sync
    String enkripIdUnit;
    InternetChecker internetChecker;
    UnitHelper unitHelper;
    SimpleMD5 simpleMD5;

    boolean offlineMode;

    public EditUnitController(Context context, IEditUnitResult iEditUnitResult, boolean offlineMode) {
        this.context = context;
        this.iEditUnitResult = iEditUnitResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        enckripIdUsers=sessionManager.getEncryptedIdUsers();
        unitHelper=new UnitHelper(context);
        simpleMD5=new SimpleMD5();
        internetChecker=new InternetChecker();
    }

    public EditUnitController(Context context, IEditUnitResult iEditUnitResult) {
        this.context = context;
        this.iEditUnitResult = iEditUnitResult;

        sessionManager=new SessionManager(context);
        enckripIdUsers=sessionManager.getEncryptedIdUsers();
        unitHelper=new UnitHelper(context);
        simpleMD5=new SimpleMD5();
        internetChecker=new InternetChecker();
    }

    @Override
    public void editUnit(String id_unit, final String name_unit, final String code_unit) {

        //siapkan variabel unit
        //untuk menemukan record unit di sqlite
        //jika sukses, edit record tsb dgn sync_update=SUKSES
        //jika gagal, edit record tsb dgn sync_update=GAGAL
        final Unit unit = unitHelper.getUnitById(Long.parseLong(id_unit));

        if (offlineMode){


            //jika offline
            unit.setName(name_unit);
            unit.setSingkatan(code_unit);
            unit.setSync_update(Constants.STATUS_BELUM_SYNC);

            //commit di sqlite
            unitHelper.updateUnit(unit);

            iEditUnitResult.onEditUnitError("");
        }else {

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //siapkan enkrip buat idkategori
                enkripIdUnit = simpleMD5.generate(id_unit);

                //mulai nembak API
                iEditUnit=EditUnitUtil.getInterface();
                iEditUnit.editUnit(enkripIdUnit,enckripIdUsers,name_unit, code_unit).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.d("sukses url", call.request().url().toString());

                        //jika sukses nembak API
                        unit.setName(name_unit);
                        unit.setSingkatan(code_unit);
                        unit.setSync_update(Constants.STATUS_SUDAH_SYNC);

                        //commit di sqlite
                        unitHelper.updateUnit(unit);

                        iEditUnitResult.onEditUnitSuccess(response.body());
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("gagal url", call.request().url().toString());

                        //jika sukses nembak API
                        unit.setName(name_unit);
                        unit.setSingkatan(code_unit);
                        unit.setSync_update(Constants.STATUS_BELUM_SYNC);

                        //commit di sqlite
                        unitHelper.updateUnit(unit);

                        iEditUnitResult.onEditUnitError(t.getMessage());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada internet

                //jika sukses nembak API
                unit.setName(name_unit);
                unit.setSingkatan(code_unit);
                unit.setSync_update(Constants.STATUS_BELUM_SYNC);

                //commit di sqlite
                unitHelper.updateUnit(unit);

                iEditUnitResult.onEditUnitError("");

            }



        }




    }
}
