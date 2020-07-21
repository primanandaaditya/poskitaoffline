package com.kitadigi.poskita.fragment.addunit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.fragment.editunit.EditUnitController;
import com.kitadigi.poskita.fragment.editunit.IEditUnitResult;

public class AddUnitActivity extends BaseActivity implements AddUnitResult, IEditUnitResult {


    //init widget
    TextView tv_nama_unit,tv_singkatan_unit, tv_nav_header;
    EditText et_nama_unit,et_singkatan_unit;
    Button btnSave;
    ImageView iv_back;

    //init controller
    AddUnitController addUnitController;
    EditUnitController editUnitController;


    String id_unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        findId();
    }

    void findId(){

        //findviewbyid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_nama_unit=(TextView)findViewById(R.id.tv_nama_unit);
        tv_singkatan_unit=(TextView)findViewById(R.id.tv_singkatan_unit);
        et_nama_unit=(EditText)findViewById(R.id.et_nama_unit);
        et_singkatan_unit=(EditText)findViewById(R.id.et_singkatan_unit);


        //tombol untuk simpan/edit
        btnSave=(Button)findViewById(R.id.btn_save);
        btnSave.setEnabled(true);


        //tentukan judul activity dan judul tombol
        Bundle extras                   = getIntent().getExtras();
        Intent intent = getIntent();
        if(extras==null){
            tv_nav_header.setText(getResources().getString(R.string.tambah_unit));
            btnSave.setText(getResources().getString(R.string.simpan));
        }else{
            tv_nav_header.setText(getResources().getString(R.string.edit_unit));
            et_nama_unit.setText(intent.getStringExtra("name"));
            et_singkatan_unit.setText(intent.getStringExtra("short_name"));
            id_unit=intent.getStringExtra("id");
            btnSave.setText(getResources().getString(R.string.edit));
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //jika simpan
                if (btnSave.getText().toString().matches(getResources().getString(R.string.simpan))){
                    saveUnit();
                }else{
                    editUnit();
                }
            }
        });


        iv_back=(ImageView)findViewById(R.id.iv_back);
        //tombol untuk menutup activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //apply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_nama_unit);
        this.applyFontRegularToTextView(tv_singkatan_unit);
        this.applyFontRegularToEditText(et_nama_unit);
        this.applyFontRegularToEditText(et_singkatan_unit);
        this.applyFontBoldToButton(btnSave);

    }


    void saveUnit(){

        if (et_nama_unit.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_unit_harus_diisi));

        }else{
            String nama_unit = et_nama_unit.getText().toString();
            String singkatan_unit = et_singkatan_unit.getText().toString();

            addUnitController=new AddUnitController(AddUnitActivity.this, this, true);
            addUnitController.addUnit(nama_unit,singkatan_unit);
        }


    }


    void editUnit(){

        if (et_nama_unit.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_unit_harus_diisi));

        }else{
            String nama_unit = et_nama_unit.getText().toString();
            String singkatan_unit = et_singkatan_unit.getText().toString();

            editUnitController=new EditUnitController(AddUnitActivity.this,this, true);
            editUnitController.editUnit(id_unit,nama_unit,singkatan_unit);
        }


    }


    @Override
    public void onAddUnitSuccess(BaseResponse baseResponse) {
        if (this.sessionExpired(baseResponse.getMessage())==0){
//            this.showToast(baseResponse.getMessage());
        }
        kosongkanEdit();
    }

    @Override
    public void onAddUnitError(String error) {
//        Toast.makeText(AddUnitActivity.this,error,Toast.LENGTH_LONG).show();
        kosongkanEdit();
        finish();
    }

    @Override
    public void onEditUnitSuccess(BaseResponse baseResponse) {
        if (this.sessionExpired(baseResponse.getMessage())==0){
//            this.showToast(getResources().getString(R.string.update_ok));
        }
        kosongkanEdit();
    }

    @Override
    public void onEditUnitError(String error) {

//        Toast.makeText(AddUnitActivity.this,error,Toast.LENGTH_LONG).show();
        kosongkanEdit();
        finish();
    }

    void kosongkanEdit(){
        et_nama_unit.setText("");
        et_singkatan_unit.setText("");
    }
}
