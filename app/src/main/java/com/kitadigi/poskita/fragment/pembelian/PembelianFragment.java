package com.kitadigi.poskita.fragment.pembelian;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.pembelian.SubPembelianActivity;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.beli.Beli;
import com.kitadigi.poskita.dao.beli.BeliHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.fragment.item.BarangController;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.IBarangResult;
import com.kitadigi.poskita.model.BeliModel;
import com.kitadigi.poskita.model.ListBeliModel;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.SubTotalModel;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.kitadigi.poskita.util.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

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
public class PembelianFragment extends BaseFragment implements IBarangResult {


    private static final String TAG = PembelianFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    Calendar calendar;
    SimpleDateFormat today;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_qty_data, tv_divider, tv_price_data;
    private RecyclerView recycler_view;
    private EditText et_search;
    private RelativeLayout rl_content, rl_button, rl_cart;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    /* Init Swipe Refresh */
    private SwipeRefreshLayout swipeRefreshLayout;

    //SessionManager untuk nyimpen data penjualan offline
    SessionManager sessionManager;


    //init controller
    BarangController barangController;

    //init sqlite
    ItemHelper itemHelper;
    BeliHelper beliHelper;

    //items
//    private List<Items> items = new ArrayList<>();
//    private List<Items> itemsAll = new ArrayList<>();
//    private ItemsAddNewAdapter itemsAdapter;
//    private List<Items> itemsSelect = new ArrayList<>();
//    private ItemsSelectAdapter itemsSelectAdapter;

    //variable
    int check, checkTransaction, checkItems, checkTemp, checkAddress;
    String jsonListItems;
    String jsonTransaction;
    String now;
    String kodeTransaction;
    int totalQty;
    int total;
    String jsonListAll;

    private SweetAlertDialog sweetAlertDialog;
    private AlertDialog alertDialog;
    LayoutInflater inflater = null;

