package com.kitadigi.poskita.fragment.addbrand;

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
import com.kitadigi.poskita.fragment.editbrand.EditBrandController;
import com.kitadigi.poskita.fragment.editbrand.IEditResult;

public class AddBrandActivity extends BaseActivity implements IAddBrandResult, IEditResult {

    //init widget
    TextView tv_nama_brand,tv_deskripsi_brand, tv_nav_header;
    EditText et_nama_brand,et_deskripsi_brand;
    Button btnSave;
    ImageView iv_back;

    //init controller
    AddBrandController addBrandController;
    EditBrandController editBrandController;


    String id_brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);

        findId();
    }

    void findId(){

        //findviewbyid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_nama_brand=(TextView)findViewById(R.id.tv_nama_brand);
        tv_deskripsi_brand=(TextView)findViewById(R.id.tv_deskripsi_brand);
        et_nama_brand=(EditText)findViewById(R.id.et_nama_brand);
        et_deskripsi_brand=(EditText)findViewById(R.id.et_deskripsi_brand);


        //tombol untuk simpan/edit
        btnSave=(Button)findViewById(R.id.btn_save);
        btnSave.setEnabled(true);


        //tentukan judul activity dan judul tombol
        Bundle extras                   = getIntent().getExtras();
        Intent intent = getIntent();
        if(extras==null){
            tv_nav_header.setText(getResources().getString(R.string.tambah_brand));
            btnSave.setText(getResources().getString(R.string.simpan));
        }else{
            tv_nav_header.setText(getResources().getString(R.string.edit_brand));
            et_nama_brand.setText(intent.getStringExtra("name"));
            et_deskripsi_brand.setText(intent.getStringExtra("description"));
            id_brand=intent.getStringExtra("id");
            btnSave.setText(getResources().getString(R.string.edit));
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //jika simpan
                if (btnSave.getText().toString().matches(getResources().getString(R.string.simpan))){
                    saveBrand();
                }else{
                    editBrand();
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
        this.applyFontRegularToTextView(tv_nama_brand);
        this.applyFontRegularToTextView(tv_deskripsi_brand);
        this.applyFontRegularToEditText(et_nama_brand);
        this.applyFontRegularToEditText(et_deskripsi_brand);
        this.applyFontBoldToButton(btnSave);

    }

    void kosongkanEdit(){
        et_nama_brand.setText("");
        et_deskripsi_brand.setText("");
    }

    void saveBrand(){
        if (et_nama_brand.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_brand_harus_diisi));

        }else{
            String nama_brand = et_nama_brand.getText().toString();
            String deskripsi_brand = et_deskripsi_brand.getText().toString();

            addBrandController=new AddBrandController(AddBrandActivity.this,this, true);
            addBrandController.addBrand(nama_brand,deskripsi_brand);
        }
    }

    void editBrand(){
        if (et_nama_brand.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_brand_harus_diisi));

        }else{
            String nama_brand = et_nama_brand.getText().toString();
            String deskripsi_brand = et_deskripsi_brand.getText().toString();

            editBrandController=new EditBrandController(AddBrandActivity.this,this, true);
            editBrandController.editBrand(id_brand,nama_brand,deskripsi_brand);
        }
    }

    @Override
    public void onBrandSuccess(BaseResponse baseResponse) {
        if(baseResponse==null){

        }else{

            if (this.sessionExpired(baseResponse.getMessage())==0 ){
//                this.showToast(getResources().getString(R.string.insert_ok));
            }else{

            }

        }
        kosongkanEdit();
    }

    @Override
    public void onBrandError(String error) {
//        Toast.makeText(AddBrandActivity.this,error,Toast.LENGTH_LONG).show();
        kosongkanEdit();
        finish();

    }

    @Override
    public void onEditSuccess(BaseResponse baseResponse) {
        if (this.sessionExpired(baseResponse.getMessage())==0){
//            this.showToast(baseResponse.getMessage());
            kosongkanEdit();
        }

    }

    @Override
    public void onEditError(String error) {
//        Toast.makeText(AddBrandActivity.this,error,Toast.LENGTH_LONG).show();
        kosongkanEdit();
        finish();
    }
}
