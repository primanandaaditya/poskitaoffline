package com.kitadigi.poskita.fragment.deletebrand;

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

public class DeleteBrandController implements IDeleteRequest {

    Context context;
    IDeleteBrand iDeleteBrand;
    SweetAlertDialog sweetAlertDialog;
    IDeleteResult iDeleteResult;
    SessionManager sessionManager;
    String enkripIdUser;

    String MD5IdBrand;
    SimpleMD5 simpleMD5;

    BrandHelper brandHelper;
    InternetChecker internetChecker;

    boolean offlineMode;

    public DeleteBrandController(Context context, IDeleteResult iDeleteResult) {
        this.context = context;
        this.iDeleteResult=iDeleteResult;

        simpleMD5=new SimpleMD5();
        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();
    }

    public DeleteBrandController(Context context, IDeleteResult iDeleteResult, boolean offlineMode) {
        this.context = context;
        this.iDeleteResult = iDeleteResult;
        this.offlineMode = offlineMode;

        simpleMD5=new SimpleMD5();
        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();
    }

    @Override
    public void deleteBrand(String enkripIdBrand) {

        //siapkan variabel brand
        //untuk menemukan record brand di sqlite
        //jika sukses, edit record tsb dgn sync_delete=SUKSES
        //jika gagal, edit record tsb dgn sync_delete=GAGAL
        final Brand brand = brandHelper.getBrandById(Long.parseLong(enkripIdBrand));

        //siapkan enkrip buat idbrand
        MD5IdBrand = simpleMD5.generate(enkripIdBrand);


        if (offlineMode){

            //jika gagal, edit record brand di sqlite
            brand.setSync_delete(Constants.STATUS_BELUM_SYNC);
            //commit di sqlite
            brandHelper.updateBrand(brand);
            iDeleteResult.onDeleteError("");

        }else{

            if (internetChecker.haveNetwork(context)){

                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                sessionManager=new SessionManager(context);
                enkripIdUser=sessionManager.getEncryptedIdUsers();

                iDeleteBrand=DeleteBrandUtil.getInterface();
                iDeleteBrand.deleteBrand(MD5IdBrand,enkripIdUser,"kosong").enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.d("sukses url", call.request().url().toString());

                        //jika sukses, edit record kategori di sqlite
                        brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                        //commit di sqlite
                        brandHelper.updateBrand(brand);

                        iDeleteResult.onDeleteSuccess(response.body());
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d("gagal url", call.request().url().toString());

                        //jika gagal, edit record brand di sqlite
                        brand.setSync_delete(Constants.STATUS_BELUM_SYNC);
                        //commit di sqlite
                        brandHelper.updateBrand(brand);

                        iDeleteResult.onDeleteError(t.getMessage());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });


            }else{
                //jika gagal, edit record brand di sqlite
                brand.setSync_delete(Constants.STATUS_BELUM_SYNC);
                //commit di sqlite
                brandHelper.updateBrand(brand);
                iDeleteResult.onDeleteError("");
            }

        }




    }
}
