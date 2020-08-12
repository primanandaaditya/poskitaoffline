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
import com.kitadigi.poskita.util.ISinkronizer;
import com.kitadigi.poskita.util.Sinkronisasi;
import com.kitadigi.poskita.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CobaActivity extends BaseActivity implements ISinkronizer

{

    ImageView iv;

    Sinkronisasi sinkronisasi;
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
        sinkronisasi = new Sinkronisasi(CobaActivity.this, this);
        sinkronisasi.mulaiSinkron();
    }


    @Override
    public void onNoInternet() {
        this.showToast(getResources().getString(R.string.no_internet));
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onProgress(Integer progress) {

    }

    @Override
    public void onFinish(String pesan) {
        this.showToast(pesan);
    }

    @Override
    public void onSukses() {
        this.showToast(getResources().getString(R.string.sinkron_berhasil));
    }
}
