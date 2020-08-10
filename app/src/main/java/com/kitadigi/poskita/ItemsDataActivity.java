package com.kitadigi.poskita;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.additem.AddBarangController;
import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.fragment.additem.IAddBarangResult;
import com.kitadigi.poskita.fragment.brand.BrandController;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.fragment.edititem.EditBarangController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.fragment.kategori.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.KategoriController;
import com.kitadigi.poskita.fragment.kategori.KategoriModel;
import com.kitadigi.poskita.fragment.unit.IUnitResult;
import com.kitadigi.poskita.fragment.unit.UnitController;
import com.kitadigi.poskita.fragment.unit.UnitModel;
import com.kitadigi.poskita.util.SpinnerFormat.MySpinnerAdapter;
import com.kitadigi.poskita.util.ResizeImage;
import com.kitadigi.poskita.util.UrlToBitmap;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class ItemsDataActivity extends BaseActivity implements IAddBarangResult,
        IKategoriResult,
        IBrandResult,
        IUnitResult
{

    //variabel boolean
    //untuk deteksi apakah button pilih image di-klik atau tidak
    //karena kalau aplikasi baru pertama kali diinstal
    //saat activity ini dibuka -> dialog 'pilih kamera/galeri?' langsung muncul
    //padahal dialog itu hanya boleh muncul saat button 'pilih image' diklik
    boolean klik=false;

    //qr code scanner object
    private IntentIntegrator qrScan;


    private static final String TAG = ItemsDataActivity.class.getName();
    Context context;

    //init controller
    private AddBarangController addBarangController;
    private EditBarangController editBarangController;
    private KategoriController kategoriController;
    private UnitController unitController;
    private BrandController brandController;


    //init Datum (barang)
    //untuk keperluan edit
    com.kitadigi.poskita.fragment.item.Datum barang;


    /* init ui */
    RelativeLayout rl_save;
    TextView tv_nav_header, tv_remark_items, tv_remark_unit, tv_remark_price, tv_remark_price_sell;
    TextView tv_remark_brand, tv_remark_kategori, tv_remark_barkode, tv_stok, tv_stok_minimum;
    EditText et_items, et_price, et_price_sell, et_barkode, et_stok, et_stok_minimum;
    ImageView iv_back, iv_preview_photo;
    Button btn_photo, btn_save;
    Spinner sp_unit_id, sp_brand_id, sp_kategori;
    ImageButton ib_barkode;

    /* typeface fonts */
    Typeface fonts, fontsItalic;

    private AlertDialog alertDialog;

    private SweetAlertDialog sweetAlertDialog;

    LayoutInflater inflater = null;

    /* Sqlite database */
    private Database db;

    private final int CAMERA_RESULT = 101;
    private final int GALLERY_RESULT = 102;
    TimeZone tz;

    Animation fadeIn, fadeOut;

    String imageFilePath="";
    String nama_barang;
    Integer id,harga_beli,harga_jual;
    String id_item;

//    untuk spinner-spinner
    Integer id_kategori,id_unit,id_brand;
    String category_mobile_id,brand_mobile_id,unit_mobile_id;

    //untuk cek inten dari adapter
    Bundle extras;


    Bitmap ssBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_data);
        context = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //init find id
        findID();


    }

    void findID(){
        //init barcode scanner
        qrScan = new IntentIntegrator(ItemsDataActivity.this);

        //init id spinner kategori dan brand
        id_brand=0;
        id_kategori=0;
        id_unit=0;
        category_mobile_id="";
        brand_mobile_id = "";
        unit_mobile_id = "";

        //init controller
//        addBarangController=new AddBarangController(this,ItemsDataActivity.this);
//        editBarangController=new EditBarangController(this, ItemsDataActivity.this);
        kategoriController=new KategoriController(ItemsDataActivity.this,this, true);
        unitController=new UnitController(ItemsDataActivity.this,this, true);
        brandController=new BrandController(ItemsDataActivity.this, this, true);



        /* init sqlite db */
        db                          = new Database(context);

        /* Calendar */
        tz                          = TimeZone.getTimeZone("GMT+0700");

        /* init fonts */
        fonts                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                 = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_remark_items             = findViewById(R.id.tv_remark_items);
        tv_remark_price             = findViewById(R.id.tv_remark_hargabeli);
        tv_remark_price_sell        = findViewById(R.id.tv_remark_hargajual);
        tv_remark_unit              = findViewById(R.id.tv_remark_unit);
        tv_remark_brand             = findViewById(R.id.tv_remark_brand);
        tv_remark_kategori          = findViewById(R.id.tv_remark_kategori);
        tv_remark_barkode           = findViewById(R.id.tv_barkode);
        tv_stok                     = findViewById(R.id.tv_stok);
        tv_stok_minimum             = findViewById(R.id.tv_stok_minimum);

        /* init edittext */
        et_items                    = findViewById(R.id.et_items);
        et_price                    = findViewById(R.id.et_price);
        et_price_sell               = findViewById(R.id.et_price_sell);
        et_barkode                  = findViewById(R.id.et_barkode);
        et_stok                     = findViewById(R.id.et_stok);
        et_stok_minimum             = findViewById(R.id.et_stok_minimum);

        /* init spinner */
        sp_unit_id = findViewById(R.id.sp_unit_id);
        sp_brand_id = findViewById(R.id.sp_brand_id);
        sp_kategori = findViewById(R.id.sp_kategori);


        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);
        iv_preview_photo            = findViewById(R.id.iv_preview_photo);

        /* init button */
        btn_photo                   = findViewById(R.id.btn_photo);
        btn_save                    = findViewById(R.id.btn_save);

        /* init relative layout */
        rl_save                     = findViewById(R.id.rl_save);
        /* animation */
        fadeIn                      = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut                     = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        /* set fonts */
        this.applyFontBoldToTextView(tv_nav_header);
        tv_remark_items.setTypeface(fontsItalic);
        tv_remark_unit.setTypeface(fontsItalic);
        tv_remark_brand.setTypeface(fontsItalic);
        tv_remark_kategori.setTypeface(fontsItalic);
        tv_remark_price.setTypeface(fontsItalic);
        tv_remark_price_sell.setTypeface(fontsItalic);
        tv_remark_barkode.setTypeface(fontsItalic);
        tv_stok_minimum.setTypeface(fontsItalic);
        tv_stok.setTypeface(fontsItalic);
        btn_photo.setTypeface(fonts);
        btn_save.setTypeface(fonts);
        et_items.setTypeface(fonts);
        et_price.setTypeface(fonts);
        et_price_sell.setTypeface(fonts);
        et_barkode.setTypeface(fonts);
        et_stok.setTypeface(fonts);
        et_stok_minimum.setTypeface(fonts);


        btn_save.setEnabled(true);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDataActivity.super.onBackPressed();
            }
        });

        barang = null;

        //cek intent melalui extra
        //jika ada extra, berarti ini EDIT
        //jika tidak ada extra, berarti ini INSERT
        extras = getIntent().getExtras();

        if(extras != null){

            //jika ada extra, berarti ini EDIT
            Log.d(TAG, "Edit Mode");

            //ini terjadi jika mode Edit
            //readonly

            barang=new com.kitadigi.poskita.fragment.item.Datum(
                    extras.getString("id"),
                    extras.getString("category_id"),
                    extras.getString("sub_category_id"),
                    extras.getString("units_id"),
                    extras.getString("brands_id"),
                    extras.getString("types"),
                    extras.getString("code_product"),
                    extras.getString("name_product"),
                    extras.getString("brands_name"),
                    extras.getString("name_category"),
                    extras.getString("name_sub_category"),
                    extras.getString("units_name"),
                    extras.getString("image"),
                    extras.getString("purchase_price"),
                    extras.getString("sell_price"),
                    extras.getString("qty_stock"),
                    extras.getString("qty_minimum"),
                    extras.getString("additional"),
                    extras.getString("category_mobile_id"),
                    extras.getString("brand_mobile_id"),
                    extras.getString("unit_mobile_id"),
                    extras.getString("mobile_id")

            );

//            Log.d("brand_id", extras.getString("brands_id"));
//            Log.d("category_id", extras.getString("category_id"));
//            Log.d("units_id", extras.getString("units_id"));
//




            if (barang.getName_product()==null){
                et_items.setText("");
            }else{
                et_items.setText(barang.getName_product());
            }

            et_price.setText(barang.getPurchase_price().toString());
            et_price_sell.setText(barang.getSell_price().toString());

            if (barang.getCode_product()==null){
                et_barkode.setText("");
            }else{
                et_barkode.setText(barang.getCode_product().toString());
            }

            if (barang.getQty_stock()==null){
                et_stok.setText("0");
            }else{
                et_stok.setText(barang.getQty_stock());
            }

            if (barang.getQty_minimum()==null){
                et_stok_minimum.setText("0");
            }else{
                et_stok_minimum.setText(barang.getQty_minimum());
            }


            tv_nav_header.setText("Edit Items");
            btn_save.setText("Ubah");
            btn_photo.setText("Ubah Foto");

            //untuk mode edit
            //barkode dan stok tidak dapat diupdate
            et_barkode.setEnabled(false);
            et_stok.setEnabled(false);


            //cek apakah ada gambar
            if (barang.getImage().toString().matches("")  || barang.getImage()==null ){
                //jika tidak ada gambar
                //hilangkan iv_preview_photo
                iv_preview_photo.setVisibility(View.GONE);
            }else{

                Log.d("lokasi foto", barang.getImage());
                //tampilkan imageview
                iv_preview_photo.setVisibility(View.VISIBLE);

                //tampilkan gambar
                Picasso.with(ItemsDataActivity.this)
                        .load(new File(barang.getImage()))
                        .into(iv_preview_photo);

            }

            //variabel imageFilePath harus di-assign ke barang.getImage()
            //supaya jika commit edit, tinggal pass parameter imageFilePath ke EditBarangController.java
            imageFilePath = barang.getImage();

            //assign var id_item dengan intent yang berasal dari BarangAdapter.java
            id_item = barang.getId();

        }else{
            Log.d(TAG, "null");
        }


        //get untuk semua spinner
        //dimulai dari kategori
        //sesudah deteksi apakah ini edit atau insert
        kategoriController.getKategoriList();

        et_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_items, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_price, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_price_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_price_sell, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        //tombol ambil foto dari kamera/galeri
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                //saat diklik, var 'klik' bertipe boolean
                //harus value = true
                klik = true;

                //waktu diklik, harus dapat izin dari user dulu
                //ask permission
                if(ContextCompat.checkSelfPermission(ItemsDataActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

                    //,munculkan dialog kepada user
                    // apakah memilih kamera atau galeri?
                    dialogKameraGaleri();

                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }
            }



        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_text = btn_save.getText().toString();
                if(btn_text.toLowerCase().equals("save")){
                    saveItem();
                }else if(btn_text.toLowerCase().equals("ubah")){
                    editItem();
                }

            }
        });

        //init imagebutton
        ib_barkode                  = (ImageButton)findViewById(R.id.ib_barkode);
        //jika diklik akan scan barkode
        ib_barkode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //var klik harus diset false
                //soalnya tombol ib_barkode diklik
                //supaya di onRequestPermission-nya langsung scan barkode
                //kalau true, yang muncul dialog ambil foto kamera/galeri
                klik=false;


                //scan barkode
                //minta izin user untuk ambil kamera/galeri
                //waktu diklik, harus dapat izin dari user dulu
                //ask permission
               mintaPermission();

                //kode lanjut ke requestPermission


            }
        });



