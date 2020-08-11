package com.kitadigi.poskita.fragment.pos;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.pos.SubTransaksiActivity;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.POSFragment;
import com.kitadigi.poskita.model.Items;
import com.kitadigi.poskita.model.JualModel;
import com.kitadigi.poskita.model.ListJualModel;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.SubTotalModel;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class JualFragment extends BaseFragment implements IStokResult {


    private final int CAMERA_RESULT = 101;

    private static final String TAG = POSFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private POSFragment.OnFragmentInteractionListener mListener;

    Calendar calendar;
    SimpleDateFormat today;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_qty_data, tv_divider, tv_price_data;
    private RecyclerView recycler_view;
    private EditText et_search;
    private RelativeLayout rl_content, rl_button, rl_cart;
    private FloatingActionButton fab;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    /* Init Swipe Refresh */
    private SwipeRefreshLayout swipeRefreshLayout;

    //SessionManager untuk nyimpen data penjualan offline
    SessionManager sessionManager;

    //database
    private Database db;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;


    //init controller
    StokController stokController;


    //variable
    int check, checkTransaction, checkItems, checkTemp, checkAddress;
    String now;
    int totalQty;
    int total;
    String jsonListAll;

    private SweetAlertDialog sweetAlertDialog;
    private AlertDialog alertDialog;
    LayoutInflater inflater = null;

    //init scanner barkode
    IntentIntegrator intentIntegrator;

    public JualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jual, container, false);
        initMain(view);
        return view;
    }


    void tampilkanKeranjang(){

        //cari subtotal penjualan offline
        final SubTotalModel subTotalModel;
        subTotalModel = sessionManager.sumTotalPenjualanOffline();

        //cek apakah ada penjualan

        if(subTotalModel.getSum_total() != 0){

            DecimalFormat formatter     = new DecimalFormat("#,###,###");
            totalQty                    = subTotalModel.getSum_qty();
            total                       = subTotalModel.getSum_total();

            String price                = formatter.format(total);

            tv_qty_data.setText(totalQty + " item");
            tv_price_data.setText("Rp " + price);

            rl_button.setVisibility(View.VISIBLE);
            rl_cart.setVisibility(View.VISIBLE);
            Animation animation1 =
                    AnimationUtils.loadAnimation(getActivity(),
                            R.anim.fade_in);
            rl_button.startAnimation(animation1);
            rl_cart.startAnimation(animation1);
            et_search.setText("");

        }else{

            rl_button.setVisibility(View.VISIBLE);
            rl_cart.setVisibility(View.VISIBLE);
            Animation animation1 =
                    AnimationUtils.loadAnimation(getActivity(),
                            R.anim.fade_out);
            rl_button.startAnimation(animation1);
            rl_cart.startAnimation(animation1);

            rl_button.setVisibility(View.GONE);
            rl_cart.setVisibility(View.GONE);


        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final MainActivity mainActivity = (MainActivity) getActivity();

        tampilkanKeranjang();
    }

    private void initMain(View view) {


        //session manager untuk myimpen penjualan offline
        sessionManager = new SessionManager(getActivity());


        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //init database
        db                              = new Database(context);

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        TimeZone tz                     = TimeZone.getTimeZone("GMT+0700");
        calendar                        = Calendar.getInstance(tz);

        today                           = new SimpleDateFormat("yyyy-MM-dd");
        now                             = today.format(calendar.getTime());


        //init view
        tv_qty_data                     = view.findViewById(R.id.tv_qty_data);
        tv_divider                      = view.findViewById(R.id.tv_divider);
        tv_price_data                   = view.findViewById(R.id.tv_price_data);
        recycler_view                   = view.findViewById(R.id.rv_items);
        et_search                       = view.findViewById(R.id.et_search);
        rl_content                      = view.findViewById(R.id.rl_content);
        rl_button                       = view.findViewById(R.id.rl_button);
        rl_cart                         = view.findViewById(R.id.rl_cart);
        fab                             = view.findViewById(R.id.fab);


        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        et_search.setTypeface(fontsItalic);
        tv_qty_data.setTypeface(fonts);
        tv_divider.setTypeface(fonts);
        tv_price_data.setTypeface(fonts);


        et_search.setVisibility(View.INVISIBLE);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        });





        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get item penjualan dari session
                ListJualModel listJualModel = sessionManager.getPenjualanOffline();

                //urutkan penjualan berdasarkan id produk, sebelum di-grup
                Collections.sort(listJualModel.getJualModels(), JualModel.jualModelComparator);

                //groupkan penjualan berdasarkan id

                //variabel untuk perulangan
                int counter = 0;

                //variabel untuk menghitung jumlah transaksi detail (array size)
                int jmlTransaksiDetail = 0;

                //variabel untuk mengetahui jumlah item pada variabel transaksi detail
                int qtyTransaksiDetail = 0;

                //variabel sebagai hasil
                int qtyBaru = 0;


                //buat list dari transaksi detail, nantinya di-intent ke activity SubTransaksiActivity
                ListTransactionDetail transactionsDetails=new ListTransactionDetail();
                List<TransactionsDetail> detailList = new ArrayList<>();
                transactionsDetails.setTransactionsDetails(detailList);

                //variabel transaksi detail
                TransactionsDetail transactionsDetail;

                //looping jualmodel dari session
                //untuk digrouping ke list transaksi detail
                for(JualModel jualModel:listJualModel.getJualModels()){
                    Log.d("item urut", jualModel.getId() + " - " + jualModel.getName_product() + " - " + String.valueOf(jualModel.getQty()) + " - " + String.valueOf(jualModel.getSell_price()));

                    if (counter==0){

                        //jika baru pertama kali looping
                        transactionsDetail=new TransactionsDetail();
                        transactionsDetail.setQty(jualModel.getQty().toString());
                        transactionsDetail.setIdItems(jualModel.getId());
                        transactionsDetail.setItemsName(jualModel.getName_product());
                        transactionsDetail.setNo(String.valueOf(counter+1));
                        transactionsDetail.setItemsPrice(jualModel.getSell_price().toString());
                        transactionsDetails.getTransactionsDetails().add(transactionsDetail);
                    }else{

                        //jika looping ke2,3, dstr
                        jmlTransaksiDetail = transactionsDetails.getTransactionsDetails().size();
                        transactionsDetail=transactionsDetails.getTransactionsDetails().get(jmlTransaksiDetail-1);
                        if (jualModel.getId().toString().matches(transactionsDetail.getIdItems())){

                            //tinggal tambahkan qtynya
                            qtyTransaksiDetail = Integer.parseInt(transactionsDetail.getQty());
                            qtyBaru = jualModel.getQty() + qtyTransaksiDetail;
                            transactionsDetail.setQty(String.valueOf(qtyBaru));

                        }else{

                            //tambahkan transaksi detail ke array
                            transactionsDetail=new TransactionsDetail();
                            transactionsDetail.setIdItems(jualModel.getId());
                            transactionsDetail.setQty(jualModel.getQty().toString());
                            transactionsDetail.setItemsName(jualModel.getName_product());
                            transactionsDetail.setNo(String.valueOf(counter+1));
                            transactionsDetail.setItemsPrice(jualModel.getSell_price().toString());
                            transactionsDetails.getTransactionsDetails().add(transactionsDetail);
                        }
                    }
                    counter = counter + 1;
                }

