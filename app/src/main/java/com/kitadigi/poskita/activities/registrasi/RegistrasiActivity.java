package com.kitadigi.poskita.activities.registrasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.GetRegistrasiResultIntractor;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.RegistrasiView;
import com.kitadigi.poskita.activities.registrasi.RegistrasiActivityContract.Presenter;
import com.kitadigi.poskita.base.BaseResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrasiActivity extends BaseActivity implements RegistrasiView {

    TextView tv_nav_header,tvEmail,tvNama,tvTelepon,
    tvJenisToko,tvNamaToko,tvAlamatToko,tvAlamatPemilik,tvKeterangan;

    EditText etEmail,etNama,etTelepon,etJenisToko,
    etNamaToko,etAlamatToko,etAlamatPemilik,etKeterangan;

    Button btnSave;

    ImageView iv_back;

    Presenter presenter;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        findID();

    }

    void findID(){

        btnSave=(Button)findViewById(R.id.btn_save);

        iv_back=(ImageView)findViewById(R.id.iv_back);

        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tvEmail=(TextView)findViewById(R.id.tv_email);
        tvNama=(TextView)findViewById(R.id.tv_nama);
        tvTelepon=(TextView)findViewById(R.id.tv_telepon);
        tvJenisToko=(TextView)findViewById(R.id.tv_jenis_toko);
        tvNamaToko=(TextView)findViewById(R.id.tv_nama_toko);
        tvAlamatToko=(TextView)findViewById(R.id.tv_alamat_toko);
        tvAlamatPemilik=(TextView)findViewById(R.id.tv_alamat_pemilik);
        tvKeterangan=(TextView)findViewById(R.id.tv_keterangan);

        etEmail=(EditText)findViewById(R.id.et_email);
        etNama=(EditText)findViewById(R.id.et_nama);
        etTelepon=(EditText)findViewById(R.id.et_telepon);
        etJenisToko=(EditText)findViewById(R.id.et_jenis_toko);
        etNamaToko=(EditText)findViewById(R.id.et_nama_toko);
        etAlamatToko=(EditText)findViewById(R.id.et_alamat_toko);
        etAlamatPemilik=(EditText)findViewById(R.id.et_alamat_pemilik);
        etKeterangan=(EditText)findViewById(R.id.et_keterangan);


        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tvAlamatPemilik);
        this.applyFontRegularToTextView(tvAlamatToko);
        this.applyFontRegularToTextView(tvEmail);
        this.applyFontRegularToTextView(tvJenisToko);
        this.applyFontRegularToTextView(tvKeterangan);
        this.applyFontRegularToTextView(tvNama);
        this.applyFontRegularToTextView(tvNamaToko);
        this.applyFontRegularToTextView(tvTelepon);

        this.applyFontRegularToTextView(etAlamatPemilik);
        this.applyFontRegularToTextView(etAlamatToko);
        this.applyFontRegularToTextView(etEmail);
        this.applyFontRegularToTextView(etJenisToko);
        this.applyFontRegularToTextView(etKeterangan);
        this.applyFontRegularToTextView(etNama);
        this.applyFontRegularToTextView(etNamaToko);
        this.applyFontRegularToTextView(etTelepon);

        this.applyFontBoldToButton(btnSave);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //jika tombol ditekan
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //lakukan registrasi
                doRegistrasi();
            }
        });

    }


    @Override
    public void doRegistrasi() {

        //tampung edittext dalam variabel
        String alamatPemilik = etAlamatPemilik.getText().toString();
        String alamtToko = etAlamatToko.getText().toString();
        String email = etEmail.getText().toString();
        String jenisToko = etJenisToko.getText().toString();
        String keterangan = etKeterangan.getText().toString();
        String nama = etNama.getText().toString();
        String namaToko = etNamaToko.getText().toString();
        String telepon = etTelepon.getText().toString();


        //jika masih ada yang kosong
        if (alamatPemilik.matches("") ||
            alamtToko.matches("") ||
                jenisToko.matches("") ||
                nama.matches("") ||
                namaToko.matches("") ||
                telepon.matches("")
        ){
            this.showToast(getResources().getString(R.string.masih_ada_yang_kosong));
        }else{
            //presenter untuk nembak API registrasi
            presenter = new RegistrasiActivityPresenter(this, new RegistrasiActivityImpl(email,nama,telepon,jenisToko,namaToko,alamtToko,alamatPemilik,keterangan));
            presenter.requestDataFromServer();
        }


    }

    @Override
    public void showProgress() {
        sweetAlertDialog=new SweetAlertDialog(RegistrasiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.now_loading));
        sweetAlertDialog.show();

    }

    @Override
    public void hideProgress() {
        sweetAlertDialog.dismissWithAnimation();
    }

    @Override
    public void setDataToView(RegistrasiRespon registrasiRespon) {
        //jika registrasi OK
        if (registrasiRespon.status==1){

            //tampilkan layar sukses
            Intent intent = new Intent(RegistrasiActivity.this, RegistrasiSuksesActivity.class);
            startActivity(intent);

            //tutup screen
            finish();
        }else{
            this.showToast(registrasiRespon.getMessage());
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        this.showToast(throwable.getMessage());
    }
}
