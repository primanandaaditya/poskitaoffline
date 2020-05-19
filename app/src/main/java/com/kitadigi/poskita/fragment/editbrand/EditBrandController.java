package com.kitadigi.poskita.fragment.editbrand;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBrandController implements IEditRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IEditBrand iEditBrand;
    IEditResult iEditResult;
    SessionManager sessionManager;
    String enckripIdUsers, enkripIdBrand;

    //sync
    InternetChecker internetChecker;
    BrandHelper brandHelper;
    SimpleMD5 simpleMD5;

    boolean offlineMode;

    public EditBrandController(Context context, IEditResult iEditResult) {
        this.context = context;
        this.iEditResult = iEditResult;

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
    }

    public EditBrandController(Context context, IEditResult iEditResult, boolean offlineMode) {
        this.context = context;
        this.iEditResult = iEditResult;
        this.offlineMode = offlineMode;

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();
        simpleMD5=new SimpleMD5();
    }

    @Override
    public void editBrand(String id_brand, final String name, final String description) {

        //siapkan variabel brand
        //untuk menemukan record brand di sqlite
        //jika sukses, edit record tsb dgn sync_update=SUKSES
        //jika gagal, edit record tsb dgn sync_update=GAGAL
        final Brand brand = brandHelper.getBrandById(Long.parseLong(id_brand));


        if (offlineMode){

            //jika gagal, edit record kategori di sqlite
            brand.setSync_update(Constants.STATUS_BELUM_SYNC);
            brand.setName(name);
            brand.setDescription(description);

            //commit di sqlite
            brandHelper.updateBrand(brand);

            iEditResult.onEditError("");

        }else{


            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada internet
                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                //siapkan enkrip buat idkategori
                enkripIdBrand = simpleMD5.generate(id_brand);

                sessionManager=new SessionManager(context);
                enckripIdUsers=sessionManager.getEncryptedIdUsers();

                iEditBrand=EditBrandUtil.getInterface();
                iEditBrand.editBrand(enkripIdBrand,enckripIdUsers,name,description).enqueue(new Callback<BaseResponse>() {

                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                        Log.d("sukses url", call.request().url().toString());

                        //jika sukses, edit record kategori di sqlite
                        brand.setSync_update(Constants.STATUS_SUDAH_SYNC);
                        brand.setName(name);
                        brand.setDescription(description);

                        //commit di sqlite
                        brandHelper.updateBrand(brand);

                        iEditResult.onEditSuccess(response.body());
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("gagal url", call.request().url().toString());

                        //jika gagal, edit record kategori di sqlite
                        brand.setSync_update(Constants.STATUS_BELUM_SYNC);
                        brand.setName(name);
                        brand.setDescription(description);

                        //commit di sqlite
                        brandHelper.updateBrand(brand);

                        iEditResult.onEditError(t.getMessage());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada internet
                //jika gagal, edit record kategori di sqlite
                brand.setSync_update(Constants.STATUS_BELUM_SYNC);
                brand.setName(name);
                brand.setDescription(description);

                //commit di sqlite
                brandHelper.updateBrand(brand);

                iEditResult.onEditError("");
            }



        }



    }
}