//               Toast.makeText(getActivity(),String.valueOf(transactionsDetails.getTransactionsDetails().size()),Toast.LENGTH_SHORT).show();
                for (TransactionsDetail a :transactionsDetails.getTransactionsDetails()){
                    Log.d("hasil urut", a.getIdItems() + " - " + a.getItemsName() + " - " + a.getQty() + " - " + a.getItemsPrice());
                }

                //sebelum pindah kelayar SubTransaksiActivity,
                //langsung hasil group di atas, diupdate ke sessionManager
                sessionManager.gantiPenjualanOffline(transactionsDetails.getTransactionsDetails());

                //pindah ke layar SubTransaksiActivity
                //dengan membawa intent list_transaksi_detail
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), SubTransaksiActivity.class);
                intent.putExtra("list_transaksi_detail", gson.toJson(transactionsDetails));
                startActivityForResult(intent, MainActivity.REQUEST_HOME);


            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                onResume();

            }
        });

        check                           = db.getCountListItems();
        if(check == 0){

        }

        checkAddress                    = db.getCountListAddress();
        if(checkAddress == 0){
            db.addAdress("John Doe", "Jl Aren No 29 Jatipulo Jakarta Barat", "081234567890");
        }


//        checkTemp                       = db.getCountListItemsTempTipe("0");
//        if (checkTemp == 0){
//            db.addListItemsTemp("0", "Add Shortcuts", "", "","ic_plus", "","0", "1");
//            db.addListItemsTemp("0","Add Custom\nPenjualan", "", "","ic_plus", "","3", "1");
//            db.addListItemsTemp("1","Gulaku 1KG", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2017/1/22/2833458/2833458_3d1a6760-03cf-4227-b23c-0723df51040a_2048_2048.jpg", "Gulaku 1KG","1", "1");
//            db.addListItemsTemp("2","Bimoli 1L", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2016/5/16/9320291/9320291_ec3262ab-9a6e-4944-8362-40440c0440a9.jpg", "Bimoli 1L","1", "1");
//            db.addListItemsTemp("3","Beras Rojolele 10KG", "10000", "15000","https://www.lifull-produk.id/bundles/assets/img/product/1440560791abc_kecap_manis_pouch_580ml-_1.jpg", "Beras Rojolele 10KG","1", "1");
//            db.addListItemsTemp("4","Kecap Manis ABC 580ML", "10000", "15000","https://assets.klikindomaret.com/products/20063269/20063269_1.jpg", "Kecap Manis ABC 580ML","1", "1");
//            db.addListItemsTemp("5","Sabun Cair Nuvo 450ML", "10000", "15000","https://www.rinso.com/content/dam/unilever/dirt_is_good/indonesia/pack_shot/front/laundry/fabrics_cleaning/rinso_deterjen_bubuk_anti_noda_450gr/rinso_anti_noda_12x430g-front-1195144.png", "Sabun Cair Nuvo 450ML","1", "1");
//            db.addListItemsTemp("6","Sabun Cuci Rinso 475GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Sabun Cuci Rinso 475GR","1", "1");
//            db.addListItemsTemp("7","Desaku Bubuk Balado 11GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Desaku Bubuk Balado 11GR","1", "1");
//            db.addListItemsTemp("8","Masako Ayam 100GR", "10000", "15000","https://assets.klikindomaret.com/products/10035898/10035898_1.jpg", "Masako Ayam 100GR","1", "1");
//
//            db.addListItemsTemp("1","Gulaku 1KG", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2017/1/22/2833458/2833458_3d1a6760-03cf-4227-b23c-0723df51040a_2048_2048.jpg", "Gulaku 1KG","2", "1");
//            db.addListItemsTemp("2","Bimoli 1L", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2016/5/16/9320291/9320291_ec3262ab-9a6e-4944-8362-40440c0440a9.jpg", "Bimoli 1L","2", "1");
//            db.addListItemsTemp("3","Beras Rojolele 10KG", "10000", "15000","https://www.lifull-produk.id/bundles/assets/img/product/1440560791abc_kecap_manis_pouch_580ml-_1.jpg", "Beras Rojolele 10KG","2", "1");
//            db.addListItemsTemp("4","Kecap Manis ABC 580ML", "10000", "15000","https://assets.klikindomaret.com/products/20063269/20063269_1.jpg", "Kecap Manis ABC 580ML","2", "1");
//            db.addListItemsTemp("5","Sabun Cair Nuvo 450ML", "10000", "15000","https://www.rinso.com/content/dam/unilever/dirt_is_good/indonesia/pack_shot/front/laundry/fabrics_cleaning/rinso_deterjen_bubuk_anti_noda_450gr/rinso_anti_noda_12x430g-front-1195144.png", "Sabun Cair Nuvo 450ML","2", "1");
//            db.addListItemsTemp("6","Sabun Cuci Rinso 475GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Sabun Cuci Rinso 475GR","2", "1");
//            db.addListItemsTemp("7","Desaku Bubuk Balado 11GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Desaku Bubuk Balado 11GR","2", "1");
//            db.addListItemsTemp("8","Masako Ayam 100GR", "10000", "15000","https://assets.klikindomaret.com/products/10035898/10035898_1.jpg", "Masako Ayam 100GR","2", "1");
//        }


        //tombol untuk scan barkode
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //scan barkode
                //minta izin user untuk ambil kamera/galeri
                //waktu diklik, harus dapat izin dari user dulu
                //ask permission
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    IntentIntegrator.forSupportFragment(JualFragment.this).initiateScan();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.izin_akses_kamera_diperlukan), Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }

                //kode lanjut ke requestPermission

            }
        });

        //tarik data produk dari server
        refreshData();


    }



    void refreshData(){
        stokController=new StokController(this,getActivity(), true);
        stokController.getStok();
    }

    @Override
    public void onStokSuccess(StokModel stokModel,List<Stok> stokOffline) {
        if (this.sessionExpired(stokModel.getMessage())==0){

            //urutkan nama produk

//            Collections.sort(stokModel.getData(), StokDatum.datumComparator);

            //siapkan recyclerview
            //siapkan data untuk recycler view

            recycler_view.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
            recycler_view.setLayoutManager(mLayoutManager);

            final StokSyncAdapter stokSyncAdapter =new StokSyncAdapter(getActivity(),stokOffline,this);
            stokSyncAdapter.notifyDataSetChanged();
            recycler_view.setAdapter(stokSyncAdapter);

            et_search.setVisibility(View.VISIBLE);

            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    stokSyncAdapter.getFilter().filter(s.toString());
                }
            });

        }
    }

    @Override
    public void onStokError(String error,List<Stok> stokOffline) {

        //siapkan recyclerview
        //siapkan data untuk recycler view

        recycler_view.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view.setLayoutManager(mLayoutManager);

        final StokSyncAdapter stokSyncAdapter =new StokSyncAdapter(getActivity(),stokOffline,this);
        stokSyncAdapter.notifyDataSetChanged();
        recycler_view.setAdapter(stokSyncAdapter);

        et_search.setVisibility(View.VISIBLE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stokSyncAdapter.getFilter().filter(s.toString());
            }
        });
