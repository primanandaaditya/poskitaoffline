package com.kitadigi.poskita.fragment.sinkron;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.kategori.IMKategoriAdapter;
import com.kitadigi.poskita.activities.sinkron.PengaturanSinkronActivity;
import com.kitadigi.poskita.base.BaseFragment;
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
import com.kitadigi.poskita.fragment.kategori.Datum;
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
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnit;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnitResult;
import com.kitadigi.poskita.sinkron.unit.delete.SinkronDeleteUnitController;
import com.kitadigi.poskita.sinkron.unit.insert.ISinkronAddUnitResult;
import com.kitadigi.poskita.sinkron.unit.insert.SinkronInsertUnitController;
import com.kitadigi.poskita.sinkron.unit.update.ISinkronUpdateUnitResult;
import com.kitadigi.poskita.sinkron.unit.update.SinkronUpdateUnitController;
import com.kitadigi.poskita.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SinkronFragment extends BaseFragment implements
        ISinkronAddKategoriResult, ISinkronUpdateKategoriResult, ISinkronDeleteKategoriResult,
        ISinkronAddBrandResult, ISinkronUpdateBrandResult, ISinkronDeleteBrandResult,
        ISinkronAddUnitResult, ISinkronUpdateUnitResult, ISinkronDeleteUnitResult,
        ISinkronAddProdukResult, ISinkronUpdateProdukResult, ISinkronDeleteProdukResult,
        ISinkronAddJualResult, ISinkronAddBeliResult,
        IKategoriResult, IBrandResult, IUnitResult, IBarangResult,
        IStokResult

{

    //init session, untuk menampilkan tanggal last sync
    SessionManager sessionManager;

    //init widget
    TextView tvLastSync;
    Button btnSinkron, btnPengaturan;
    SweetAlertDialog sweetAlertDialog;
//    ListView lv;


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

//    List<Datum> datumList;
//    Datum datum;

    public SinkronFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init session, untuk menampilkan tanggal di tvLastSync
        sessionManager=new SessionManager(getActivity());

//        datumList = new ArrayList<>();



        //init controller
        sinkronInsertKategoriController =new SinkronInsertKategoriController(getActivity(),this);
        sinkronUpdateKategoriController =new SinkronUpdateKategoriController(getActivity(),this);
        sinkronDeleteKategoriController =new SinkronDeleteKategoriController(getActivity(),this);

        sinkronInsertBrandController    = new SinkronInsertBrandController(getActivity(),this);
        sinkronUpdateBrandController    = new SinkronUpdateBrandController(getActivity(),this);
        sinkronDeleteBrandController    = new SinkronDeleteBrandController(getActivity(),this);

        sinkronInsertUnitController     = new SinkronInsertUnitController(getActivity(),this);
        sinkronUpdateUnitController     = new SinkronUpdateUnitController(getActivity(),this);
        sinkronDeleteUnitController     = new SinkronDeleteUnitController(getActivity(),this);

        sinkronInsertProdukController   = new SinkronInsertProdukController(getActivity(),this);
        sinkronUpdateProdukController   = new SinkronUpdateProdukController(getActivity(), this);
        sinkronDeleteProdukController   = new SinkronDeleteProdukController(getActivity(), this);

        sinkronInsertJualController     = new SinkronInsertJualController(getActivity(),this);
        sinkronInsertBeliController     = new SinkronInsertBeliController(getActivity(), this);

        kategoriController              = new KategoriController(getActivity(),this);
        brandController                 = new BrandController(getActivity(), this);
        unitController                  = new UnitController(getActivity(), this);
        barangController                = new BarangController(this,getActivity());

        stokController                  = new StokController(this, getActivity());




//        lv=(ListView)getActivity().findViewById(R.id.lv);
//
//
//
//        datum=new Datum();
//        datumList.add(datum);
//
//        datum=new Datum();
//        datumList.add(datum);
//
//        datum=new Datum();
//        datumList.add(datum);
//
//        final IMKategoriAdapter imKategoriAdapter = new IMKategoriAdapter(getActivity(), datumList);
//        lv.setAdapter(imKategoriAdapter);

        //tampilkan tanggal terakhir sync
        tvLastSync=(TextView)getActivity().findViewById(R.id.tvLastSync);
        this.applyFontRegularToTextView(tvLastSync);
        tvLastSync.setText(getActivity().getResources().getString(R.string.sinkron_data_terakhir) + sessionManager.getLastSync());

        //tombol untuk panggil activity pengaturan sinkon
        btnPengaturan=(Button)getActivity().findViewById(R.id.btnPengaturan);
        this.applyFontBoldToButton(btnPengaturan);
        btnPengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PengaturanSinkronActivity.class);
                startActivity(intent);
            }
        });


        btnSinkron=(Button)getActivity().findViewById(R.id.btnSinkron);
        this.applyFontBoldToButton(btnSinkron);
        btnSinkron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncInsertKategori();
