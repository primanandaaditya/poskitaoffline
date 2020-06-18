package com.kitadigi.poskita.activities.registrasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;

public class RegistrasiActivity extends BaseActivity {

    TextView tv_nav_header,tvEmail,tvNama,tvTelepon,
    tvJenisToko,tvNamaToko,tvAlamatToko,tvAlamatPemilik,tvKeterangan;

    EditText etEmail,etNama,etTelepon,etJenisToko,
    etNamaToko,etAlamatToko,etAlamatPemilik,etKeterangan;

    Button btnSave;

    ImageView iv_back;

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


        this.applyFontRegularToTextView(tv_nav_header);
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


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