//        this.showToast(error);
    }


    public void setData(final String json) {

//        fetchListItems(json);
//        mShimmerViewContainer.setVisibility(View.VISIBLE);
//        mShimmerViewContainer.startShimmerAnimation();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fetchListItems(json);
//            }
//        }, 1500);

    }


    public void fetchListItems(String json){
//        items.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Hasil 2 => "+array.toString());
            Log.d(TAG, "Hasil 2 length => "+array.length());

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    Items item = new Items();
                    item.setIdItems(obj.getString("idItems"));
                    item.setItemsName(obj.getString("itemsName"));
                    item.setItemsPrice(obj.getString("itemsPrice"));
                    item.setItemsPriceSell(obj.getString("itemsPriceSell"));
                    item.setItemsImage(obj.getString("itemsImage"));
                    item.setItemsDescription(obj.getString("itemsDescription"));
                    item.setTipe(obj.getString("tipe"));
                    item.setShortcut(obj.getString("shortcut"));

//                    items.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            Collections.reverse(items);
//            itemsAdapter.notifyDataSetChanged();

            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
            rl_content.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBg));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addItems(final String idItems, final String itemsName, final String itemsPrice, final Bitmap itemsImage, final String tipe, final int qty_available){

        DecimalFormat formatter             = new DecimalFormat("#,###,###");
        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(getActivity());
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_add_items, null);
        ImageView iv_icon                   = dialogView.findViewById(R.id.iv_icon);
        ImageView iv_delete_shortcut        = dialogView.findViewById(R.id.iv_delete_shortcut);
        TextView tv_items                   = dialogView.findViewById(R.id.tv_items);
        TextView tv_price                   = dialogView.findViewById(R.id.tv_price);
        final TextView tv_qty               = dialogView.findViewById(R.id.tv_qty);
        final NumberPicker np_qty           = dialogView.findViewById(R.id.np_qty);

        np_qty.setMinValue(1);


        //validasi QTY===================================================================
        //kode berikut ini untuk menentukan berapa jumlah
        //item yang boleh diinput
        //mengacu pada stok
        //dan qty yang sudah diinput pada layar POS
        int qty_sudah_diinput = sessionManager.jumlahItemYangDiinputKePenjualanOffline(idItems);