//        mintaPermission();
    }


    void mintaPermission(){
        //minta izin user untuk ambil kamera/galeri
        //waktu diklik, harus dapat izin dari user dulu
        //ask permission
        if(ContextCompat.checkSelfPermission(ItemsDataActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            qrScan.initiateScan();
        }
        else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.izin_akses_kamera_diperlukan), Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
        }
    }
    void dialogKameraGaleri(){
        //,munculkan dialog kepada user
        // apakah memilih kamera atau galeri?
        new SweetAlertDialog(ItemsDataActivity.this, SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText(getResources().getString(R.string.ambil_foto))
                .setContentText(getResources().getString(R.string.kamera_atau_galeri))
                .setConfirmText(getResources().getString(R.string.kamera))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        dispatchTakenPictureIntent();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText(getResources().getString(R.string.galeri))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_RESULT);
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void dispatchTakenPictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,getResources().getString(R.string.provider), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_RESULT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_RESULT:{
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    iv_preview_photo.setVisibility(View.VISIBLE);

                    Glide.with(this).load(imageFilePath).into(iv_preview_photo);
                    Log.d(TAG, "imageFilePath " + imageFilePath);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, getResources().getString(R.string.gagal_mengambil_gambar),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case GALLERY_RESULT:{
                if (requestCode == GALLERY_RESULT && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageFilePath = cursor.getString(columnIndex);
                    cursor.close();

                    //tampilkan foto di image_view
                    Glide.with(this).load(imageFilePath).into(iv_preview_photo);


                    iv_preview_photo.setVisibility(View.VISIBLE);
                    Log.d(TAG, "imageFilePath " + imageFilePath);

                    // String picturePath contains the path of selected Image
                }
                break;
            }

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }


        //intent untuk scan barkode
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, getResources().getString(R.string.barkode_tidak_ditemukan), Toast.LENGTH_LONG).show();
            } else {
                et_barkode.setText(result.getContents().toString());
//                Toast.makeText(ItemsDataActivity.this, result.getContents().toString(),Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                //cek dulu apakah btn 'pilih galeri/kamera' diklik
                if (klik){
                    //jika btn diklik, tampilkan dialog kamera/galeri
                    dialogKameraGaleri();
                }else{
                    qrScan.initiateScan();
                }

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "DCIM/POS/Photos");

        if (!folder.exists()) {
            folder.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                folder      /* directory */
        );

        //                    Blocked by Prima
