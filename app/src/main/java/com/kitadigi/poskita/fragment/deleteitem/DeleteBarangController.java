package com.kitadigi.poskita.fragment.deleteitem;

import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.fragment.edititem.IEditResult;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeleteBarangController implements IDeleteBarangRequest {
    Context context;
    SweetAlertDialog sweetAlertDialog;
    IDeleteBarang iDeleteBarang;
    IEditResult iEditResult;
    SessionManager sessionManager;
    String encryptedIdUsers;


    //untuk keperluan sync
    String IdItemMD5;
    SimpleMD5 simpleMD5;
    Item item;
    ItemHelper itemHelper;
    InternetChecker internetChecker;

    boolean offlineMode;

    public DeleteBarangController(Context context, IEditResult iEditResult) {
        this.context = context;
        this.iEditResult = iEditResult;
        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        simpleMD5=new SimpleMD5();
        itemHelper=new ItemHelper(context);
        internetChecker=new InternetChecker();
    }

    public DeleteBarangController(Context context, IEditResult iEditResult, boolean offlineMode) {
        this.context = context;
        this.iEditResult = iEditResult;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        simpleMD5=new SimpleMD5();
        itemHelper=new ItemHelper(context);
        internetChecker=new InternetChecker();
    }

    @Override
    public void deleteBarang(String encryptedIdBarang) {


        //siapkan string enkripsi untuk id produk
        IdItemMD5 = simpleMD5.generate(encryptedIdBarang);

        //siapkan record item yang dicari dengan id
        item=itemHelper.getItemById(Long.parseLong(encryptedIdBarang));

        if (offlineMode){

            //jika tiada koneksi internet, edit record item di sqlite
            item.setSync_delete(Constants.STATUS_BELUM_SYNC);
            //commit di sqlite
            itemHelper.updateItem(item);

        }else{

            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada koneksi internet
                sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                iDeleteBarang=DeleteBarangUtil.getInterface();
                iDeleteBarang.deleteBarang(IdItemMD5,encryptedIdUsers,"kosong").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<AddBarangResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                                iEditResult.onError(e.getMessage());

                                //jika gagal, edit record item di sqlite
                                item.setSync_delete(Constants.STATUS_BELUM_SYNC);
                                //commit di sqlite
                                itemHelper.updateItem(item);

                                sweetAlertDialog.dismissWithAnimation();
                            }

                            @Override
                            public void onNext(AddBarangResult addBarangResult) {

                                //jika sukses, edit record item di sqlite
                                item.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                                //commit di sqlite
                                itemHelper.updateItem(item);

                                iEditResult.onSuccess(addBarangResult);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                ;
            }else{

                //jika tiada koneksi internet, edit record item di sqlite
                item.setSync_delete(Constants.STATUS_BELUM_SYNC);
                //commit di sqlite
                itemHelper.updateItem(item);

            }


        }




    }
}