//        int qty_sudah_dibeli = getJumlahBeli(idItems);

        //jumlah yang sudah dibeli di-0-kan dulu
        //soalnya sudah dihitung di StokSyncAdapter.java
        //di fungsi -> .rl_items.setOnClickListener
        int qty_sudah_dibeli = 0;

        int sisa = qty_sudah_dibeli + (qty_available - qty_sudah_diinput);
        Log.d("jumlah inputan", String.valueOf(qty_sudah_diinput));
        np_qty.setMaxValue(sisa);
        //validasi qty barang selesai====================================================
        //===============================================================================
        //===============================================================================



        String price;
        if (itemsPrice.matches("") || itemsPrice==null){
            price="0";
        }else{
            price = formatter.format(Integer.parseInt(itemsPrice));
        }



        tv_items.setTypeface(fontsBold);
        tv_price.setTypeface(fonts);
        tv_qty.setTypeface(fonts);

        Log.d(TAG, "ii " + tipe);
        tv_items.setText(itemsName);
        tv_price.setText("Rp. " + price);
        iv_icon.setImageBitmap(itemsImage);

        if(!tipe.equals("1")){
            iv_delete_shortcut.setVisibility(View.GONE);
        }

        iv_delete_shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setCancelConfirm("Hapus " + itemsName + "\ndari shortcut ?", idItems);
