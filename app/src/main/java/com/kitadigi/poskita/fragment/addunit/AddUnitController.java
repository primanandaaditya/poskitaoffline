package com.kitadigi.poskita.fragment.addunit;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUnitController implements AddUnitRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IAddUnit iAddUnit;
    AddUnitResult addUnitResult;
    SessionManager sessionManager;
    String enkripIdUsers;

    //untuk sync
    UnitHelper unitHelper;
    InternetChecker internetChecker;
    String bussinessId;

    boolean offlineMode;

    public AddUnitController(Context context, AddUnitResult addUnitResult, boolean offlineMode) {
        this.context = context;
        this.addUnitResult = addUnitResult;
        this.offlineMode = offlineMode;

        unitHelper=new UnitHelper(context);
        internetChecker=new InternetChecker();
        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        //generate new bussiness id
        bussinessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();
    }

    public AddUnitController(Context context, AddUnitResult addUnitResult) {
        this.context = context;
        this.addUnitResult = addUnitResult;

        unitHelper=new UnitHelper(context);
        internetChecker=new InternetChecker();
        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        //generate new bussiness id
        bussinessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();
    }

    @Override
    public void addUnit(final String name_unit, final String code_unit) {

        if (offlineMode){


            //simpan di sqlite
           simpanOffline(name_unit, code_unit);

        }else{

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada koneksi internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();



                iAddUnit=AddUnitUtil.getInterface();
                iAddUnit.addUnit(enkripIdUsers,name_unit,code_unit).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.d("sukses url", String.valueOf(response.code()));

                        //jika sukses nembak API
                        //simpan di sqlite
                        Unit unit=new Unit();
                        unit.setName(name_unit);
                        unit.setSingkatan(code_unit);
//                        unit.setKode_id(bussinessId);
                        unit.setKode_id(StringUtil.getRandomString(Constants.randomString));
                        unit.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                        unit.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                        unit.setSync_update(Constants.STATUS_SUDAH_SYNC);
                        unitHelper.addUnit(unit);

                        addUnitResult.onAddUnitSuccess(response.body());
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("gagal url", call.request().url().toString());

                        //jika gagal nembak API
                        //simpan di sqlite
                        simpanOffline(name_unit, code_unit);
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada koneksi internet
                //jika gagal nembak API
                //simpan di sqlite
                simpanOffline(name_unit, code_unit);
            }


        }

    }

    void simpanOffline(String name_unit,String code_unit){


        //cek duplikasi
        boolean adaRow = unitHelper.cekNamaUnit(name_unit);

        //jika tidak ada duplikasi
        if (adaRow==false){

            //simpan di sqlite
            Unit unit=new Unit();
            unit.setName(name_unit);
            unit.setSingkatan(code_unit);
//            unit.setKode_id(bussinessId);
            unit.setKode_id(StringUtil.getRandomString(Constants.randomString));
            unit.setSync_delete(Constants.STATUS_SUDAH_SYNC);
            unit.setSync_insert(Constants.STATUS_BELUM_SYNC);
            unit.setSync_update(Constants.STATUS_SUDAH_SYNC);
            unitHelper.addUnit(unit);

            addUnitResult.onAddUnitError(context.getResources().getString(R.string.tersimpan_offline));

        }else{
            //jika ada duplikasi
            addUnitResult.onAddUnitError(context.getResources().getString(R.string.duplikasi_unit));
        }

    }
}
