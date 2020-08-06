package com.kitadigi.poskita.activities.coba;

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
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterController;
import com.kitadigi.poskita.sinkron.beli.insert.ISinkronAddBeliResult;
import com.kitadigi.poskita.sinkron.beli.insert.SinkronInsertBeliController;
import com.kitadigi.poskita.sinkron.brand.delete.ISinkronDeleteBrandResult;
import com.kitadigi.poskita.sinkron.brand.delete.SinkronDeleteBrandController;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandResult;
import com.kitadigi.poskita.sinkron.brand.insert.SinkronInsertBrandController;
import com.kitadigi.poskita.sinkron.brand.update.ISinkronUpdateBrandResult;
import com.kitadigi.poskita.sinkron.brand.update.SinkronUpdateBrandController;
import com.kitadigi.poskita.sinkron.jual.get_detail.GetJualDetailController;
import com.kitadigi.poskita.sinkron.jual.get_master.GetJualMasterController;
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

public class CobaActivity extends BaseActivity implements
        ISinkronAddKategoriResult, ISinkronUpdateKategoriResult, ISinkronDeleteKategoriResult,
        ISinkronAddBrandResult, ISinkronUpdateBrandResult, ISinkronDeleteBrandResult,
        ISinkronAddUnitResult, ISinkronUpdateUnitResult, ISinkronDeleteUnitResult,
        ISinkronAddProdukResult, ISinkronUpdateProdukResult, ISinkronDeleteProdukResult,
        ISinkronAddJualResult, ISinkronAddBeliResult,
        IKategoriResult, IBrandResult, IUnitResult, IBarangResult


{

    ImageView iv;

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


    Button button;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);


        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        iv=(ImageView)findViewById(R.id.iv);



        Picasso.with(CobaActivity.this)
                .load("http://192.168.1.16:8888/upload_api//master_product/img/1/83/1596440187_cache3KO3ansxT3B5xSBVkLYh.jpg")
                .into(iv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              coba();
            }
        });
    }

    void coba(){
        sinkronInsertKategoriController=new SinkronInsertKategoriController(CobaActivity.this, this);
        sinkronInsertKategoriController.insert_kategori();
    }


    @Override
    public void onSinkronAddKategoriSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            //lanjut ke update kategori
            sinkronUpdateKategoriController=new SinkronUpdateKategoriController(CobaActivity.this, this);
            sinkronUpdateKategoriController.update_kategori();
        }
    }

    @Override
    public void onSinkronAddKategoriError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronUpdateKategoriSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteKategoriController = new SinkronDeleteKategoriController(CobaActivity.this, this);
            sinkronDeleteKategoriController.delete_kategori();
        }
    }

    @Override
    public void onSinkronUpdateKategoriError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronDeleteKategoriSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
           sinkronInsertBrandController = new SinkronInsertBrandController(CobaActivity.this,this);
           sinkronInsertBrandController.insert_brand();
        }
    }

    @Override
    public void onSinkronDeleteKategoriError(String error) {

        this.showToast(error);
    }

    @Override
    public void onSinkronAddBrandSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronUpdateBrandController = new SinkronUpdateBrandController(CobaActivity.this, this);
            sinkronUpdateBrandController.update_brand();
        }
    }

    @Override
    public void onSinkronAddBrandError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronUpdateBrandSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteBrandController =new SinkronDeleteBrandController(CobaActivity.this, this);
            sinkronDeleteBrandController.delete_brand();
        }
    }

    @Override
    public void onSinkronUpdateBrandError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronDeleteBrandSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//           this.showToast(sinkronResponse.getStatus().getMessage());
           sinkronInsertUnitController = new SinkronInsertUnitController(CobaActivity.this, this);
           sinkronInsertUnitController.insert_unit();
        }
    }

    @Override
    public void onSinkronDeleteBrandError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronAddUnitSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
           sinkronUpdateUnitController = new SinkronUpdateUnitController(CobaActivity.this, this);
           sinkronUpdateUnitController.update_unit();

        }
    }

    @Override
    public void onSinkronAddUnitError(String error) {

        this.showToast(error);
    }

    @Override
    public void onSinkronUpdateUnitSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronDeleteUnitController = new SinkronDeleteUnitController(CobaActivity.this, this);
            sinkronDeleteUnitController.delete_unit();
        }
    }

    @Override
    public void onSinkronUpdateUnitError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronDeleteUnitSuccess(SinkronResponse sinkronResponse) {

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());
            sinkronInsertProdukController = new SinkronInsertProdukController(CobaActivity.this, this);
            sinkronInsertProdukController.insert_produk();
        }
    }

    @Override
    public void onSinkronDeleteUnitError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronAddProdukSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronUpdateProdukController = new SinkronUpdateProdukController(CobaActivity.this,this);
            sinkronUpdateProdukController.update_produk();
        }
    }

    @Override
    public void onSinkronAddProdukError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronUpdateProdukSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronDeleteProdukController =new SinkronDeleteProdukController(CobaActivity.this, this);
            sinkronDeleteProdukController.delete_produk();
        }
    }

    @Override
    public void onSinkronUpdateProdukError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronDeleteProdukSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){
//            this.showToast(sinkronResponse.getStatus().getMessage());

            sinkronInsertJualController = new SinkronInsertJualController(CobaActivity.this, this);
            sinkronInsertJualController.insert_jual();
        }
    }

    @Override
    public void onSinkronDeleteProdukError(String error) {
        this.showToast(error);
    }


    @Override
    public void onSinkronAddJualSuccess(SinkronResponse sinkronResponse) {
        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){

            sinkronInsertBeliController = new SinkronInsertBeliController(CobaActivity.this,this);
            sinkronInsertBeliController.insert_beli();
//            this.showToast(sinkronResponse.getStatus().getMessage());
        }
    }

    @Override
    public void onSinkronAddJualError(String error) {
        this.showToast(error);
    }

    @Override
    public void onSinkronAddBeliSuccess(SinkronResponse sinkronResponse) {

        if (sinkronResponse.getStatus().getCode().equals(Constants.KODE_200)){


            kategoriController = new KategoriController(CobaActivity.this, this);
            kategoriController.getKategoriList();
//            this.showToast(sinkronResponse.getStatus().getMessage());
        }
    }

    @Override
    public void onSinkronAddBeliError(String error) {
        this.showToast(error);
    }

    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline) {
       brandController = new BrandController(CobaActivity.this, this);
       brandController.getBrandList();
    }

    @Override
    public void onKategoriError(String error, List<Kategori> kategoriOffline) {
        this.showToast(error);
    }

    @Override
    public void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline) {
        unitController = new UnitController(CobaActivity.this, this);
        unitController.getUnitList();
    }

    @Override
    public void onBrandError(String error, List<Brand> brandOffline) {
        this.showToast(error);
    }

    @Override
    public void onUnitSuccess(UnitModel unitModel, List<Unit> units) {
        barangController = new BarangController(CobaActivity.this, this);
        barangController.getBarang();
    }

    @Override
    public void onUnitError(String error, List<Unit> units) {
        this.showToast(error);
    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {

    }

    @Override
    public void onError(String error, List<Item> items) {

        this.showToast(error);
    }



}
