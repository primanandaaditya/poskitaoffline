package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ROKartuStokActivity extends BaseActivity implements IKartuStokResult {

    //init controller
    KartuStokController kartuStokController;

    //session untuk get enkrip id user
    SessionManager sessionManager;

    //untuk menampung id user
    String enkripIdUser;

    //string untuk menampung tanggal dari dan tanggal sampai
    String tanggal_dari,tanggal_sampai;

    //init listview
    ListView lv;

    //init tv
    TextView tv_nav_header, tv_total_qty, tv_grand_total, tv_title, tv_price_purchase, tv_price_sell;

    //init imageview
    ImageView iv_back,iv_filter, iv_icon;



    //di bawah ini untuk dialog pilih tanggal
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    DatePicker et_periode_awal;


    //var untuk menampung string intent dari PilihBarangActivity.java
    String kode_id;


    //init adapter
    KartuStokDetailAdapter kartuStokDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rokartu_stok);

        //terima intent dari PilihBarangActivity.java
        Intent intent = getIntent();
        kode_id = intent.getStringExtra("kode_id");
        Log.d("intent", kode_id);

        //tombol untuk filter tanggal
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFormStartDate();
            }
        });

        //iv_back
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //untuk nampilin gambar
        iv_icon=(ImageView)findViewById(R.id.iv_icon);

        //init session manager
        sessionManager=new SessionManager(ROKartuStokActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        //init listview report
        lv=(ListView)findViewById(R.id.lv);

        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_price_purchase=(TextView)findViewById(R.id.tv_price_purchase);
        tv_price_sell=(TextView)findViewById(R.id.tv_price_sell);

//        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_total_qty);
        this.applyFontRegularToTextView(tv_grand_total);
        this.applyFontBoldToTextView(tv_title);
        this.applyFontRegularToTextView(tv_price_purchase);
        this.applyFontRegularToTextView(tv_price_sell);
//        this.applyFontRegularToTextView(tv_tanggal);

        //pasang nama,gambar produk
        settingItem();

        //waktu pertama kali, suruh user memilih tanggal
        DialogFormStartDate();

    }


    void requestReport(){

        kartuStokController=new KartuStokController(ROKartuStokActivity.this, this, kode_id);
        kartuStokController.getReport(tanggal_dari,tanggal_sampai);

    }


    private void DialogFormStartDate() {

        dialog = new AlertDialog.Builder(ROKartuStokActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);


        //set title
        tv_title.setText(getResources().getString(R.string.dari_tanggal));


//        this.applyFontRegularToEditText(et_periode_awal);
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filterTanggal = StringUtil.tanggalDariDatePicker(et_periode_awal);

                //simpan ke variabel tanggal_dari
                tanggal_dari = filterTanggal;

                //tutup dialog
                dialog.dismiss();

                //pindah ke dialog end date
                DialogFormEndDate();


//                requestReport(enkripIdUser,filterTanggal,filterTanggal);
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void DialogFormEndDate() {

        dialog = new AlertDialog.Builder(ROKartuStokActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);


        //set title
        tv_title.setText(getResources().getString(R.string.sampai_tanggal));
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String filterTanggal = StringUtil.tanggalDariDatePicker(et_periode_awal);

                //simpan ke variabel tanggal sampai
                tanggal_sampai = filterTanggal;

                //proses report
                requestReport();

                //tutup dialog
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onSuccessKartuStok(List<KartuStokModel> kartuStokModels) {

        //tampilkan di listview
        kartuStokDetailAdapter = new KartuStokDetailAdapter(ROKartuStokActivity.this, kartuStokModels);
        lv.setAdapter(kartuStokDetailAdapter);

        //tampilkan sum masuk dan keluar
        kartuStokController.sumKeluar(kartuStokModels,tv_total_qty);
        kartuStokController.sumMasuk(kartuStokModels, tv_grand_total);
    }

    @Override
    public void onErrorKartuStok(String error) {
        this.showToast(error);
    }

    //fungsi ini untuk mendapatkan intent dari PilihBarangActivity.java
    //dari listview yang diklik
    void settingItem(){

        //tampung var intent
        Intent intent = getIntent();

        //get nilai param dalam intent
        String nama_barang = intent.getStringExtra("nama_barang");
        String harga_beli = intent.getStringExtra("harga_beli");
        String harga_jual = intent.getStringExtra("harga_jual");

        //settext dalam textview
        tv_title.setText(nama_barang);
        tv_price_sell.setText("Harga jual : " + harga_jual);
        tv_price_purchase.setText("Harga beli : " + harga_beli);

        //cek dulu apakah gambar benar2 ada di server
        if (intent.getStringExtra("image")==null || intent.getStringExtra("image").equals("")){
            //jika gambar tidak ada
            //tidak usah load
            Log.d("gambar item","String kosong");
        }else{
            //jika gambar ada
            //gambar sudah disimpan di hp user
            //tampilkan di imageview

            String image = intent.getStringExtra("image");

            File file = new File(image);
            boolean adaGambar=file.exists();

            if (adaGambar){
                //jika file gambar ada
                //load di iv_icon
//                file = new  File(directory.getAbsolutePath() + "/" + item.getImage());
//                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(ROKartuStokActivity.this)
                        .load(new File(image))
                        .into(iv_icon);
                Log.d("picasso ada", file.getAbsolutePath());

            }else{
                //tidak ada gambar

            }


        }

    }
}
