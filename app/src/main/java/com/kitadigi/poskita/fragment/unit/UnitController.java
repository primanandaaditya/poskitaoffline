package com.kitadigi.poskita.fragment.unit;

import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitController implements IUnitRequest {

    IUnit iUnit;
    Context context;
    IUnitResult iUnitResult;
    SweetAlertDialog sweetAlertDialog;
    SessionManager sessionManager;
    String encrytedIdUsers;

    //untuk sync
    InternetChecker internetChecker;
    UnitHelper unitHelper;
    Unit unit;

    boolean showDialog=true;
    boolean offlineMode;

    public UnitController(Context context, IUnitResult iUnitResult) {
        this.context = context;
        this.iUnitResult = iUnitResult;
        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        unitHelper = new UnitHelper(context);

    }



    public UnitController(Context context, IUnitResult iUnitResult, boolean offlineMode) {
        this.context = context;
        this.iUnitResult = iUnitResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        internetChecker=new InternetChecker();
        unitHelper = new UnitHelper(context);
    }

    @Override
    public void getUnitList() {

        if (offlineMode){

            //jika tidak ada internet
            iUnitResult.onUnitError("",unitHelper.getAllUnit());
        }else{


            //cek apakah ada koneksi internet
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
                iUnit= UnitUtil.getInterface();
                iUnit.getUnitList(encrytedIdUsers).enqueue(new Callback<UnitModel>() {
                    @Override
                    public void onResponse(Call<UnitModel> call, Response<UnitModel> response) {

                        //ini langkah untuk memasukkan data online
                        //ke database offline
                        //hapus dulu semua unit di sqlite
                        unitHelper.deleteAllUnit();


                        //looping hasil unit
                        //masukkan semua data online ke sqlite
                        for (UnitData unitData:response.body().getData()){
                            unit = new Unit();
                            unit.setName(unitData.getName());
                            unit.setSingkatan(unitData.getShort_name());
                            unit.setId(Long.parseLong(unitData.getId()));
                            unit.setBusiness_id(unitData.getBusiness_id());
                            unit.setKode_id(unitData.getMobile_id());
                            unit.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            unit.setSync_update(Constants.STATUS_SUDAH_SYNC);
                            unit.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            unitHelper.addUnit(unit);
                        }


                        iUnitResult.onUnitSuccess(response.body(), unitHelper.getAllUnit());
//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }

                    @Override
                    public void onFailure(Call<UnitModel> call, Throwable t) {

                        iUnitResult.onUnitError(t.getMessage(),unitHelper.getAllUnit());
//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }
                });
            }else{
                //jika tidak ada internet
                iUnitResult.onUnitError("",unitHelper.getAllUnit());
            }

        }


    }
}
