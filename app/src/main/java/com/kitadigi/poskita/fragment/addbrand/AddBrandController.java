package com.kitadigi.poskita.fragment.addbrand;

import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBrandController implements IAddBrandRequest {

    Context context;
    SweetAlertDialog sweetAlertDialog;
    IAddBrand iAddBrand;
    IAddBrandResult iAddBrandResult;
    SessionManager sessionManager;
    String enkripIdUsers;

    BrandHelper brandHelper;
    InternetChecker internetChecker;
    String bussinessId;

    boolean offlineMode;

    public AddBrandController(Context context, IAddBrandResult iAddBrandResult) {
        this.context = context;
        this.iAddBrandResult = iAddBrandResult;

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        bussinessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();
    }

    public AddBrandController(Context context, IAddBrandResult iAddBrandResult, boolean offlineMode) {
        this.context = context;
        this.iAddBrandResult = iAddBrandResult;
        this.offlineMode = offlineMode;

        brandHelper=new BrandHelper(context);
        internetChecker=new InternetChecker();

        sessionManager=new SessionManager(context);
        enkripIdUsers=sessionManager.getEncryptedIdUsers();

        bussinessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();

    }

    @Override
    public void addBrand(final String name, final String description) {


        if (offlineMode){
            simpanOffline(name, description);
        }else{

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){


                sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                iAddBrand=AddBrandUtil.getInterface();
                iAddBrand.addBrand(enkripIdUsers,name,description)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                Log.d("brand sukses", call.request().url().toString());

                                //jika sukses nembak API, simpan di sqlite
                                //buat var brand
                                //status sync insert = sudah sync
                                Brand brand = new Brand();
                                brand.setName(name);
                                brand.setDescription(description);
                                brand.setKode_id(bussinessId);
                                brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                brand.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                                brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                brandHelper.addBrand(brand);

                                iAddBrandResult.onBrandSuccess(response.body());
                                sweetAlertDialog.dismissWithAnimation();
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.d("brand fail", call.request().url().toString());


                                //jika gagal nembak API, simpan di sqlite
                                //buat var brand
                                //status sync insert = belum sync
                                simpanOffline(name, description);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

            }else{
                //jika tidak ada koneksi internet
                //buat var brand
                //status sync insert = belum sync
                simpanOffline(name, description);
            }
        }
    }

    void simpanOffline(String nama,String deskripsi){

        //cek duplikasi
        boolean adaRow = brandHelper.cekNamaBrand(nama);

        //jika tidak ada duplikasi
        if (adaRow==false){

            Brand brand = new Brand();
            brand.setName(nama);
            brand.setDescription(deskripsi);
            brand.setKode_id(bussinessId);
            brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
            brand.setSync_insert(Constants.STATUS_BELUM_SYNC);
            brand.setSync_delete(Constants.STATUS_SUDAH_SYNC);
            brandHelper.addBrand(brand);

            iAddBrandResult.onBrandError(context.getResources().getString(R.string.tersimpan_offline));
        }else{
            //jika ada duplikasi
            iAddBrandResult.onBrandError(context.getResources().getString(R.string.duplikasi_brand));
        }



    }

}
