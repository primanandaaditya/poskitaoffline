package com.kitadigi.poskita.fragment.sinkron;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.kategori.IMKategoriAdapter;
import com.kitadigi.poskita.activities.sinkron.PengaturanSinkronActivity;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.brand.dengan_header.BrandController;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.fragment.item.dengan_header.BarangController;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.IBarangResult;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriController;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriModel;
import com.kitadigi.poskita.fragment.pos.IStokResult;
import com.kitadigi.poskita.fragment.pos.StokController;
import com.kitadigi.poskita.fragment.pos.StokModel;
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
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnit;
import com.kitadigi.poskita.sinkron.unit.delete.ISinkronDeleteUnitResult;
import com.kitadigi.poskita.sinkron.unit.delete.SinkronDeleteUnitController;
import com.kitadigi.poskita.sinkron.unit.insert.ISinkronAddUnitResult;
import com.kitadigi.poskita.sinkron.unit.insert.SinkronInsertUnitController;
import com.kitadigi.poskita.sinkron.unit.update.ISinkronUpdateUnitResult;
import com.kitadigi.poskita.sinkron.unit.update.SinkronUpdateUnitController;
import com.kitadigi.poskita.util.AlarmReceiver;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.ISinkronizer;
import com.kitadigi.poskita.util.InternetChecker;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.Sinkronisasi;
import com.kitadigi.poskita.util.Sinkronizer;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SinkronFragment extends BaseFragment implements ISinkronizer

{

    private final int CAMERA_RESULT = 101;
    private final int GALLERY_RESULT = 102;

    //buat cek internet
    InternetChecker internetChecker;

    //init session, untuk menampilkan tanggal last sync
    SessionManager sessionManager;

    //init widget
    TextView tvLastSync;
    Button btnSinkron, btnPengaturan, btnMatikanAlarm;
    SweetAlertDialog sweetAlertDialog;
//    ListView lv;

    Sinkronisasi sinkronisasi;
    Integer progrez = Constants.progress;



    public SinkronFragment() {
        // Required empty public constructor
    }

    void mintaPermission(){

        //minta izin user untuk ambil kamera/galeri
        //waktu diklik, harus dapat izin dari user dulu
        //ask permission
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

        }
        else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(getActivity(), getResources().getString(R.string.izin_akses_kamera_diperlukan), Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){



            }
            else{
                Toast.makeText(getActivity(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init cek internet
        internetChecker = new InternetChecker();

        //init session, untuk menampilkan tanggal di tvLastSync
        sessionManager=new SessionManager(getActivity());



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

                //sinkron mulai dari insert kategori dulu
                syncInsertKategori();
            }
        });

        btnMatikanAlarm = (Button)getActivity().findViewById(R.id.btnMatikanAlarm);
        this.applyFontBoldToButton(btnMatikanAlarm);
        btnMatikanAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm  = getActivity().getPackageManager();
                ComponentName componentName = new ComponentName(getActivity(), AlarmReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alarm_dimatikan), Toast.LENGTH_LONG).show();
            }
        });

        mintaPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sinkron, container, false);



    }

    void syncInsertKategori(){

        //cek dulu apakah ada internet atau tidak?
        if (internetChecker.haveNetwork(getActivity())){

            //tampikan sweet dialog
            //init sweet dialog
            sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.now_loading));
            sweetAlertDialog.setCancelable(false);

            sweetAlertDialog.show();

            //tembak API
            sinkronisasi = new Sinkronisasi(getActivity(), this);
            sinkronisasi.mulaiSinkron();
        }else{

            //jika tidak ada internet, munculkan pesan
            this.showToast(getActivity().getResources().getString(R.string.tidak_ada_koneksi_internet));
        }


    }


    @Override
    public void onNoInternet() {
        this.showToast(getActivity().getResources().getString(R.string.no_internet));
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onProgress(Integer progress) {
        sweetAlertDialog.setContentText(progress.toString() + "/" + progrez.toString());
    }

    @Override
    public void onFinish(String pesan) {

        //this.showToast(stokModel.getMessage());
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.sinkron_gagal));

        //sweetAlertDialog.dismissWithAnimation();
        //simpan tanggal sekarang di session
        sessionManager.createLasySync();
        tvLastSync.setText(getActivity().getResources().getString(R.string.sinkron_data_terakhir) + sessionManager.getLastSync());
    }

    @Override
    public void onSukses() {
//
        //this.showToast(stokModel.getMessage());
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.sinkron_berhasil));
        sweetAlertDialog.setContentText("");


        //simpan tanggal sekarang di session
        sessionManager.createLasySync();
        tvLastSync.setText(getActivity().getResources().getString(R.string.sinkron_data_terakhir) + sessionManager.getLastSync());

    }
}
