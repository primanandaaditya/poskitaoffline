package com.kitadigi.poskita.fragment.additem;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBarangController implements IAddBarangRequest {

    IAddBarang iAddBarang;
    IAddBarangResult iAddBarangResult;
    SweetAlertDialog sweetAlertDialog;
    Context context;
    SessionManager sessionManager;
    String encryptedIdUsers;

    //untuk sync
    InternetChecker internetChecker;
    ItemHelper itemHelper;
    String businessId;
    Item item;

    //init sqlite untuk mencari mobile_id dari kategori,brand dan unit
    KategoriHelper kategoriHelper;
    BrandHelper brandHelper;
    UnitHelper unitHelper;

    //var string untuk nampung mobile_id dari query berdasarkan id
    String kategori_mobile_id,brand_mobile_id,unit_mobile_id;


    //var string ini untuk nama produk
    //karena dalam interface retrofitnya diharuskan @Part, bukan @Field
    //jadi string tidak bisa diakses langsung
    //string ini dijadikan konstruktor class ini
    //sehingga bisa disimpan offline
    String nama_produk;

    //var string ini untuk file path foto
    //karena dalam interface retrofitnya diharuskan @Part Multipart.Body, bukan @Field
    //jadi string tidak bisa diakses langsung
    //string ini dijadikan konstruktor class ini
    //sehingga bisa disimpan offline
    String imageFilePath;

    Bitmap bitmap;
    File file,fileCompressed;

    boolean offlineMode;

    public AddBarangController(IAddBarangResult iAddBarangResult, Context context, String nama_produk, String imageFilePath, boolean offlineMode) {
        this.iAddBarangResult = iAddBarangResult;
        this.context = context;
        this.nama_produk = nama_produk;
        this.imageFilePath = imageFilePath;
        this.offlineMode = offlineMode;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        itemHelper=new ItemHelper(context);
        internetChecker=new InternetChecker();
        businessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();

    }

    public AddBarangController(IAddBarangResult iAddBarangResult, Context context, String nama_produk, String imageFilePath) {
        this.iAddBarangResult = iAddBarangResult;
        this.context = context;
        this.nama_produk=nama_produk;
        this.imageFilePath=imageFilePath;

        sessionManager=new SessionManager(context);
        encryptedIdUsers=sessionManager.getEncryptedIdUsers();

        itemHelper=new ItemHelper(context);
        internetChecker=new InternetChecker();
        businessId = sessionManager.getLeadingZeroBussinessId() + StringUtil.timeMilis();

    }

    @Override
    public void addBarang(final MultipartBody.Part image,
                          final RequestBody name_product, final Integer brand_id,
                          final Integer category_id, final Integer unit_id,
                          final Integer purchase_price, final Integer sell_price,
                          final String code_product, final Integer qty_stock,
                          final Integer qty_minimum, final String category_mobile_id,
                          final String brand_mobile_id,
                          final String unit_mobile_id) {


        if (offlineMode){

            //jika mode offline
            //simpan offline
            simpanOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum, category_mobile_id,brand_mobile_id,unit_mobile_id);

        }else{


            //cek koneksi internet
            if (internetChecker.haveNetwork(context)){
                //jika ada koneksi internet
                sweetAlertDialog =new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                iAddBarang=AddBarangUtil.getInterface();

                iAddBarang.addBarang(
                        image,
                        encryptedIdUsers,
                        name_product,
                        brand_id,
                        category_id,
                        unit_id,
                        purchase_price,
                        sell_price,
                        code_product,
                        qty_stock,
                        qty_minimum
                ).enqueue(new Callback<AddBarangResult>() {
                    @Override
                    public void onResponse(Call<AddBarangResult> call, Response<AddBarangResult> response) {
                        iAddBarangResult.onSuccess(response.body());
                        Log.d("url add product", call.request().url().toString());

                        //jika sukses nembak API, simpan di sqlite
                        //buat var brand
                        //status sync insert = sudah sync
                        item=new Item();
                        item.setKode_id(businessId);
                        item.setName_product(nama_produk);
                        item.setCode_product(code_product);
                        item.setBrand_id(brand_id);
                        item.setCategory_id(category_id);
                        item.setUnit_id(unit_id);

                        item.setPurchase_price(purchase_price);
                        item.setSell_price(sell_price);
                        item.setQty_stock(qty_stock);
                        item.setQty_minimum(qty_minimum);
                        item.setTypes("");
                        item.setCategory_name("");
                        item.setBrand_name("");
                        item.setUnit_name("");
                        item.setSync_update(Constants.STATUS_SUDAH_SYNC);
                        item.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                        item.setSync_delete(Constants.STATUS_SUDAH_SYNC);

                        //simpan foto barang di hp user
                        //cek dulu apakah user ambil foto dari kamera/galeri atau tidak
                        //jika imageFilePath kosong/null, berarti nyimpen item tanpa gambar
                        if (imageFilePath.matches("") || imageFilePath==null){
                            item.setImage("");
                        }else{
                            //simpan file gambar di item-nya sqlite
                            item.setImage(imageFilePath);

                        }



                        //commit di sqlite
                        itemHelper.addItem(item);

                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onFailure(Call<AddBarangResult> call, Throwable t) {
                        iAddBarangResult.onError(t.getMessage());
                        Log.d("upload foto error", t.getMessage().toString());
                        Log.d("url add product error", call.request().url().toString());

                        //jika gagal nembak API, simpan di sqlite
                        //buat var brand
                        //status sync insert = belum sync
                        simpanOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum, category_mobile_id,brand_mobile_id,unit_mobile_id);

                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }else{
                //jika tidak ada koneksi internet

                //jika gagal nembak API, simpan di sqlite
                //buat var brand
                //status sync insert = belum sync
                simpanOffline(code_product, brand_id, category_id, unit_id, purchase_price, sell_price, qty_stock, qty_minimum, category_mobile_id,brand_mobile_id,unit_mobile_id);
            }
        }
    }

    void simpanOffline(String code_product,
                       Integer brand_id,
                       Integer category_id,
                       Integer unit_id,
                       Integer purchase_price,
                       Integer sell_price,
                       Integer qty_stock,
                       Integer qty_minimum,
                       String category_mobile_id,
                       String brand_mobile_id,
                       String unit_mobile_id
    )
    {


        //cek duplikasi
        boolean adaRow = itemHelper.cekNamaItem(nama_produk);

        //jika tidak ada duplikasi
        if (adaRow==false){

            //yang harus diperhatikan:
            //pada tabel item di sqlite ada kolom category_mobile_id,unit_mobile_id dan brand_mobile_id
            //ketiga kolom tersebut harus diisi dengan mobile_id dari kategori,unit dan brand
            //karena saat insert data produk, dropdown yang ada hanya diketahui id-nya saja
            //id-id tersebut berasal dari ketiga spinner yang ada di activity ItemsDataActivity.java
            //jadi kita harus mencari mobile_id lewat query

            //jalankan fungsi cariMobileId
            //dan lemparkan tiap nilai
            // ke variabel kategori_mobile_id,brand_mobile_id,unit_mobile_id
            cariMobileId(brand_id,category_id,unit_id);

            item=new Item();
//            item.setKode_id(businessId);
            item.setKode_id(StringUtil.getRandomString(Constants.randomString));
            item.setName_product(nama_produk);
            item.setCode_product(code_product);
            item.setBrand_id(brand_id);
            item.setCategory_id(category_id);
            item.setUnit_id(unit_id);
            item.setBrand_mobile_id(brand_mobile_id);
            item.setCategory_mobile_id(category_mobile_id);
            item.setUnit_mobile_id(unit_mobile_id);
            item.setPurchase_price(purchase_price);
            item.setSell_price(sell_price);
            item.setQty_stock(qty_stock);
            item.setQty_minimum(qty_minimum);
            item.setTypes("");
            item.setCategory_name("");
            item.setBrand_name("");
            item.setUnit_name("");
            item.setSync_update(Constants.STATUS_SUDAH_SYNC);
            item.setSync_insert(Constants.STATUS_BELUM_SYNC);
            item.setSync_delete(Constants.STATUS_SUDAH_SYNC);

            //simpan foto barang di hp user
            //cek dulu apakah user ambil foto dari kamera/galeri atau tidak
            //jika imageFilePath kosong/null, berarti nyimpen item tanpa gambar
            if (imageFilePath.matches("") || imageFilePath==null){
                item.setImage("");
            }else{
                //simpan nama gambar di item-nya sqlite
                item.setImage(imageFilePath);
            }

            //commit disqlite
            itemHelper.addItem(item);
            iAddBarangResult.onError(context.getResources().getString(R.string.tersimpan_offline));

        }else{
            //jika ada duplikasi
            iAddBarangResult.onError(context.getResources().getString(R.string.duplikasi_item));
        }

    }


    //fungsi ini untuk mencari mobile_id
    //dari kategori,brand dan unit
    //lalu dilempar ke variabel
    void cariMobileId(Integer brand_id,Integer category_id,Integer unit_id){

        //init sqlite dulu
        kategoriHelper = new KategoriHelper(context);
        brandHelper = new BrandHelper(context);
        unitHelper = new UnitHelper(context);

        //cari dulu mobile_id di tiap tabel
        Kategori kategori = kategoriHelper.getKategoriById(Long.parseLong(category_id.toString()));
        kategori_mobile_id = kategori.getKode_id();

        Brand brand = brandHelper.getBrandById(Long.parseLong(brand_id.toString()));
        brand_mobile_id = brand.getKode_id();

        Unit unit = unitHelper.getUnitById(Long.parseLong(unit_id.toString()));
        unit_mobile_id = unit.getKode_id();

    }

}