//                String data = imKategoriAdapter.createJSONArray().toString();
//                sinkronInsertKategoriController.insert_kategori(data);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sinkron, container, false);



    }

    void syncInsertKategori(){

        //tampikan sweet dialog
        //init sweet dialog
        sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);

        sweetAlertDialog.show();

        //tembak API
        sinkronInsertKategoriController.insert_kategori();
    }

    @Override
    public void onSinkronAddKategoriSuccess(SinkronResponse sinkronResponse) {

        //lanjutkan ke sinkron update
        sinkronUpdateKategoriController.update_kategori();

    }

    @Override
    public void onSinkronAddKategoriError(String error) {
//        this.showToast(error);
        sinkronUpdateKategoriController.update_kategori();
    }

    @Override
    public void onSinkronUpdateKategoriSuccess(SinkronResponse sinkronResponse) {
       //lanjutkan delete
        sinkronDeleteKategoriController.delete_kategori();
    }

    @Override
    public void onSinkronUpdateKategoriError(String error) {
        //lanjutkan delete
        sinkronDeleteKategoriController.delete_kategori();

    }

    @Override
    public void onSinkronDeleteKategoriSuccess(SinkronResponse sinkronResponse) {
        sinkronInsertBrandController.insert_brand();

    }

    @Override
    public void onSinkronDeleteKategoriError(String error) {

        sinkronInsertBrandController.insert_brand();
    }

    @Override
    public void onSinkronAddBrandSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sinkron update brand
        sinkronUpdateBrandController.update_brand();
    }

    @Override
    public void onSinkronAddBrandError(String error) {
        //lanjutkan ke sinkron update brand
        sinkronUpdateBrandController.update_brand();
    }

    @Override
    public void onSinkronUpdateBrandSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sinkron delete brand
        sinkronDeleteBrandController.delete_brand();
    }

    @Override
    public void onSinkronUpdateBrandError(String error) {
        //lanjutkan ke sinkron delete brand
        sinkronDeleteBrandController.delete_brand();
    }

    @Override
    public void onSinkronDeleteBrandSuccess(SinkronResponse sinkronResponse) {

        //lanjut ke sync insert unit
        sinkronInsertUnitController.insert_unit();
    }

    @Override
    public void onSinkronDeleteBrandError(String error) {
        //lanjut ke sync insert unit
        sinkronInsertUnitController.insert_unit();
    }

    @Override
    public void onSinkronAddUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync update unit
        sinkronUpdateUnitController.update_unit();
    }

    @Override
    public void onSinkronAddUnitError(String error) {
//lanjut ke sync update unit
        sinkronUpdateUnitController.update_unit();
    }

    @Override
    public void onSinkronUpdateUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync delete unit
        sinkronDeleteUnitController.delete_unit();
    }

    @Override
    public void onSinkronUpdateUnitError(String error) {
        //lanjut ke sync delete unit
        sinkronDeleteUnitController.delete_unit();
    }

    @Override
    public void onSinkronDeleteUnitSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync insert produk
       sinkronInsertProdukController.insert_produk();
    }

    @Override
    public void onSinkronDeleteUnitError(String error) {
        //lanjut ke sync insert produk
        sinkronInsertProdukController.insert_produk();
    }

    @Override
    public void onSinkronAddProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync update produk
        sinkronUpdateProdukController.update_produk();
    }

    @Override
    public void onSinkronAddProdukError(String error) {
        //lanjut ke sync update produk
        sinkronUpdateProdukController.update_produk();
    }

    @Override
    public void onSinkronUpdateProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync delete produk
        sinkronDeleteProdukController.delete_produk();
    }

    @Override
    public void onSinkronUpdateProdukError(String error) {
        //lanjut ke sync delete produk
        sinkronDeleteProdukController.delete_produk();
    }

    @Override
    public void onSinkronDeleteProdukSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sync jual
        sinkronInsertJualController.insert_jual();
    }

    @Override
    public void onSinkronDeleteProdukError(String error) {
        //lanjut ke sync jual
        sinkronInsertJualController.insert_jual();
    }

    @Override
    public void onSinkronAddJualSuccess(SinkronResponse sinkronResponse) {
        //lanjutkan ke sync pembelian
        sinkronInsertBeliController.insert_beli();
    }

    @Override
    public void onSinkronAddJualError(String error) {
        //lanjutkan ke sync pembelian
        sinkronInsertBeliController.insert_beli();
    }

    @Override
    public void onSinkronAddBeliSuccess(SinkronResponse sinkronResponse) {
        //lanjut ke sinkron select kategori
        kategoriController.getKategoriList();
    }

    @Override
    public void onSinkronAddBeliError(String error) {
        //lanjut ke get kategori
        kategoriController.getKategoriList();
    }

    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline) {
        //lanjut ke get brand
        brandController.getBrandList();
    }

    @Override
    public void onKategoriError(String error, List<Kategori> kategoriOffline) {
        //lanjut ke get brand
        brandController.getBrandList();
    }

    @Override
    public void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline) {
        //lanjut ke get unit
        unitController.getUnitList();
    }

    @Override
    public void onBrandError(String error, List<Brand> brandOffline) {
        //lanjut ke get unit
        unitController.getUnitList();
    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {
        //lanjut ke sync stok
        stokController.getStok();
    }

    @Override
    public void onError(String error, List<Item> items) {
        //lanjut ke sync stok
        stokController.getStok();
    }

    @Override
    public void onUnitSuccess(UnitModel unitModel, List<Unit> units) {
//lanjut ke get produk
        barangController.getBarang();
    }

    @Override
    public void onUnitError(String error, List<Unit> units) {
        //lanjut ke get produk
        barangController.getBarang();
    }

    @Override
    public void onStokSuccess(StokModel stokModel, List<Stok> stokOffline) {
        //this.showToast(stokModel.getMessage());
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.sinkron_berhasil));
        //sweetAlertDialog.dismissWithAnimation();

        //simpan tanggal sekarang di session
        sessionManager.createLasySync();
        tvLastSync.setText(getActivity().getResources().getString(R.string.sinkron_data_terakhir) + sessionManager.getLastSync());

    }

    @Override
    public void onStokError(String error, List<Stok> stoksOffline) {
        //this.showToast(error);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(error);
    }
}
