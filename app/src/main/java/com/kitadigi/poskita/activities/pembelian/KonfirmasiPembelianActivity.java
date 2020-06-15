package com.kitadigi.poskita.activities.pembelian;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.util.DateUtil;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SpinnerFormat;
import com.kitadigi.poskita.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class KonfirmasiPembelianActivity extends BaseActivity implements IAddPembelianResult {


    //init controller
    AddPembelianController addPembelianController;

    //init widget
    TextView tv_label_tanggal,tv_nama_supplier,tv_nomor_referensi,tv_uang_dibayar,tv_total_pembelian, tv_nav_header;
    EditText et_nomor_referensi, et_uang_dibayar, et_total_pembelian, et_tanggal;
    Spinner sp_supplier;
    Button btnSave;
    ImageView iv_back;

    //integer untuk menyimpan id supplier
    Integer id_supplier;

    //integer untk memperoleh total pembelian dari intent
    Integer total_pembelian;

    //session untuk menghapus ppembelian offline, kalau sudah menyimpan
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembelian);


        //init session
        sessionManager=new SessionManager(KonfirmasiPembelianActivity.this);

        //dapatkan intent dulu
        //untuk mendapatkan totalpembelian
        Intent intent = getIntent();
        total_pembelian = intent.getIntExtra("total_pembelian",0);


        findID();
    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    void findID(){

        //init controller
        addPembelianController =new AddPembelianController(KonfirmasiPembelianActivity.this,this, true);

        //findid
        sp_supplier=(Spinner)findViewById(R.id.sp_supplier);
        et_uang_dibayar=(EditText)findViewById(R.id.et_uang_dibayar);
        et_tanggal = (EditText)findViewById(R.id.et_tanggal);
        et_total_pembelian=(EditText)findViewById(R.id.et_total_pembelian);
       et_nomor_referensi =(EditText) findViewById(R.id.et_nomor_referensi);
       tv_nomor_referensi=(TextView)findViewById(R.id.tv_nomor_referensi);
       tv_label_tanggal=(TextView)findViewById(R.id.tv_label_tanggal);
       tv_nama_supplier=(TextView)findViewById(R.id.tv_nama_supplier);
       tv_uang_dibayar=(TextView)findViewById(R.id.tv_uang_dibayar);
       tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
       tv_total_pembelian=(TextView)findViewById(R.id.tv_total_pembelian);
       btnSave=(Button)findViewById(R.id.btn_save);
       iv_back=(ImageView)findViewById(R.id.iv_back);


       //apply font
       this.applyFontRegularToEditText(et_nomor_referensi);
       this.applyFontBoldToButton(btnSave);
       this.applyFontBoldToTextView(tv_nomor_referensi);
       this.applyFontBoldToTextView(tv_nama_supplier);
       this.applyFontBoldToTextView(tv_uang_dibayar);
       this.applyFontRegularToEditText(et_uang_dibayar);
       this.applyFontBoldToEditText(et_total_pembelian);
       this.applyFontBoldToTextView(tv_total_pembelian);
       this.applyFontBoldToTextView(tv_nav_header);
       this.applyFontBoldToTextView(tv_label_tanggal);
       this.applyFontRegularToEditText(et_tanggal);



       //apply style ke Spinner supplier
        //populasikan daftar suppplier di Spinner supplier
        //===============================================================================



        //PERHATIAN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //ini untuk API supplier, di kode 555
        //untuk selected index di kode 556

        //555
        //buat array string khusus nama supplier
        final List<String> listSupplier = new ArrayList<>();
        listSupplier.add("PT. Indomaret, Tbk");
        listSupplier.add("PT. Alfamart, Tbk");
        listSupplier.add("Sinar Harapan");
        listSupplier.add("CV. Pacific");

        //buat hashmap dan counter
        int counter = 0;
        final HashMap<String,Integer> hash = new HashMap<>();

        for(String s : listSupplier){
            hash.put(s, counter);
            counter=counter+5;
        }


        //dengan class SpinnerFormat, aplikasikan ke spinner Supplier
        SpinnerFormat.MySpinnerAdapter supplierAdapter = new SpinnerFormat.MySpinnerAdapter(
                KonfirmasiPembelianActivity.this,
                R.layout.myspinner,
                listSupplier
        );
        sp_supplier.setAdapter(supplierAdapter);

        //jika user memilih spinner
        //maka assign-kan var id_supplier dengan hash di atas
        sp_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                //kode 556
                //contoh penggunaan
                id_supplier = hash.get(listSupplier.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //untuk tombol panah kiri atas
       iv_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });


       //untuk pemisah ribuan
        et_uang_dibayar.addTextChangedListener(new StringUtil.NumberTextWatcher(et_uang_dibayar));
        et_total_pembelian.addTextChangedListener(new StringUtil.NumberTextWatcher(et_total_pembelian));

        //tampilkan total pembelian dari intent ke et_total_pembelian
        et_total_pembelian.setText(total_pembelian.toString());

        //textbox tanggal
        //jika diklik, muncul datepicker
        et_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //untuk memunculkan dialog date picker
                DateUtil dateUtil=new DateUtil();
                dateUtil.dateDialog(KonfirmasiPembelianActivity.this, et_tanggal);

            }
        });

       btnSave.setEnabled(true);
       btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //dapatkan uang yang dibayar
               String total_pembayaran = et_uang_dibayar.getText().toString().replaceAll("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+", "");

               //untuk id supplier bisa dianggap = 0
               id_supplier = 0;

               //simpan online
               addPembelianController.addPembelian(
                       id_supplier.toString(),
                       et_nomor_referensi.getText().toString(),
                       Integer.parseInt(total_pembayaran),
                       total_pembelian
               );

           }
       });
    }

    @Override
    public void onAddPembelianSuccess(BaseResponse baseResponse) {

        if (this.sessionExpired(baseResponse.getMessage())==0){
            if (baseResponse.getMessage().matches(getResources().getString(R.string.add_purchase_order_true))){
                pindahLayarPembelianBerhasil();
            }else{
//                this.showToast(baseResponse.getMessage());
                Toast.makeText(KonfirmasiPembelianActivity.this,baseResponse.getMessage(),Toast.LENGTH_SHORT).show();
                pindahKeHome();
            }

            //happus pembelian offline sementara di session
            sessionManager.clearPembelianOffline();
        }


    }

    @Override
    public void onAddPembelianError(String error) {


        pindahLayarPembelianBerhasil();

        //happus pembelian offline sementara di session
        sessionManager.clearPembelianOffline();

        //tutup acctivity
        finish();
    }


    void pindahLayarPembelianBerhasil(){
        Intent intent=new Intent(KonfirmasiPembelianActivity.this,PembelianBerhasilActivity.class);
        startActivity(intent);
        finish();
    }

    void pindahKeHome(){
        //pindah ke home
        Intent homeActivity = new Intent(KonfirmasiPembelianActivity.this, MainActivity.class);
        homeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeActivity);
        this.finish();

    }


}
