package com.kitadigi.poskita.fragment.addkategori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.fragment.editkategori.EditKategoriController;
import com.kitadigi.poskita.fragment.editkategori.IEditKategoriResult;

public class AddKategoriActivity extends BaseActivity implements IAddKategoriResult, IEditKategoriResult {

    //init widget
    TextView tv_kode_kategori,tv_nama_kategori, tv_nav_header;
    EditText et_kode_kategori,et_nama_kategori;
    Button btnSave;
    ImageView iv_back;

    //init controller
    AddKategoriController addKategoriController;
    EditKategoriController editKategoriController;
//    KategoriInsertDAO kategoriInsertDAO;
//    KategoriEditDAO kategoriEditDAO;


    //var ini untuk menangkap intent dari listview kategori
    String id_kategori;

    //var ini untuk nangkap intent id kategori yang sudah di-enkrip
    String enkripp_id_kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kategori);

        findId();

    }

    void findId(){



        //findviewbyid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_kode_kategori=(TextView)findViewById(R.id.tv_kode_kategori);
        tv_nama_kategori=(TextView)findViewById(R.id.tv_nama_kategori);
        et_kode_kategori=(EditText)findViewById(R.id.et_kode_kategori);
        et_nama_kategori=(EditText)findViewById(R.id.et_nama_kategori);


        //tombol untuk simpan/edit
        btnSave=(Button)findViewById(R.id.btn_save);
        btnSave.setEnabled(true);


        //get intent dari listview kategori, di fragmentkategoriprima
        //tentukan judul activity dan judul tombol
        Bundle extras                   = getIntent().getExtras();
        Intent intent = getIntent();
        if(extras==null){
            tv_nav_header.setText(getResources().getString(R.string.tambah_kategori));
            btnSave.setText(getResources().getString(R.string.simpan));
        }else{

            //terima lemparan intent dari adapter
            tv_nav_header.setText(getResources().getString(R.string.edit_kategori));
            et_kode_kategori.setText(intent.getStringExtra("kode_kategori"));
            et_nama_kategori.setText(intent.getStringExtra("nama_kategori"));

            //var ini merupakan hasil intent
            //dari fragmentpprima dari listviewnya (KategoriSyncAdapter.java)
            //ini digunakan untuk EditKategoriController
            //tapi yang diambil bukan langsung enkrip-an
            //melainkan id yang bertipe long di-string
            //soalnya di EditKategoriController nanti
            //harus ada pencarian kategori berdasarkan id
            //di sqlite
            //susah kalau yang dilempar langsung enkrip-nya
            //jadi nanti id-nya di-MD5 pas di EditKategoriController-nya
            id_kategori=intent.getStringExtra("id_kategori");


            btnSave.setText(getResources().getString(R.string.edit));
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //jika simpan
                if (btnSave.getText().toString().matches(getResources().getString(R.string.simpan))){
                    saveKategori();
                }else{
                    editKategori();
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
        this.applyFontRegularToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_kode_kategori);
        this.applyFontRegularToTextView(tv_nama_kategori);
        this.applyFontRegularToEditText(et_kode_kategori);
        this.applyFontRegularToEditText(et_nama_kategori);
        this.applyFontBoldToButton(btnSave);

        //init db offline
//        kategoriInsertDAO=new KategoriInsertDAO(AddKategoriActivity.this,this);
//        kategoriEditDAO=new KategoriEditDAO(AddKategoriActivity.this,this);

    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        kosongkanEditText();
        if(this.sessionExpired(baseResponse.getMessage())==0){
            this.showToast(baseResponse.getMessage());

        }
//        kosongkanEditText();
//        Toast.makeText(AddKategoriActivity.this,baseResponse.getMessage(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String error) {
//        this.showToast(error);
//        Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.tersimpan_offline),Toast.LENGTH_SHORT).show();
        kosongkanEditText();
    }

    void kosongkanEditText(){
        et_kode_kategori.setText("");
        et_nama_kategori.setText("");
    }

    void saveKategori(){

        if (et_nama_kategori.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_kategori_harus_diisi));

        }else{
            String nama_kategori = et_nama_kategori.getText().toString();
            String kode_kategori = et_kode_kategori.getText().toString();

            addKategoriController=new AddKategoriController(AddKategoriActivity.this, this, true);
            addKategoriController.addKategori(nama_kategori,kode_kategori);

//            kategoriInsertDAO.addKategori(nama_kategori,kode_kategori);

        }


    }


    void editKategori(){

        if (et_nama_kategori.getText().toString().matches("")){
//            Toast.makeText(AddKategoriActivity.this,getResources().getString(R.string.nama_kategori_harus_diisi),Toast.LENGTH_SHORT).show();
            this.showToast(getResources().getString(R.string.nama_kategori_harus_diisi));

        }else{

            //cari kategori berdasarkan id dulu
//            Kategori kategori = kategoriEditDAO.getKategoriById(id_kategori);


            //get text dari edittext
            String nama_kategori = et_nama_kategori.getText().toString();
            String kode_kategori = et_kode_kategori.getText().toString();

            //pass nilai
//            kategori.setName_category(nama_kategori);
//            kategori.setCode_category(kode_kategori);
//            kategori.setSync_update(Constants.STATUS_BELUM_SYNC);

            //edit
//            kategoriEditDAO.editKategori(kategori);

            editKategoriController=new EditKategoriController(AddKategoriActivity.this, this, true);
            editKategoriController.editKategori(id_kategori,nama_kategori,kode_kategori);
        }


    }

    @Override
    public void onEditSuccess(BaseResponse baseResponse) {
        if (baseResponse==null){

        }else{
            this.sessionExpired(baseResponse.getMessage());
            this.showToast(getResources().getString(R.string.update_ok));
        }
//        this.showToast(baseResponse.getMessage());
//        this.finish();
    }

    @Override
    public void onEditError(String error) {
//        this.showToast(error);
    }
}