//                showAlertDialog();
            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog                         = dialogBuilder.create();

        Window window                       = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center
        // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom
        Button btn_cancel                   = dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(fontsBold);
        btn_cancel.setText(getActivity().getResources().getString(R.string.batal));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
            }
        });

        // Ssave Button

        Button btn_add                      = dialogView.findViewById(R.id.btn_add);
        btn_add.setTypeface(fontsBold);
        btn_add.setText(getActivity().getResources().getString(R.string.tambah));
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tambahkan item ke session sebagai jualmodel
                //karena sekarang ada mode OFFLINE,
                //maka sekarang id diganti dengan kode_id
                //lihat penggunaan kode ini di StokSyncAdapter.java, bukan di StokAdapter.java
                JualModel jualModel = new JualModel(idItems,itemsName,Integer.parseInt(itemsPrice),qty_available,np_qty.getValue());

                sessionManager.tambahItemPenjualanOffline(jualModel);

                alertDialog.hide();
                alertDialog.dismiss();

                tampilkanKeranjang();


            }
        });

        alertDialog.show();
    }

    //fungsi ini untuk mendapatkan jumlah item yang dibeli
    //dari tabel pembelian di sqlite
    //tapi hanya untuk yang di tabel master yang belum di-sync
//    public  Integer getJumlahBeli(String mobile_id){
//
//        Integer hasil=0;
//        String nomor;
//        Integer qty=0;
//        BeliMaster beliMaster;
//
//        //init database sqlite
//        BeliMasterHelper beliMasterHelper = new BeliMasterHelper(context);
//        BeliDetailHelper beliDetailHelper = new BeliDetailHelper(context);
//
//        //dapatkan semua item beli dari tabel beli detail
//        //yang sesuai parameter di atas (mobile_id)
//        List<BeliDetail> beliDetails = beliDetailHelper.getBeliDetailByMobileId(mobile_id);
//
//        //tampung jumlah dalam variabel
//        int jumlah = beliDetails.size();
//
//        //jika jumlah nya 0
//        //maka langsung return 0
//        if (jumlah ==0 ){
//            hasil = 0;
//        }else{
//
//            //looping list beli detail tadi
//            //cek satu persatu nomor trx-nya di tabel beli master
//            for (BeliDetail beliDetail: beliDetails){
//
//                //tampung nomor_trx dalam var nomor
//                nomor = beliDetail.getNomor();
//
//                //cari di tabel beli master
//                //cek apakah status belum sync atau sudah
//                beliMaster = beliMasterHelper.getBeliMasterByNomor(nomor);
//
//                //jika status sync belum
//                //masukkan ke dalam counter
//                if (beliMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){
//                    qty = Integer.parseInt(beliDetail.getQty());
//                    hasil = hasil + qty;
//                }
//            }
//
//        }
//
//        Log.d("jumlah beli", hasil.toString());
//        return hasil;
//    }

    //fungsi ini untuk mendapatkan jumlah item yang dijual
    //dari tabel penjualan di sqlite
    //tapi hanya untuk yang di tabel master yang belum di-sync
