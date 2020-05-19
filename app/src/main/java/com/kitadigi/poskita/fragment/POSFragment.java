package com.kitadigi.poskita.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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


import com.bumptech.glide.Glide;
import com.kitadigi.poskita.ItemsTransactionsActivity;
import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.ItemsAddNewAdapter;
import com.kitadigi.poskita.adapter.ItemsSelectAdapter;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.Items;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link POSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link POSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSFragment extends BaseFragment {
    private static final String TAG = POSFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private OnFragmentInteractionListener mListener;

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

    //database
    private Database db;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;

    //items
    private List<Items> items = new ArrayList<>();
    private List<Items> itemsAll = new ArrayList<>();
    //    private ItemsAddAdapter itemsAdapter;
    private ItemsAddNewAdapter itemsAdapter;
    private List<Items> itemsSelect = new ArrayList<>();
    private ItemsSelectAdapter itemsSelectAdapter;

    //variable
    int check, checkTransaction, checkItems, checkTemp, checkAddress;
    String jsonListItems, jsonTransaction, now, kodeTransaction, totalQty, total, jsonListAll;

    private SweetAlertDialog sweetAlertDialog;
    private AlertDialog alertDialog;
    LayoutInflater inflater = null;

    public POSFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSFragment newInstance(String param1, String param2) {
        POSFragment fragment = new POSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pos, container, false);
        initMain(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Init dashboard layout (info update)
     */
    private void initMain(View view) {

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

//        HashMap<String, String> getKode = db.getTransactionsNo(now);
//        kodeTransaction = getKode.get("no");
//
//        if(kodeTransaction != null){
//            kodeTransaction = String.valueOf(Integer.parseInt(kodeTransaction) + 1);
//        }else{
//            kodeTransaction = "1";
//        }
//
//        Log.d(TAG, "Kode, " + kodeTransaction);

        //init view
        tv_qty_data                     = view.findViewById(R.id.tv_qty_data);
        tv_divider                      = view.findViewById(R.id.tv_divider);
        tv_price_data                   = view.findViewById(R.id.tv_price_data);
        recycler_view                   = view.findViewById(R.id.rv_items);
        et_search                       = view.findViewById(R.id.et_search);
        rl_content                      = view.findViewById(R.id.rl_content);
        rl_button                       = view.findViewById(R.id.rl_button);
        rl_cart                         = view.findViewById(R.id.rl_cart);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        et_search.setTypeface(fontsItalic);
        tv_qty_data.setTypeface(fonts);
        tv_divider.setTypeface(fonts);
        tv_price_data.setTypeface(fonts);

        RecyclerView.LayoutManager mLayoutManager   = new GridLayoutManager(context, 3);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(3), true));
        itemsAdapter                                = new ItemsAddNewAdapter(context, items, itemsAll, this);
        recycler_view.setAdapter(itemsAdapter);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemsAdapter.getFilter().filter(s.toString());
            }
        });

