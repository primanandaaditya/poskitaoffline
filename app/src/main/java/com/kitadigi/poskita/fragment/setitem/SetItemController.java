package com.kitadigi.poskita.fragment.setitem;

import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.BrandUtil;
import com.kitadigi.poskita.fragment.brand.IBrand;
import com.kitadigi.poskita.fragment.kategori.IKategori;
import com.kitadigi.poskita.fragment.kategori.KategoriModel;
import com.kitadigi.poskita.fragment.kategori.KategoriUtil;
import com.kitadigi.poskita.fragment.unit.IUnit;
import com.kitadigi.poskita.fragment.unit.UnitModel;
import com.kitadigi.poskita.fragment.unit.UnitUtil;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetItemController implements SetItemRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    SetItemModel setItemModel;
    SetItemResult setItemResult;
    SessionManager sessionManager;
    String encryptedIdUsers;

    IKategori iKategori;
    IBrand iBrand;
    IUnit iUnit;



    public SetItemController(Context context, SetItemResult setItemResult) {
        this.context = context;
        this.setItemResult = setItemResult;
        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

    }

    @Override
    public void getSetItem() {

        setItemModel=new SetItemModel();
        if (new InternetChecker().haveNetwork(context)){
            //jika ada internet=======================================================

            //mulai tembak api master kategori
            sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText(context.getResources().getString(R.string.memuat_data));
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();

            iKategori= KategoriUtil.getInterface();
            iKategori.getKategoriList(encryptedIdUsers).enqueue(new Callback<KategoriModel>() {
                @Override
                public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {
                    setItemModel.setKategoriModel(response.body());


                    //mulai tembak api master_unit
                    iUnit= UnitUtil.getInterface();
                    iUnit.getUnitList(encryptedIdUsers).enqueue(new Callback<UnitModel>() {
                        @Override
                        public void onResponse(Call<UnitModel> call, Response<UnitModel> response) {

                            setItemModel.setUnitModel(response.body());

                            //mulai tembak api master_brand
                            iBrand= BrandUtil.getInterface();
                            iBrand.getBrandList(encryptedIdUsers).enqueue(new Callback<BrandModel>() {
                                @Override
                                public void onResponse(Call<BrandModel> call, Response<BrandModel> response) {
                                    setItemModel.setBrandModel(response.body());
                                    sweetAlertDialog.dismissWithAnimation();
                                }

                                @Override
                                public void onFailure(Call<BrandModel> call, Throwable t) {
                                    setItemModel.setBrandModel(null);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<UnitModel> call, Throwable t) {
                            setItemModel.setUnitModel(null);
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });


                }

                @Override
                public void onFailure(Call<KategoriModel> call, Throwable t) {
                    setItemModel.setKategoriModel(null);
                    sweetAlertDialog.dismissWithAnimation();
                }

            });

            setItemResult.onSuccessSetItem(setItemModel);

        }else{

            //jika tidak ada internet=======================================================




        }
    }
}
