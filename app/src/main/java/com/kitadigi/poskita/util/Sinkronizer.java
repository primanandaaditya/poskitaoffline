package com.kitadigi.poskita.util;

import android.content.Context;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.brand.BrandController;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.fragment.item.BarangController;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.IBarangResult;
import com.kitadigi.poskita.fragment.kategori.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.KategoriController;
import com.kitadigi.poskita.fragment.kategori.KategoriModel;
import com.kitadigi.poskita.fragment.pos.IStokResult;
import com.kitadigi.poskita.fragment.pos.StokController;
import com.kitadigi.poskita.fragment.pos.StokModel;
import com.kitadigi.poskita.fragment.unit.IUnitResult;
import com.kitadigi.poskita.fragment.unit.UnitController;
import com.kitadigi.poskita.fragment.unit.UnitModel;
import com.kitadigi.poskita.sinkron.beli.insert.ISinkronAddBeliResult;
import com.kitadigi.poskita.sinkron.beli.insert.SinkronInsertBeliController;
import com.kitadigi.poskita.sinkron.brand.delete.ISinkronDeleteBrandResult;
import com.kitadigi.poskita.sinkron.brand.delete.SinkronDeleteBrandController;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandResult;
import com.kitadigi.poskita.sinkron.brand.insert.SinkronInsertBrandController;
import com.kitadigi.poskita.sinkron.brand.update.ISinkronUpdateBrandResult;
import com.kitadigi.poskita.sinkron.brand.update.SinkronUpdateBrandController;
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

import java.util.List;
import java.util.TooManyListenersException;