//        jsonListItems                               = db.getListItems();
//        setData(jsonListItems);

        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transaction = new Intent(mainActivity, ItemsTransactionsActivity.class);
                jsonTransaction = db.getListTransactionsDetail("0", "1");
                transaction.putExtra("transaction", jsonTransaction);
                startActivityForResult(transaction, MainActivity.REQUEST_HOME);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl_button.setVisibility(View.GONE);
                        rl_cart.setVisibility(View.GONE);
                        //db.deleteItemsTemp();
                        jsonListItems                               = db.getListItemsTemp("2");
                        setData(jsonListItems);
                    }
                }, 1500);
            }
        });

        check                           = db.getCountListItems();
        if(check == 0){
            fetchMenu(context);
        }

        checkAddress                    = db.getCountListAddress();
        if(checkAddress == 0){
            db.addAdress("John Doe", "Jl Aren No 29 Jatipulo Jakarta Barat", "081234567890");
        }

        checkTemp                       = db.getCountListItemsTempTipe("0");
        if (checkTemp == 0){
            db.addListItemsTemp("0", "Add Shortcuts", "", "","ic_plus", "","0", "1");
            db.addListItemsTemp("0","Add Custom\nPenjualan", "", "","ic_plus", "","3", "1");
            db.addListItemsTemp("1","Gulaku 1KG", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2017/1/22/2833458/2833458_3d1a6760-03cf-4227-b23c-0723df51040a_2048_2048.jpg", "Gulaku 1KG","1", "1");
            db.addListItemsTemp("2","Bimoli 1L", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2016/5/16/9320291/9320291_ec3262ab-9a6e-4944-8362-40440c0440a9.jpg", "Bimoli 1L","1", "1");
            db.addListItemsTemp("3","Beras Rojolele 10KG", "10000", "15000","https://www.lifull-produk.id/bundles/assets/img/product/1440560791abc_kecap_manis_pouch_580ml-_1.jpg", "Beras Rojolele 10KG","1", "1");
            db.addListItemsTemp("4","Kecap Manis ABC 580ML", "10000", "15000","https://assets.klikindomaret.com/products/20063269/20063269_1.jpg", "Kecap Manis ABC 580ML","1", "1");
            db.addListItemsTemp("5","Sabun Cair Nuvo 450ML", "10000", "15000","https://www.rinso.com/content/dam/unilever/dirt_is_good/indonesia/pack_shot/front/laundry/fabrics_cleaning/rinso_deterjen_bubuk_anti_noda_450gr/rinso_anti_noda_12x430g-front-1195144.png", "Sabun Cair Nuvo 450ML","1", "1");
            db.addListItemsTemp("6","Sabun Cuci Rinso 475GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Sabun Cuci Rinso 475GR","1", "1");
            db.addListItemsTemp("7","Desaku Bubuk Balado 11GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Desaku Bubuk Balado 11GR","1", "1");
            db.addListItemsTemp("8","Masako Ayam 100GR", "10000", "15000","https://assets.klikindomaret.com/products/10035898/10035898_1.jpg", "Masako Ayam 100GR","1", "1");

            db.addListItemsTemp("1","Gulaku 1KG", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2017/1/22/2833458/2833458_3d1a6760-03cf-4227-b23c-0723df51040a_2048_2048.jpg", "Gulaku 1KG","2", "1");
            db.addListItemsTemp("2","Bimoli 1L", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2016/5/16/9320291/9320291_ec3262ab-9a6e-4944-8362-40440c0440a9.jpg", "Bimoli 1L","2", "1");
            db.addListItemsTemp("3","Beras Rojolele 10KG", "10000", "15000","https://www.lifull-produk.id/bundles/assets/img/product/1440560791abc_kecap_manis_pouch_580ml-_1.jpg", "Beras Rojolele 10KG","2", "1");
            db.addListItemsTemp("4","Kecap Manis ABC 580ML", "10000", "15000","https://assets.klikindomaret.com/products/20063269/20063269_1.jpg", "Kecap Manis ABC 580ML","2", "1");
            db.addListItemsTemp("5","Sabun Cair Nuvo 450ML", "10000", "15000","https://www.rinso.com/content/dam/unilever/dirt_is_good/indonesia/pack_shot/front/laundry/fabrics_cleaning/rinso_deterjen_bubuk_anti_noda_450gr/rinso_anti_noda_12x430g-front-1195144.png", "Sabun Cair Nuvo 450ML","2", "1");
            db.addListItemsTemp("6","Sabun Cuci Rinso 475GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Sabun Cuci Rinso 475GR","2", "1");
            db.addListItemsTemp("7","Desaku Bubuk Balado 11GR", "10000", "15000","https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/20/3607562/3607562_c91dbe15-117a-427d-8456-bae02e22a492_1620_1620.jpg", "Desaku Bubuk Balado 11GR","2", "1");
            db.addListItemsTemp("8","Masako Ayam 100GR", "10000", "15000","https://assets.klikindomaret.com/products/10035898/10035898_1.jpg", "Masako Ayam 100GR","2", "1");
        }

        jsonListItems                               = db.getListItemsTemp("2");
        setData(jsonListItems);
        jsonListAll                                 = db.getListItems();
        fetchListItemsAll(jsonListAll);
        //}

    }

    public void setData(final String json) {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListItems(json);
            }
        }, 1500);

    }

    public void setDataDialog(final String json, final ShimmerFrameLayout shimmerFrameLayout, final RecyclerView recyclerView, final RelativeLayout relativeLayout) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchListItemsDialog(json, shimmerFrameLayout, recyclerView, relativeLayout);
            }
        }, 1500);

    }

    private void fetchMenu (Context context){
        JSONArray array = null;
        String jsonfilename;
        try {
            jsonfilename = "json/listItems.json";
            array = new JSONArray(loadJSONFromAsset(context, jsonfilename));
            //Log.d(TAG, "Hasil 1 => " + array.toString());
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    db.addListItems(obj.getString("itemsName"), obj.getString("itemsPrice"), obj.getString("itemsPriceSell"), obj.getString("itemsImage"), obj.getString("itemsDescription"), "1", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            jsonListItems                               = db.getListItems();
//            setData(jsonListItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void fetchListItems(String json){
        items.clear();
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

                    items.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(items);
            itemsAdapter.notifyDataSetChanged();

            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
            rl_content.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBg));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchListItemsAll(String json){
        itemsAll.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Hasil 2 all => "+array.toString());
            Log.d(TAG, "Hasil 2 all length => "+array.length());

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
                    itemsAll.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchListItemsDialog(String json, ShimmerFrameLayout shimmerFrameLayout, RecyclerView recyclerView, RelativeLayout rl_content){
        itemsSelect.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            //Log.d(TAG, "Hasil 2 => "+array.toString());
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

                    itemsSelect.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            itemsSelectAdapter.notifyDataSetChanged();
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rl_content.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBg));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addListItemsDashboard(String idItems, String itemsName, String itemsPrice, String itemsPriceSell, String itemsImage, String itemsDescription){
        hide();
        int checkExist = db.getCountListItemsTemp(itemsName, "1");
        if(checkExist == 0){
            db.addListItemsTemp(idItems, itemsName, itemsPrice, itemsPriceSell, itemsImage, itemsDescription, "1", "1");
            jsonListItems                               = db.getListItemsTemp("2");
            Log.d(TAG, "Json " + jsonListItems);
            setData(jsonListItems);
        }else{
            Toast.makeText(context, itemsName + " Sudah Ada", Toast.LENGTH_LONG).show();
            jsonListItems                               = db.getListItemsTemp("2");
            Log.d(TAG, "Json " + jsonListItems);
            setData(jsonListItems);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        db = new Database(getActivity());
        checkTransaction = db.getCountTransactionsDetail("1");
        Log.d(TAG, "resum " + checkTransaction);
        final MainActivity mainActivity = (MainActivity) getActivity();

        if(checkTransaction != 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DecimalFormat formatter     = new DecimalFormat("#,###,###");
                    totalQty                    = String.valueOf(db.getTotalQty("1"));
                    total                       = String.valueOf(db.getTotal("1"));

                    Log.d(TAG, "Total, " + total);
                    String price                = formatter.format(Integer.parseInt(total));

                    tv_qty_data.setText(totalQty + " Items");
                    tv_price_data.setText("Rp " + price);

                    rl_button.setVisibility(View.VISIBLE);
                    rl_cart.setVisibility(View.VISIBLE);
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getActivity(),
                                    R.anim.fade_in);
                    rl_button.startAnimation(animation1);
                    rl_cart.startAnimation(animation1);
                    et_search.setText("");
                    //mainActivity.onResume();
                }
            }, 200);
        }else{
            Log.d(TAG, "resum2 " + checkTransaction);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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
            }, 200);
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is = null;
        try {
            AssetManager manager = context.getAssets();
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /* Add */
    public void addItems(final String idItems, final String itemsName, final String itemsPrice, final String itemsImage, final String tipe){

        DecimalFormat formatter             = new DecimalFormat("#,###,###");
        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(getActivity());
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_add_items, null);
        ImageView iv_icon                   = dialogView.findViewById(R.id.iv_icon);
        ImageView iv_delete_shortcut        = dialogView.findViewById(R.id.iv_delete_shortcut);
        TextView tv_items                   = dialogView.findViewById(R.id.tv_items);
        TextView tv_price                   = dialogView.findViewById(R.id.tv_price);
        TextView tv_qty                     = dialogView.findViewById(R.id.tv_qty);
        final NumberPicker np_qty                 = dialogView.findViewById(R.id.np_qty);

        np_qty.setMinValue(1);
        np_qty.setMaxValue(100);

        String price            = formatter.format(Integer.parseInt(itemsPrice));


        tv_items.setTypeface(fontsBold);
        tv_price.setTypeface(fonts);
        tv_qty.setTypeface(fonts);

        Log.d(TAG, "ii " + tipe);
        tv_items.setText(itemsName);
        tv_price.setText("Rp " + price);

        Glide.with(this)
                .load(itemsImage)
                .into(iv_icon);

        if(!tipe.equals("1")){
            iv_delete_shortcut.setVisibility(View.GONE);
        }

        iv_delete_shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancelConfirm("Hapus " + itemsName + "\ndari shortcut ?", idItems);
                showAlertDialog();
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
        btn_cancel.setText("Batal");
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
        btn_add.setText("Tambah");
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                setLoadingDialog("Loading ...");
                showAlertDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideAlertDialog();
                        checkItems = db.getCountTransactionsDetailItems(idItems, "1");
                        Log.d(TAG, "Check " + checkItems);

                        if(checkItems == 0){
                            final int subtotal            = Integer.parseInt(itemsPrice) * np_qty.getValue();
                            db.addTransactionsDetail("0", idItems, itemsName, String.valueOf(np_qty.getValue()), itemsPrice, String.valueOf(subtotal), "1");
                            setSuccessDialog("Items berhasil ditambahkan");
                            showAlertDialog();
                        }else{
                            final int subtotal            = Integer.parseInt(itemsPrice) * np_qty.getValue();
                            HashMap<String, String> items   = db.getTransactionsItems("0", idItems, "1");
                            String qty                      = items.get("qty");
                            String subtotalo                 = items.get("subtotal");

                            int newQty                      = Integer.parseInt(qty) + np_qty.getValue();
                            int newSubtotal                 = Integer.parseInt(subtotalo) + subtotal;
                            db.updateTransactions("0", idItems, itemsName, String.valueOf(newQty), itemsPrice, String.valueOf(newSubtotal), "1");
                            setSuccessDialog("Items berhasil ditambahkan");
                            showAlertDialog();
                        }


                    }
                }, 500);


            }
        });

        alertDialog.show();
    }

    /* Add */
    public void addCustomItems(){

        DecimalFormat formatter             = new DecimalFormat("#,###,###");
        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(getActivity());
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_add_custom_items, null);
        ImageView iv_icon                   = dialogView.findViewById(R.id.iv_icon);
        final EditText et_items                   = dialogView.findViewById(R.id.et_items);
        final EditText et_price                   = dialogView.findViewById(R.id.et_price);
        TextView tv_qty                     = dialogView.findViewById(R.id.tv_qty);
        final NumberPicker np_qty                 = dialogView.findViewById(R.id.np_qty);

        np_qty.setMinValue(1);
        np_qty.setMaxValue(100);

        et_items.setTypeface(fonts);
        et_price.setTypeface(fonts);
        tv_qty.setTypeface(fonts);

        dialogBuilder.setView(dialogView);

        alertDialog                         = dialogBuilder.create();

        Window window                       = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center
        // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom
        Button btn_cancel                   = dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(fontsBold);
        btn_cancel.setText("Batal");
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
        btn_add.setText("Tambah");
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                setLoadingDialog("Loading ...");
                showAlertDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideAlertDialog();
                        final int subtotal            = Integer.parseInt(et_price.getText().toString()) * np_qty.getValue();
                        db.addTransactionsDetail("0", "", et_items.getText().toString(), String.valueOf(np_qty.getValue()), et_price.getText().toString(), String.valueOf(subtotal), "1");
                        setSuccessDialog("Items berhasil ditambahkan");
                        showAlertDialog();

                    }
                }, 500);


            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();
    }

    /* Add */
    public void addNewItems(){

        DecimalFormat formatter             = new DecimalFormat("#,###,###");
        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(getActivity());
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_add_items_dialog, null);
        final EditText et_search            = dialogView.findViewById(R.id.et_search);
        ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_view_container);
        RecyclerView recyclerView           = dialogView.findViewById(R.id.rv_items);
        RelativeLayout rl_content           = dialogView.findViewById(R.id.rl_content);

        RecyclerView.LayoutManager mLayoutManager   = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(3), true));
        itemsSelectAdapter                  = new ItemsSelectAdapter(context, itemsSelect, this);
        recyclerView.setAdapter(itemsSelectAdapter);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemsSelectAdapter.getFilter().filter(s.toString());
            }
        });

        jsonListItems                               = db.getListItems();
        setDataDialog(jsonListItems, shimmerFrameLayout, recyclerView, rl_content);

        et_search.setTypeface(fonts);

        dialogBuilder.setView(dialogView);

        alertDialog                         = dialogBuilder.create();

        Window window                       = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center

        Button btn_close                      = dialogView.findViewById(R.id.btn_close);
        btn_close.setTypeface(fontsBold);
        btn_close.setText("Tutup");
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                onResume();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

    }

    public void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText(message);
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDialog(String message){
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("SUCCESS")
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        onResume();

                    }
                });
        sweetAlertDialog.setCancelable(false);
    }

    private void setSuccessDeleteDialog(String message){
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_LARGE_TYPE)
                .setTitleText("SUCCESS")
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        hide();
                        jsonListItems                               = db.getListItemsTemp("2");
                        setData(jsonListItems);
                    }
                });
        sweetAlertDialog.setCancelable(false);
    }

    public void setCancelConfirm(String message, final String idItems){
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.CONFIRM_TYPE)
                .setTitleText("KONFIRMASI")
                .setContentText(message)
                .setCancelText("BATAL")
                .setConfirmText("HAPUS")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        hideAlertDialog();
                        setLoadingDialog("Loading data ...");
                        showAlertDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideAlertDialog();
                                db.deleteItemsTempById(idItems);
                                setSuccessDeleteDialog("Shortcut Berhasil dihapus");
                                showAlertDialog();
                            }
                        }, 1500);
                    }
                });
        sweetAlertDialog.setCancelable(false);
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

    public void hide(){
        alertDialog.dismiss();
        alertDialog.hide();
    }
}

