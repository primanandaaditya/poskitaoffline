package com.kitadigi.poskita.fragment.brand.dengan_header;

import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.fragment.brand.BrandData;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.dengan_header.BrandUtil;
import com.kitadigi.poskita.fragment.brand.dengan_header.IBrand;
import com.kitadigi.poskita.fragment.brand.IBrandRequest;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandController implements IBrandRequest {

    IBrand iBrand;
    Context context;
    IBrandResult iBrandResult;
    SweetAlertDialog sweetAlertDialog;
    SessionManager sessionManager;
    String encrytedIdUsers;

    BrandHelper brandHelper;
    Brand brand;
    InternetChecker internetChecker;
    boolean showDialog=true;
    boolean offlineMode;


    public BrandController(Context context, IBrandResult iBrandResult) {
        this.context = context;
        this.iBrandResult = iBrandResult;
        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();

    }



    public BrandController(Context context, IBrandResult iBrandResult, boolean offlineMode) {
        this.context = context;
        this.iBrandResult = iBrandResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encrytedIdUsers=sessionManager.getEncryptedIdUsers();

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();
    }

    @Override
    public void getBrandList() {

        if (offlineMode){

            //jika tidak ada koneksi internet
            iBrandResult.onBrandError("", brandHelper.getAllBrand());

        }else{
            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){

//            Toast.makeText(context,encrytedIdUsers,Toast.LENGTH_SHORT).show();

                //jika ada koneksi internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
//            if ((showDialog == true)) {
//                sweetAlertDialog.show();
//            } else {
//
//            }

                String auth_token = sessionManager.getAuthToken();

                iBrand=BrandUtil.getInterface();
                iBrand.getBrandList(auth_token).enqueue(new Callback<BrandModel>() {
                    @Override
                    public void onResponse(Call<BrandModel> call, Response<BrandModel> response) {


                        if (response.body().getData() == null){

                        }else{
                            //ini langkah untuk memasukkan data online
                            //ke database offline
                            //hapus dulu semua brand di sqlite
                            brandHelper.deleteAllBrand();

                            //looping hasil brand
                            //masukkan semua data online ke sqlite
                            for (BrandData brandData:response.body().getData()){

                                brand = new Brand();
                                brand.setBusiness_id(brandData.getBusiness_id());
                                brand.setDescription(brandData.getDescription());
                                brand.setId(Long.parseLong(brandData.getId()));
                                brand.setName(brandData.getName());
                                brand.setKode_id(brandData.getMobile_id());
                                brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                brand.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                                brand.setSync_update(Constants.STATUS_SUDAH_SYNC);


                                //commit insert
                                brandHelper.addBrand(brand);
                            }
                        }


                        iBrandResult.onBrandSuccess(response.body(), brandHelper.getAllBrand());

//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }

                    @Override
                    public void onFailure(Call<BrandModel> call, Throwable t) {

                        iBrandResult.onBrandError(t.getMessage(), brandHelper.getAllBrand());
//                    if ((showDialog == true)) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    } else {
//
//                    }
                    }
                });

            }else{

                //jika tidak ada koneksi internet
                iBrandResult.onBrandError("", brandHelper.getAllBrand());

            }

        }



    }
}