//    public  Integer getJumlahJual(String mobile_id){
//
//        Integer hasil=0;
//        String nomor;
//        Integer qty=0;
//        JualMaster jualMaster;
//
//        //init database sqlite
//        JualMasterHelper jualMasterHelper = new JualMasterHelper(context);
//        JualDetailHelper jualDetailHelper = new JualDetailHelper(context);
//
//        //dapatkan semua item jual dari tabel jual detail
//        //yang sesuai parameter di atas (mobile_id)
//        List<JualDetail> jualDetails = jualDetailHelper.getJualDetailByKodeId(mobile_id);
//
//        //tampung jumlah dalam variabel
//        int jumlah = jualDetails.size();
//
//        //jika jumlah nya 0
//        //maka langsung return 0
//        if (jumlah ==0 ){
//            hasil = 0;
//        }else{
//
//            //looping list jual detail tadi
//            //cek satu persatu nomor trx-nya di tabel jual master
//            for (JualDetail jualDetail : jualDetails){
//
//                //tampung nomor_trx dalam var nomor
//                nomor = jualDetail.getNomor();
//
//                //cari di tabel jual master
//                //cek apakah status belum sync atau sudah
//                jualMaster = jualMasterHelper.getJualMasterByNomor(nomor);
//
//                //jika status sync belum
//                //masukkan ke dalam counter
//                if (jualMaster.getSync_insert() == Constants.STATUS_BELUM_SYNC){
//                    qty = jualDetail.getQty();
//                    hasil = hasil + qty;
//                }
//            }
//
//        }
//
//        Log.d("jumlah jual", hasil.toString());
//        return hasil;
//    }




    //onActivityResult untuk callback barkode scanner
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //semua kode dibawah sebagai hasil tangkapan barkode scanner
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //cek apakah ada hasil scan
        if (result == null){

            //jika null/tidak ada


        }else {

            //tampung dalam variabel
            String barcode = result.getContents();

            //cari dalam sqlite, barkode yang di-scan
            //init dulu sqlite-nya
            ItemHelper itemHelper = new ItemHelper(getActivity());

            //tampung dalam class
            if (itemHelper.getItemByBarkode(barcode) == null){

                //jika tidak ada data barang yang sesuai barkode
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.barkode_tidak_ditemukan) + ":" + barcode, Toast.LENGTH_SHORT).show();
            }else{

                //jika ada data yang sesuai
                //tampung dalam variabel
                Item item = itemHelper.getItemByBarkode(barcode);

                //tampung semua properti item dalam variabel
                String kodeId = item.getKode_id().toString();
                String namaItem = item.getName_product().toString();
                String hargaJual = item.getSell_price().toString();
                String tipe = item.getTypes().toString();
                String pathFoto = item.getImage();
                Bitmap bitmap=null;

                //buat file baru dari pathFoto
                File imgFile = new File(pathFoto);
//
                //cek apakah ada file tersebut, buat jaga-jaga
                if(imgFile.exists()){
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                }

                //kode dibawah ini untuk menentukan berapa qty available
                //ditinjau dari jumlah pembelian, penjualan, item yang sudah dimasukkan dalam cart
                int qty_yang_sudah_diinput;
                int qty_total;
                int qty_available;
                int qty_stok_sekarang;

                qty_stok_sekarang = item.getQty_stock();

                //cari jumlah barang yang sudah di-add ke keranjang
                qty_yang_sudah_diinput = sessionManager.jumlahItemYangDiinputKePenjualanOffline(kodeId);

                //jumlahkan kedua variabel diatas, nantinya akan dijadikan pengurang
                qty_total = qty_stok_sekarang - qty_yang_sudah_diinput;

                //jika stok =0 atau jumlah stok sama dengan yang diinputkan
                //munculkan pesan, kalau overstok
                if ( qty_stok_sekarang <=0 ){
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.stok_kosong),Toast.LENGTH_SHORT).show();
                }else{
                    //jika lebih dari 0
                    //tampilkan dialog

                    //karena sekarang ada mode OFFLINE,
                    //maka fungsi addItems dibawah menggunakan kode_id,
                    //bukan id item lagi

                    //kurangkan qty_available pada stok dengan qty_total
                    qty_available = qty_total;

                    //tampilkan popup dari jualfragment.java
                    //lihat fungsi addItems()
                    //untuk parameter id pada fungsi jualFragment.addItems(), diganti dengan kode_id
                    //sehubungan dengan sinkronisasi tabel penjualan
//                    jualFragment.addItems(stok.getKode_id(),stok.getName_product(),stok.getSell_price(), bitmap,"", qty_available);
                    addItems(kodeId,namaItem,hargaJual,bitmap,tipe,qty_available);

                }
            }
        }

    }

    //jika user mengizinkan akses kamera
    //maka scan barkode
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                IntentIntegrator.forSupportFragment(JualFragment.this).initiateScan();
            }
            else{
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.izin_akses_kamera_diperlukan), Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