//                    ==================================================================
//                    ==================================================================
        imageFilePath = image.getAbsolutePath();
        Log.d("imageFilePath", imageFilePath);
        return image;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void showAlertDialog(){
        if(!sweetAlertDialog.isShowing()){
            sweetAlertDialog.show();
        }
    }

    public void hideAlertDialog(){
        if(sweetAlertDialog != null && sweetAlertDialog.isShowing()){
            sweetAlertDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(AddBarangResult addBarangResult) {
        if (this.sessionExpired(addBarangResult.getMessage()) == 0){
//            Toast.makeText(ItemsDataActivity.this, addBarangResult.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
//         Toast.makeText(ItemsDataActivity.this, error,Toast.LENGTH_LONG).show();
         //kalau duplikasi, jangan finish()
         if (error.equals(getResources().getString(R.string.duplikasi_item))){

         }else{
             finish();
         }
    }


    void saveItem(){

        Bitmap bitmap = null;

        if (TextUtils.isEmpty(et_items.getText()) || TextUtils.isEmpty(et_price_sell.getText()) || sp_unit_id.getSelectedItem()==null ){

            Toast.makeText(ItemsDataActivity.this, getResources().getString(R.string.validasi_add_barang),Toast.LENGTH_SHORT).show();
        }else{

            if (!et_stok.getText().toString().matches("") && et_stok_minimum.getText().toString().matches("")){
                Toast.makeText(ItemsDataActivity.this,getResources().getString(R.string.stok_minimum_belum_diisi),Toast.LENGTH_SHORT).show();
            }else{

                Log.d("simpan item","SIMPAN!!!");
                Map<String, RequestBody> map = new HashMap<>();

                File file, file_image_ready;
                MultipartBody.Part body;
                if (iv_preview_photo.getVisibility()==View.GONE || iv_preview_photo.getVisibility()==View.INVISIBLE){

                    //null image, karena user tidak mengambil kamera
                    body=null;
                }else{
                    //jika ada gambar/foto
                    //ambil bitmap dari iv_preview_photo
                    iv_preview_photo.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) iv_preview_photo.getDrawable();
                    bitmap = drawable.getBitmap();


                    file = new File(imageFilePath);

                    String image_ready= ResizeImage.resizeAndCompressImageBeforeSend(ItemsDataActivity.this, file.getPath(), "Pic.jpg");
                    file_image_ready = new File(image_ready);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                    body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                    Log.d("request body ", map.toString());
                }


                String name_product = et_items.getText().toString();
                RequestBody name_product_request =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, name_product);

                String code_product;
                if (et_barkode.getText().toString().matches("")){
                    code_product=null;
                }else{
                    code_product=et_barkode.getText().toString();
                }
                Integer purchase_price = TextUtils.isEmpty(et_price.getText().toString()) ? 0: Integer.parseInt(et_price.getText().toString());
                Integer sell_price= TextUtils.isEmpty(et_price_sell.getText().toString()) ? 0: Integer.parseInt(et_price_sell.getText().toString());
                Integer qty_stok = TextUtils.isEmpty(et_stok.getText().toString()) ? 0: Integer.parseInt(et_stok.getText().toString());
                Integer qty_minimum = TextUtils.isEmpty(et_stok_minimum.getText().toString()) ? 0: Integer.parseInt(et_stok_minimum.getText().toString());


                addBarangController=new AddBarangController(ItemsDataActivity.this, this, name_product,imageFilePath, true);
                addBarangController.addBarang(body,
                        name_product_request,
                        id_brand,
                        id_kategori,
                        id_unit,
                        purchase_price,
                        sell_price,
                        code_product,
                        qty_stok,
                        qty_minimum,
                        category_mobile_id,
                        brand_mobile_id,
                        unit_mobile_id

                );

            }


        }


    }

    void editItem() {

        if (TextUtils.isEmpty(et_items.getText()) || TextUtils.isEmpty(et_price_sell.getText()) || sp_unit_id.getSelectedItem()==null ){

            Toast.makeText(ItemsDataActivity.this, getResources().getString(R.string.validasi_add_barang),Toast.LENGTH_SHORT).show();
        }else{


            if (!et_stok.getText().toString().matches("") && et_stok_minimum.getText().toString().matches("")){
                Toast.makeText(ItemsDataActivity.this,getResources().getString(R.string.stok_minimum_belum_diisi),Toast.LENGTH_SHORT).show();
            }else{


                Log.d("edit item","EDIT!!!");
                Map<String, RequestBody> map = new HashMap<>();

                File file, file_image_ready;
                MultipartBody.Part body;
                if (iv_preview_photo.getVisibility()==View.GONE || iv_preview_photo.getVisibility()==View.INVISIBLE){

                    //null image, karena user tidak mengambil kamera
                    body=null;
                }else{

                    //jika user tidak mengganti image
                    //maka imageFilePath diisi dengan file yang dicapture dari URL(barang.getImage())
                    if (imageFilePath==""){
                        Log.d("url gambar", barang.getImage());
                        try {
                            imageFilePath= UrlToBitmap.saveDrawFromImageView(iv_preview_photo);
                            Log.d("imageFilePath" , imageFilePath);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    file = new File(imageFilePath);

                    String image_ready= ResizeImage.resizeAndCompressImageBeforeSend(ItemsDataActivity.this, file.getPath(), "Pic.jpg");
                    file_image_ready = new File(image_ready);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                    body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                    Log.d("request body edit", map.toString());
                }


                String name_product = et_items.getText().toString();
                RequestBody name_product_request =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, name_product);

                String code_product = et_barkode.getText().toString();
                Integer purchase_price = et_price.getText().toString().matches("") ? 0: Integer.parseInt(et_price.getText().toString());
                Integer sell_price= et_price_sell.getText().toString().matches("") ? 0: Integer.parseInt(et_price_sell.getText().toString());
                Integer qty_stok = et_stok.getText().toString().matches("") ? 0: Integer.parseInt(et_stok.getText().toString());
                Integer qty_minimum = et_stok_minimum.getText().toString().matches("") ? 0: Integer.parseInt(et_stok_minimum.getText().toString());


                editBarangController=new EditBarangController(ItemsDataActivity.this, this,name_product,imageFilePath, true);
                editBarangController.editBarang(
                        id_item,
                        body,
                        name_product_request,
                        id_brand,
                        id_kategori,
                        id_unit,
                        purchase_price,
                        sell_price,
                        code_product,
                        qty_stok,
                        qty_minimum,
                        category_mobile_id,
                        brand_mobile_id,
                        unit_mobile_id

                );

//                Toast.makeText(ItemsDataActivity.this, id_brand.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(ItemsDataActivity.this, id_kategori.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(ItemsDataActivity.this, id_unit.toString(),Toast.LENGTH_SHORT).show();
            }



        }


    }


    @Override
    public void onBrandSuccess(BrandModel brandModel,List<Brand> brandOffline) {

//        if (brandModel==null){
//
//        }else{
//
//            this.sessionExpired(brandModel.getMessage());
//
//            //populasikan daftar kategori di dropdown brand
//            //===============================================================================
//
//            //buat hashmap dan counter
//            //untuk mode edit
//            int counter = 0;
//            HashMap<Integer,Integer> hash = new HashMap<>();
//
//            List<String> listBrand = new ArrayList<String>();
//            final List<Integer> idBrandList=new ArrayList<Integer>();
////            for( BrandData brandData:brandModel.getData()){
////                listBrand.add(brandData.getName());
////                idBrandList.add(Integer.parseInt(brandData.getId()));
////                hash.put(Integer.parseInt(brandData.getId()), counter);
////                counter = counter + 1;
////            }
//
//            for (Brand brand:brandOffline){
//                listBrand.add(brand.getName());
//                idBrandList.add(Integer.parseInt(brand.getId().toString()));
//                hash.put(Integer.parseInt(brand.getId().toString()), counter);
//                counter = counter + 1;
//            }
//            MySpinnerAdapter brandAdapter = new MySpinnerAdapter(
//                    ItemsDataActivity.this,
//                    R.layout.myspinner,
//                    listBrand
//            );
//            sp_brand_id.setAdapter(brandAdapter);
//            sp_brand_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    id_brand=idBrandList.get(position);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            if(barang!=null){
////                id_kategori=Integer.parseInt(barang.getCategory_id());
//                Integer selected = hash.get(Integer.parseInt(barang.getBrand_mobile_id()));
//                sp_brand_id.setSelection(selected);
//            }
//
//            //===============================================================================
//            //populasi selesai
//        }
    }

    @Override
    public void onBrandError(String error,List<Brand> brandOffline) {


        //find id
        sp_brand_id=(Spinner)findViewById(R.id.sp_brand_id);

        //buat hashmap dan counter
        //untuk mode edit


        int counter = 0;

        //hashmap untuk pasangan mobile_id dan counter
        HashMap<String,Integer> hash = new HashMap<>();

        //array untuk nampung mobile_id dari sqlite
        final List<String> listBrand = new ArrayList<String>();

        //array untuk nampung nama, nantinya ditampilkan di dropdown
        List<String> namaBrand = new ArrayList<>();

        final List<Integer> idBrandList=new ArrayList<Integer>();


        //looping dari sqlite
        for (Brand brand:brandOffline){

            //tambahkan mobileid ke array
            listBrand.add(brand.getKode_id());

            //tambahkan nama ke array
            namaBrand.add(brand.getName());

            idBrandList.add(Integer.parseInt(brand.getId().toString()));

            //hash untuk pasangan mobile_id dan counter
            hash.put(brand.getKode_id(), counter);

            counter = counter + 1;
        }

        //format dropdown
        MySpinnerAdapter brandAdapter = new MySpinnerAdapter(
                ItemsDataActivity.this,
                R.layout.myspinner,
                namaBrand
        );

        //set adapter
        sp_brand_id.setAdapter(brandAdapter);

        //jika user menubah dropdown
        sp_brand_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_brand=idBrandList.get(position);
                brand_mobile_id = listBrand.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //jika dalam mode EDIT
        if(barang!=null) {
            Integer selected = hash.get(barang.getBrand_mobile_id());
            sp_brand_id.setSelection(selected);


        }

    }

    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel,List<Kategori> kategoriOffline) {

    }

    @Override
    public void onKategoriError(String error,List<Kategori> kategoriOffline) {

        //populasikan daftar kategori di dropdown kategori
        //===============================================================================

        //buat hashmap dan counter
        //untuk mode edit
        int counter = 0;
        HashMap<String,Integer> hashKategori = new HashMap<>();

        //array ini untuk menyimpan mobile_id
        final List<String> listKategoriWithMobileID = new ArrayList<String>();

        //array ini untuk menyimpan nama, dan ditampilkan di dropdown
        List<String> namaKategori = new ArrayList<String>();

        final List<Integer> idKategoriList=new ArrayList<Integer>();

        //looping dari sqlite
        for(Kategori kategori:kategoriOffline){

            //tambahkan mobile_id di list
            listKategoriWithMobileID.add(kategori.getKode_id());

            //tambahkan nama di array
            namaKategori.add(kategori.getName_category());

//            idKategoriList.add(Integer.parseInt(kategori.getKode_id()));
//            hashKategori.put(Integer.parseInt(kategori.getKode_id()), counter);
            idKategoriList.add(Integer.parseInt(kategori.getId().toString()));

            //hash nyimpan mobile_id dan counter integer urut
            hashKategori.put(kategori.getKode_id(), counter);
            counter = counter + 1;
        }
        MySpinnerAdapter kategoriAdapter = new MySpinnerAdapter(
                ItemsDataActivity.this,
                R.layout.myspinner,
                namaKategori
        );
        sp_kategori=(Spinner)findViewById(R.id.sp_kategori);

        sp_kategori.setAdapter(kategoriAdapter);

        //jika dropdown dipilih user
        sp_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kategori=idKategoriList.get(position);
                category_mobile_id = listKategoriWithMobileID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //jika pada saat edit mode
        //dropdown harus sesuai indexnya
        if(barang!=null){
//                id_kategori=Integer.parseInt(barang.getCategory_id());
            Log.d("categorymid",barang.getCategory_mobile_id());
            Integer selected = hashKategori.get(barang.getCategory_mobile_id());
            sp_kategori.setSelection(selected);
        }

        //===============================================================================
        //populasi selesai
        //sesudah populasi kategori selesai, lanjutkan populasi unit
        //trigger data unit ke spinner
        unitController.getUnitList();
    }

    @Override
    public void onUnitSuccess(UnitModel unitModel,List<Unit> units) {

    }

    @Override
    public void onUnitError(String error,List<Unit> units) {


        //find id
        sp_unit_id=(Spinner)findViewById(R.id.sp_unit_id);


        int counter = 0;

        //buat hashmap untuk pasangan mobile_id dan integer index
        HashMap<String,Integer> hash = new HashMap<>();

        //array untuk nyimpen mobile_id
        final List<String> listUnit = new ArrayList<String>();

        //array untuk nyimpen nama, nanti yang akan ditampilkan di dropdown
        List<String> namaUnit = new ArrayList<>();

        final List<Integer> idUnitList=new ArrayList<Integer>();

        //looping data dari sqlite untuk ditampilkan di dropdown
        for(Unit unit:units){

            //tambahkan mobile id ke array
            listUnit.add(unit.getKode_id());

            //tambahkan nama ke array, ini nanti ditampilkan di dropdown
            namaUnit.add(unit.getName());


            idUnitList.add(Integer.parseInt(unit.getId().toString()));

            //pasangkan mobile_id dengan integer counter
            hash.put(unit.getKode_id(),counter);

            counter = counter + 1;
        }

        //format dropdown
        MySpinnerAdapter unitAdapter = new MySpinnerAdapter(
                ItemsDataActivity.this,
                R.layout.myspinner,
                namaUnit
        );

        //assignken array denga spinner
        sp_unit_id.setAdapter(unitAdapter);

        //jika user ganti spinner value
        sp_unit_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_unit=idUnitList.get(position);
                unit_mobile_id = listUnit.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //jika dalam mode EDIT
        if(barang!=null){
            Integer selected = hash.get(barang.getUnit_mobile_id());
            sp_unit_id.setSelection(selected);
        }
        //===============================================================================
        //populasi selesai
        //sesudah populasi kategori selesai, lanjutkan populasi brand


        brandController.getBrandList();
    }
}


