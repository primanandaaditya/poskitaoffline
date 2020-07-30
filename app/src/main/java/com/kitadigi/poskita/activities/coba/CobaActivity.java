package com.kitadigi.poskita.activities.coba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriController;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.fragment.unit.dengan_header.IUnitResult;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitController;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitModel;
import com.kitadigi.poskita.sinkron.beli.get_detail.GetBeliDetailController;
import com.kitadigi.poskita.sinkron.beli.get_master.GetBeliMasterController;
import com.kitadigi.poskita.sinkron.beli.insert.SinkronInsertBeliController;
import com.kitadigi.poskita.sinkron.brand.delete.ISinkronDeleteBrandResult;
import com.kitadigi.poskita.sinkron.brand.delete.SinkronDeleteBrandController;
import com.kitadigi.poskita.sinkron.brand.insert.ISinkronAddBrandResult;
import com.kitadigi.poskita.sinkron.brand.insert.SinkronInsertBrandController;
import com.kitadigi.poskita.sinkron.brand.update.ISinkronUpdateBrandResult;
import com.kitadigi.poskita.sinkron.brand.update.SinkronUpdateBrandController;
import com.kitadigi.poskita.sinkron.jual.get_detail.GetJualDetailController;
import com.kitadigi.poskita.sinkron.jual.get_master.GetJualMasterController;
import com.kitadigi.poskita.sinkron.jual.insert.SinkronInsertJualController;
import com.kitadigi.poskita.sinkron.kategori.delete.ISinkronDeleteKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.delete.SinkronDeleteKategoriController;
import com.kitadigi.poskita.sinkron.kategori.insert.ISinkronAddKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.insert.SinkronInsertKategoriController;
import com.kitadigi.poskita.sinkron.kategori.update.ISinkronUpdateKategoriResult;
import com.kitadigi.poskita.sinkron.kategori.update.SinkronUpdateKategoriController;
import com.kitadigi.poskita.sinkron.produk.delete.SinkronDeleteProdukController;
import com.kitadigi.poskita.sinkron.produk.insert.ISinkronAddProdukResult;
import com.kitadigi.poskita.sinkron.produk.insert.SinkronInsertProdukController;
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

import java.util.List;

public class CobaActivity extends BaseActivity implements
        ISinkronAddKategoriResult, ISinkronUpdateKategoriResult, ISinkronDeleteKategoriResult,
        ISinkronAddBrandResult, ISinkronUpdateBrandResult, ISinkronDeleteBrandResult,
        ISinkronAddUnitResult, ISinkronUpdateUnitResult, ISinkronDeleteUnitResult,
        ISinkronAddProdukResult

{

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


    Button button;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);


        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);

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
            this.showToast(sinkronResponse.getStatus().getMessage());
//            sinkronInsertProdukController = new SinkronInsertProdukController(CobaActivity.this, this);
//            sinkronInsertProdukController.insert_produk();
        }
    }

    @Override
    public void onSinkronAddProdukError(String error) {
        this.showToast(error);
    }
}