public class Sinkronizer implements
        ISinkronAddKategoriResult, ISinkronUpdateKategoriResult, ISinkronDeleteKategoriResult,
        ISinkronAddBrandResult, ISinkronUpdateBrandResult, ISinkronDeleteBrandResult,
        ISinkronAddUnitResult, ISinkronUpdateUnitResult, ISinkronDeleteUnitResult,
        ISinkronAddProdukResult, ISinkronUpdateProdukResult, ISinkronDeleteProdukResult,
        ISinkronAddJualResult, ISinkronAddBeliResult,
        IKategoriResult, IBrandResult, IUnitResult, IBarangResult,
        IStokResult, IGetJualMasterResult {

    //cek internet koneksi
    InternetChecker internetChecker;

    Context context;
    ISinkronizer iSinkronizer;

    //init session, untuk menampilkan tanggal last sync
    SessionManager sessionManager;

    //init controller kategori
    SinkronInsertKategoriController sinkronInsertKategoriController;
    SinkronUpdateKategoriController sinkronUpdateKategoriController;
    SinkronDeleteKategoriController sinkronDeleteKategoriController;
    KategoriController kategoriController;

    //init controller brand
    SinkronInsertBrandController sinkronInsertBrandController;
    SinkronUpdateBrandController sinkronUpdateBrandController;
    SinkronDeleteBrandController sinkronDeleteBrandController;
    BrandController brandController;

    //init controller unit
    SinkronInsertUnitController sinkronInsertUnitController;
    SinkronUpdateUnitController sinkronUpdateUnitController;
    SinkronDeleteUnitController sinkronDeleteUnitController;
    UnitController unitController;

    //init produk controller
    SinkronInsertProdukController sinkronInsertProdukController;
    SinkronUpdateProdukController sinkronUpdateProdukController;
    SinkronDeleteProdukController sinkronDeleteProdukController;
    BarangController barangController;

    //init penjualan controller
    SinkronInsertJualController sinkronInsertJualController;

    //init pembelian controller
    SinkronInsertBeliController sinkronInsertBeliController;

    //init controller untuk stok atau untuk layar POS/jualfragment.java
    StokController stokController;

    //init controller untuk get jual master
    GetJualMasterController getJualMasterController;

    public Sinkronizer(Context context, ISinkronizer iSinkronizer) {
        this.context = context;
        this.iSinkronizer = iSinkronizer;

        //interface menyatakan mulai
        iSinkronizer.onBegin();
    }

    public void doSinkron(){

        //cek apakah ada internet atau tidak?
        internetChecker = new InternetChecker();
        if (internetChecker.haveNetwork(context)){

            //jika ada internet, langsungkan sinkron

            //interface menyatakan sedang dalam progress
            iSinkronizer.onProgress();

            //init session, untuk menampilkan tanggal di tvLastSync
            sessionManager=new SessionManager(context);

            //init controller
            sinkronInsertKategoriController =new SinkronInsertKategoriController(context,this);
            sinkronUpdateKategoriController =new SinkronUpdateKategoriController(context,this);
            sinkronDeleteKategoriController =new SinkronDeleteKategoriController(context,this);

            sinkronInsertBrandController    = new SinkronInsertBrandController(context,this);
            sinkronUpdateBrandController    = new SinkronUpdateBrandController(context,this);
            sinkronDeleteBrandController    = new SinkronDeleteBrandController(context,this);

            sinkronInsertUnitController     = new SinkronInsertUnitController(context,this);
            sinkronUpdateUnitController     = new SinkronUpdateUnitController(context,this);
            sinkronDeleteUnitController     = new SinkronDeleteUnitController(context,this);

            sinkronInsertProdukController   = new SinkronInsertProdukController(context,this);
            sinkronUpdateProdukController   = new SinkronUpdateProdukController(context, this);
            sinkronDeleteProdukController   = new SinkronDeleteProdukController(context, this);

            sinkronInsertJualController     = new SinkronInsertJualController(context,this);
            sinkronInsertBeliController     = new SinkronInsertBeliController(context, this);

            kategoriController              = new KategoriController(context,this);
            brandController                 = new BrandController(context, this);
            unitController                  = new UnitController(context, this);
            barangController                = new BarangController(this,context);

            stokController                  = new StokController(this, context);

            getJualMasterController         = new GetJualMasterController(context, this);

            //mulai sinkron
            sinkronInsertKategoriController.insert_kategori();

        }else{

            //jika tidak ada internet, loncat ke interface onNoInternet()
            iSinkronizer.onNoInternet();
        }

    }
    @Override
    public void onSinkronAddKategoriSuccess(SinkronResponse sinkronResponse) {

        //lanjutkan ke sinkron update
        sinkronUpdateKategoriController.update_kategori();

    }

    @Override
    public void onSinkronAddKategoriError(String error) {
//        this.showToast(error);
//        sinkronUpdateKategoriController.update_kategori();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateKategoriSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan delete
        sinkronDeleteKategoriController.delete_kategori();
    }

    @Override
    public void onSinkronUpdateKategoriError(String error) {
        //lanjutkan delete
//        sinkronDeleteKategoriController.delete_kategori();
        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);

    }

    @Override
    public void onSinkronDeleteKategoriSuccess(SinkronResponse sinkronResponse) {
        sinkronInsertBrandController.insert_brand();

    }

    @Override
    public void onSinkronDeleteKategoriError(String error) {

//        sinkronInsertBrandController.insert_brand();
        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddBrandSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sinkron update brand
        sinkronUpdateBrandController.update_brand();
    }

    @Override
    public void onSinkronAddBrandError(String error) {
        //lanjutkan ke sinkron update brand
//        sinkronUpdateBrandController.update_brand();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateBrandSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sinkron delete brand
        sinkronDeleteBrandController.delete_brand();
    }

    @Override
    public void onSinkronUpdateBrandError(String error) {
        //lanjutkan ke sinkron delete brand
//        sinkronDeleteBrandController.delete_brand();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteBrandSuccess(SinkronResponse sinkronResponse) {

        //lanjut ke sync insert unit
        sinkronInsertUnitController.insert_unit();
    }

    @Override
    public void onSinkronDeleteBrandError(String error) {
        //lanjut ke sync insert unit
//        sinkronInsertUnitController.insert_unit();
        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync update unit
        sinkronUpdateUnitController.update_unit();
    }

    @Override
    public void onSinkronAddUnitError(String error) {
//lanjut ke sync update unit
//        sinkronUpdateUnitController.update_unit();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync delete unit
        sinkronDeleteUnitController.delete_unit();
    }

    @Override
    public void onSinkronUpdateUnitError(String error) {
        //lanjut ke sync delete unit
//        sinkronDeleteUnitController.delete_unit();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync insert produk
        sinkronInsertProdukController.insert_produk();
    }

    @Override
    public void onSinkronDeleteUnitError(String error) {
        //lanjut ke sync insert produk
//        sinkronInsertProdukController.insert_produk();
        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync update produk
        sinkronUpdateProdukController.update_produk();
    }

    @Override
    public void onSinkronAddProdukError(String error) {
        //lanjut ke sync update produk
//        sinkronUpdateProdukController.update_produk();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronUpdateProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync delete produk
        sinkronDeleteProdukController.delete_produk();
    }

    @Override
    public void onSinkronUpdateProdukError(String error) {
        //lanjut ke sync delete produk
//        sinkronDeleteProdukController.delete_produk();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronDeleteProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync jual
        sinkronInsertJualController.insert_jual();
    }

    @Override
    public void onSinkronDeleteProdukError(String error) {
        //lanjut ke sync jual
//        sinkronInsertJualController.insert_jual();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddJualSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sync pembelian
        sinkronInsertBeliController.insert_beli();
    }

    @Override
    public void onSinkronAddJualError(String error) {
        //lanjutkan ke sync pembelian
//        sinkronInsertBeliController.insert_beli();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSinkronAddBeliSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sinkron select kategori
        kategoriController.getKategoriList();
    }

    @Override
    public void onSinkronAddBeliError(String error) {
        //lanjut ke get kategori
//        kategoriController.getKategoriList();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline) {
        //lanjut ke get brand
        brandController.getBrandList();
    }

    @Override
    public void onKategoriError(String error, List<Kategori> kategoriOffline) {
        //lanjut ke get brand
//        brandController.getBrandList();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline) {
        //lanjut ke get unit
        unitController.getUnitList();
    }

    @Override
    public void onBrandError(String error, List<Brand> brandOffline) {
        //lanjut ke get unit
//        unitController.getUnitList();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {
        //lanjut ke sync stok
        stokController.getStok();
    }

    @Override
    public void onError(String error, List<Item> items) {
        //lanjut ke sync stok
//        stokController.getStok();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onUnitSuccess(UnitModel unitModel, List<Unit> units) {
//lanjut ke get produk
        barangController.getBarang();
    }

    @Override
    public void onUnitError(String error, List<Unit> units) {
        //lanjut ke get produk
//        barangController.getBarang();

        //kalau sinkron ini gagal, langsung abort!!
        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onStokSuccess(StokModel stokModel, List<Stok> stokOffline) {

        //simpan tanggal sekarang di session
//        sessionManager.createLasySync();
//        Toast.makeText(context, "Sinkron OK", Toast.LENGTH_SHORT).show();

        //interface sudah selesai
//        iSinkronizer.onFinish(context.getResources().getString(R.string.proses_sinkron_telah_selesai));

        //lanjut ke get jual master
        getJualMasterController.getJualMaster();
    }

    @Override
    public void onStokError(String error, List<Stok> stoksOffline) {

        //simpan tanggal di session
//        sessionManager.createLasySync();

        //interface sudah selesai
        iSinkronizer.onFinish(error);
    }

    @Override
    public void onGetJualMasterSuccess(MasterModel masterModel) {
        if (masterModel.isStatus()){

        }
    }

    @Override
    public void onGetJualMasterError(String error) {

        iSinkronizer.onFinish(error);
    }
}