    public PembelianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pembelian, container, false);
        initMain(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        final MainActivity mainActivity = (MainActivity) getActivity();

        tampilkanKeranjang();

    }

    void tampilkanKeranjang(){
        //cari subtotal pembelian offline
        final SubTotalModel subTotalModel;
        subTotalModel = sessionManager.sumTotalPembelianOffline();

        //cek apakah ada pembelian
        if(subTotalModel.getSum_total() != 0){

            DecimalFormat formatter     = new DecimalFormat("#,###,###");
            totalQty                    = subTotalModel.getSum_qty();
            total                       = subTotalModel.getSum_total();

            String price                = formatter.format(total);

            tv_qty_data.setText(totalQty + " items");
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

    private void initMain(View view) {

        //init sqlite
        itemHelper=new ItemHelper(getActivity());
        beliHelper=new BeliHelper(getActivity());

        List<Beli> belis = beliHelper.semuaBeli();
        for(Beli beli:belis){
            Log.d("item", beli.getId_product_master());
            Log.d("qty", beli.getQty().toString());
            Log.d("total", beli.getQty().toString());
        }

        //session manager untuk myimpen penjualan offline
        sessionManager = new SessionManager(getActivity());

        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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
        rl_cart                         = view.findViewById(R.id.rl_cartBeli);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);



        this.applyFontItalicToEditText(et_search);
        this.applyFontRegularToTextView(tv_qty_data);
        this.applyFontRegularToTextView(tv_divider);
        this.applyFontRegularToTextView(tv_price_data);

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
                ListBeliModel listBeliModel = sessionManager.getPembelianOffline();

                //urutkan penjualan berdasarkan id produk, sebelum di-grup
                Collections.sort(listBeliModel.getBeliModels(), BeliModel.beliModelComparator);

                //groupkan pembelian berdasarkan id

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
                for(BeliModel beliModel: listBeliModel.getBeliModels()){
//                    Log.d("item urut", jualModel.getId() + " - " + jualModel.getName_product() + " - " + String.valueOf(jualModel.getQty()) + " - " + String.valueOf(jualModel.getSell_price()));

                    if (counter==0){

                        //jika baru pertama kali looping
                        transactionsDetail=new TransactionsDetail();
                        transactionsDetail.setQty(beliModel.getQty().toString());
                        transactionsDetail.setIdItems(beliModel.getId());
                        transactionsDetail.setItemsName(beliModel.getName_product());
                        transactionsDetail.setNo(String.valueOf(counter+1));
                        transactionsDetail.setItemsPrice(beliModel.getPurchase_price().toString());
                        transactionsDetails.getTransactionsDetails().add(transactionsDetail);
                    }else{

                        //jika looping ke2,3, dstr
                        jmlTransaksiDetail = transactionsDetails.getTransactionsDetails().size();
                        transactionsDetail=transactionsDetails.getTransactionsDetails().get(jmlTransaksiDetail-1);
                        if (beliModel.getId().toString().matches(transactionsDetail.getIdItems())){

                            //tinggal tambahkan qtynya
                            qtyTransaksiDetail = Integer.parseInt(transactionsDetail.getQty());
                            qtyBaru = beliModel.getQty() + qtyTransaksiDetail;
                            transactionsDetail.setQty(String.valueOf(qtyBaru));

                        }else{

                            //tambahkan transaksi detail ke array
                            transactionsDetail=new TransactionsDetail();
                            transactionsDetail.setIdItems(beliModel.getId());
                            transactionsDetail.setQty(beliModel.getQty().toString());
                            transactionsDetail.setItemsName(beliModel.getName_product());
                            transactionsDetail.setNo(String.valueOf(counter+1));
                            transactionsDetail.setItemsPrice(beliModel.getPurchase_price().toString());
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
                sessionManager.gantiPembelianOffline(transactionsDetails.getTransactionsDetails());

                //pindah ke layar SubTransaksiActivity
                //dengan membawa intent list_transaksi_detail
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), SubPembelianActivity.class);
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

        //tarik data produk dari server
        refreshData();



    }

    void refreshData(){
        barangController=new BarangController(getActivity(),this, true);
        barangController.getBarang();
    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {
        if (this.sessionExpired(barangResult.getMessage())==0){

            //urutkan nama produk
//            Collections.sort(stokModel.getData(), StokDatum.datumComparator);

            //siapkan recyclerview
            //siapkan data untuk recycler view

            recycler_view.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
            recycler_view.setLayoutManager(mLayoutManager);

            final PembelianSyncAdapter pembelianAdapter =new PembelianSyncAdapter(getActivity(),items, this);
            pembelianAdapter.notifyDataSetChanged();
            recycler_view.setAdapter(pembelianAdapter);

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
                    pembelianAdapter.getFilter().filter(s.toString());
                }
            });

        }
    }

    @Override
    public void onError(String error, List<Item> items) {
        recycler_view.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view.setLayoutManager(mLayoutManager);

        final PembelianSyncAdapter pembelianAdapter =new PembelianSyncAdapter(getActivity(),items, this);
        pembelianAdapter.notifyDataSetChanged();
        recycler_view.setAdapter(pembelianAdapter);

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
                pembelianAdapter.getFilter().filter(s.toString());
            }
        });
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
        np_qty.setMaxValue(qty_available);



        String price;
        if (itemsPrice.matches("") || itemsPrice==null){
            price="0";
        }else{
            price = formatter.format(Integer.parseInt(itemsPrice));
        }


        this.applyFontBoldToTextView(tv_items);
        this.applyFontRegularToTextView(tv_price);
        this.applyFontRegularToTextView(tv_qty);

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

        this.applyFontBoldToButton(btn_cancel);
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
        this.applyFontBoldToButton(btn_add);
        btn_add.setText(getActivity().getResources().getString(R.string.tambah));
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BeliModel beliModel = new BeliModel(idItems,itemsName,Integer.parseInt(itemsPrice),qty_available,np_qty.getValue());
                sessionManager.tambahItemPembelianOffline(beliModel);
                alertDialog.hide();
                alertDialog.dismiss();

                tampilkanKeranjang();


            }
        });

        alertDialog.show();
    }


}
