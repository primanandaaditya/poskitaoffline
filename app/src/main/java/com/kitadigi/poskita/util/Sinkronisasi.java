package com.kitadigi.poskita.util;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.brand.BrandData;
import com.kitadigi.poskita.fragment.brand.dengan_header.BrandController;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.Datum;
import com.kitadigi.poskita.fragment.item.IBarangResult;
import com.kitadigi.poskita.fragment.item.dengan_header.BarangController;
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategori;
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriController;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriModel;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.fragment.unit.dengan_header.IUnitResult;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitController;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitModel;
import com.kitadigi.poskita.sinkron.beli.get_detail.GetBeliDetailController;
import com.kitadigi.poskita.sinkron.beli.get_detail.GetBeliDetailModel;
import com.kitadigi.poskita.sinkron.beli.get_detail.IGetBeliDetailResult;
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterController;
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterModel;
import com.kitadigi.poskita.sinkron.beli.get_master.IGetBeliMasterResult;
import com.kitadigi.poskita.sinkron.beli.insert.ISinkronAddBeliResult;
import com.kitadigi.poskita.sinkron.beli.insert.SinkronInsertBeliController;
import com.kitadigi.poskita.sinkron.brand.delete.ISinkronDeleteBrandResult;
import com.kitadigi.poskita.sinkron.brand.delete.SinkronDeleteBrandController;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandResult;
import com.kitadigi.poskita.sinkron.brand.insert.SinkronInsertBrandController;
import com.kitadigi.poskita.sinkron.brand.update.ISinkronUpdateBrandResult;
import com.kitadigi.poskita.sinkron.brand.update.SinkronUpdateBrandController;
import com.kitadigi.poskita.sinkron.jual.get_detail.GetDetailModel;
import com.kitadigi.poskita.sinkron.jual.get_detail.GetJualDetailController;
import com.kitadigi.poskita.sinkron.jual.get_detail.IGetJualDetailResult;
import com.kitadigi.poskita.sinkron.jual.get_master.GetJualMasterController;
import com.kitadigi.poskita.sinkron.jual.get_master.IGetJualMasterResult;
import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;
import com.kitadigi.poskita.sinkron.jual.insert.ISinkronAddJualResult;
import com.kitadigi.poskita.sinkron.jual.insert.SinkronInsertJualController;
import com.kitadigi.poskita.sinkron.kategori.delete.ISinkronDeleteKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.delete.SinkronDeleteKategoriController;
import com.kitadigi.poskita.sinkron.kategori.insert.ISinkronAddKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.insert.SinkronInsertKategoriController;
import com.kitadigi.poskita.sinkron.kategori.update.ISinkronUpdateKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.update.SinkronUpdateKategoriController;
import com.kitadigi.poskita.sinkron.produk.delete.ISinkronDeleteProdukResult;
import com.kitadigi.poskita.sinkron.produk.delete.SinkronDeleteProdukController;
import com.kitadigi.poskita.sinkron.produk.insert.ISinkronAddProdukResult;
import com.kitadigi.poskita.sinkron.produk.insert.SinkronInsertProdukController;
import com.kitadigi.poskita.sinkron.produk.update.ISinkronUpdateProdukResult;
import com.kitadigi.poskita.sinkron.produk.update.SinkronUpdateProdukController;
import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnitResult;
import com.kitadigi.poskita.sinkron.unit.delete.SinkronDeleteUnitController;
import com.kitadigi.poskita.sinkron.unit.insert.ISinkronAddUnitResult;
import com.kitadigi.poskita.sinkron.unit.insert.SinkronInsertUnitController;
import com.kitadigi.poskita.sinkron.unit.update.ISinkronUpdateUnitResult;
import com.kitadigi.poskita.sinkron.unit.update.SinkronUpdateUnitController;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Sinkronisasi implements
        ISinkronAddKategoriResult, ISinkronUpdateKategoriResult, ISinkronDeleteKategoriResult,
        ISinkronAddBrandResult, ISinkronUpdateBrandResult, ISinkronDeleteBrandResult,
        ISinkronAddUnitResult, ISinkronUpdateUnitResult, ISinkronDeleteUnitResult,
        ISinkronAddProdukResult, ISinkronUpdateProdukResult, ISinkronDeleteProdukResult,
        ISinkronAddJualResult, ISinkronAddBeliResult,
        IKategoriResult, IBrandResult, IUnitResult, IBarangResult,
        IGetJualMasterResult, IGetJualDetailResult, IGetBeliMasterResult, IGetBeliDetailResult {


    //sinkronisasi upload (insert,update,delete)
    SinkronInsertKategoriController sinkronInsertKategoriController;
    SinkronUpdateKategoriController sinkronUpdateKategoriController;
    SinkronDeleteKategoriController sinkronDeleteKategoriController;
    SinkronInsertBrandController sinkronInsertBrandController;
    SinkronUpdateBrandController sinkronUpdateBrandController;
    SinkronDeleteBrandController sinkronDeleteBrandController;
    SinkronInsertUnitController sinkronInsertUnitController;
    SinkronUpdateUnitController sinkronUpdateUnitController;
    SinkronDeleteUnitController sinkronDeleteUnitController;
    SinkronInsertProdukController sinkronInsertProdukController;
    SinkronUpdateProdukController sinkronUpdateProdukController;
    SinkronDeleteProdukController sinkronDeleteProdukController;
    SinkronInsertJualController sinkronInsertJualController;
    SinkronInsertBeliController sinkronInsertBeliController;
    GetJualMasterController getJualMasterController;
    GetJualDetailController getJualDetailController;
    GetBeliMasterController getBeliMasterController;
    GetBeliDetailController getBeliDetailController;


    //sinkronisasi download/get
    KategoriController kategoriController;
    BrandController brandController;
    UnitController unitController;
    BarangController barangController;

    
    Context context;

    //var ini untuk progress sejauh mana proses sync berjalan
    //nilainya akan dinaikkan 1 setiap kali sub sync selesai (entah sukses/gagal)
    Integer progress = 0;

    ISinkronizer iSinkronizer;

    InternetChecker internetChecker;

    //session untuk mencatat lasy sync /disimpan di sqlite
    SessionManager sessionManager;




    public Sinkronisasi(Context context, ISinkronizer iSinkronizer) {
        this.context = context;
        this.iSinkronizer = iSinkronizer;
    }


    public Sinkronisasi(Context context, Integer progress, ISinkronizer iSinkronizer) {
        this.context = context;
        this.progress = progress;
        this.iSinkronizer = iSinkronizer;
    }



    public void mulaiSinkron(){

        //cek dulu apakah ada koneksi internet
        internetChecker = new InternetChecker();

        if (internetChecker.haveNetwork(context)){

            //init session manager
            sessionManager = new SessionManager(context);

            //mulai sync dari insert kategori dulu
            sinkronInsertKategoriController=new SinkronInsertKategoriController(context, this);
            sinkronInsertKategoriController.insert_kategori();

        }else {
            iSinkronizer.onNoInternet();
        }


    }


    @Override
    public void onSinkronAddKategoriSuccess(SinkronResponse sinkronResponse) {

        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            //lanjut ke update kategori
            sinkronUpdateKategoriController=new SinkronUpdateKategoriController(context, this);
            sinkronUpdateKategoriController.update_kategori();
        }
    }

    @Override
    public void onSinkronAddKategoriError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateKategoriSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteKategoriController = new SinkronDeleteKategoriController(context, this);
            sinkronDeleteKategoriController.delete_kategori();
        }
    }

    @Override
    public void onSinkronUpdateKategoriError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteKategoriSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        iSinkronizer.onProgress(progress);
        Log.d("Progress : " ,progress.toString());

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
            sinkronInsertBrandController = new SinkronInsertBrandController(context,this);
            sinkronInsertBrandController.insert_brand();
        }
    }

    @Override
    public void onSinkronDeleteKategoriError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddBrandSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronUpdateBrandController = new SinkronUpdateBrandController(context, this);
            sinkronUpdateBrandController.update_brand();
        }
    }

    @Override
    public void onSinkronAddBrandError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateBrandSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteBrandController =new SinkronDeleteBrandController(context, this);
            sinkronDeleteBrandController.delete_brand();
        }
    }

    @Override
    public void onSinkronUpdateBrandError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteBrandSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronInsertUnitController = new SinkronInsertUnitController(context, this);
            sinkronInsertUnitController.insert_unit();
        }
    }

    @Override
    public void onSinkronDeleteBrandError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddUnitSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronUpdateUnitController = new SinkronUpdateUnitController(context, this);
            sinkronUpdateUnitController.update_unit();

        }
    }

    @Override
    public void onSinkronAddUnitError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateUnitSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteUnitController = new SinkronDeleteUnitController(context, this);
            sinkronDeleteUnitController.delete_unit();
        }
    }

    @Override
    public void onSinkronUpdateUnitError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteUnitSuccess(SinkronResponse sinkronResponse) {

        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronInsertProdukController = new SinkronInsertProdukController(context, this);
            sinkronInsertProdukController.insert_produk();
        }
    }

    @Override
    public void onSinkronDeleteUnitError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddProdukSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronUpdateProdukController = new SinkronUpdateProdukController(context,this);
            sinkronUpdateProdukController.update_produk();
        }
    }

    @Override
    public void onSinkronAddProdukError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateProdukSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronDeleteProdukController =new SinkronDeleteProdukController(context, this);
            sinkronDeleteProdukController.delete_produk();
        }
    }

    @Override
    public void onSinkronUpdateProdukError(String error) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteProdukSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronInsertJualController = new SinkronInsertJualController(context, this);
            sinkronInsertJualController.insert_jual();
        }
    }

    @Override
    public void onSinkronDeleteProdukError(String error) {
        iSinkronizer.onFinish(error);
    }


    @Override
    public void onSinkronAddJualSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){

            sinkronInsertBeliController = new SinkronInsertBeliController(context,this);
            sinkronInsertBeliController.insert_beli();
//            this.showToast(sinkronResponse.getStatus().getMessage());
        }
    }

    @Override
    public void onSinkronAddJualError(String error) {
        iSinkronizer.onFinish(error);
        Log.d("sinkron add jual error", "Sinkron add jual error");
    }

    @Override
    public void onSinkronAddBeliSuccess(SinkronResponse sinkronResponse) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){


            kategoriController = new KategoriController(context, this);
            kategoriController.getKategoriList();
//            this.showToast(sinkronResponse.getStatus().getMessage());
        }
    }

    @Override
    public void onSinkronAddBeliError(String error)
    {
        iSinkronizer.onFinish(error);
    }


    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        brandController = new BrandController(context, this);
        brandController.getBrandList();
    }

    @Override
    public void onKategoriError(String error, List<Kategori> kategoriOffline) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        unitController = new UnitController(context, this);
        unitController.getUnitList();
    }

    @Override
    public void onBrandError(String error, List<Brand> brandOffline) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onUnitSuccess(UnitModel unitModel, List<Unit> units) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        barangController = new BarangController(this,context);
        barangController.getBarang();
    }

    @Override
    public void onUnitError(String error, List<Unit> units) {
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {

        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        getJualMasterController = new GetJualMasterController(context, this);
        getJualMasterController.getJualMaster();
    }

    @Override
    public void onError(String error, List<Item> items) {
        iSinkronizer.onFinish(error);
        Log.d("get barang", "Error");
    }


    @Override
    public void onGetJualMasterSuccess(MasterModel masterModel) {

        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        getBeliMasterController = new GetBeliMasterController(context, this);
        getBeliMasterController.getBeliMaster();


    }

    @Override
    public void onGetJualMasterError(String error) {
        iSinkronizer.onFinish(error);
        Log.d("get jual master","Error");
    }

    @Override
    public void onGetBeliMasterSuccess(GetBeliMasterModel getBeliMasterModel) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        Log.d("get beli master","sukses");
        iSinkronizer.onProgress(progress);

        getJualDetailController = new GetJualDetailController(context,this);
        getJualDetailController.getJualDetail();
    }

    @Override
    public void onGetBeliMasterError(String error) {
        iSinkronizer.onFinish(error);
        Log.d("get beli master","Error");
    }

    @Override
    public void onGetJualDetailSuccess(GetDetailModel getDetailModel) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onProgress(progress);

        getBeliDetailController = new GetBeliDetailController(context, this);
        getBeliDetailController.getBeliDetail();
        Log.d("get jual detail","sukses");
    }


    public void onGetJualDetailError(String error) {
        iSinkronizer.onFinish(error);
        Log.d("get jual detail","Error");
    }

    @Override
    public void onGetBeliDetailSuccess(GetBeliDetailModel getBeliDetailModel) {
        progress ++;
        Log.d("Progress : " ,progress.toString());
        iSinkronizer.onSukses();

        iSinkronizer.onProgress(progress);
        Log.d("get beli detail","Sinkron sukses");
    }

    @Override
    public void onGetBeliDetailError(String error) {
        iSinkronizer.onFinish(error);
        Log.d("get beli detail","Error");
    }



    //getter dan setter untk progress sync
    public Integer getProgress() {
        return progress;
    }
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

}
