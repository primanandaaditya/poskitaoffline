package com.kitadigi.poskita.fragment.item;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsDataActivity;
import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.ItemsListAdapter;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.ItemsFragment;
import com.kitadigi.poskita.model.Items;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;




public class PrimaItemFragment extends BaseFragment implements IBarangResult {


    BarangController barangController;

    //init sqlite
    ItemHelper itemHelper;

    private static final String TAG = ItemsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private ItemsFragment.OnFragmentInteractionListener mListener;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_header;
    private ImageView iv_add;
    private RecyclerView recycler_view;
    private SearchView sv;

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
    private ItemsListAdapter itemsListAdapter;

    //variable
    String jsonListItems;

    private SweetAlertDialog sweetAlertDialog;

    public PrimaItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_prima_item, container, false);
        initMain(view);
        return view;

    }

    @Override
    public void onError(String error, List<Item> items) {
//        this.showToast(error);
        final BarangAdapter barangAdapter = new BarangAdapter(getActivity(),items,this);
        recycler_view.setAdapter(barangAdapter);

        sv=(SearchView)getActivity().findViewById(R.id.sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                barangAdapter.filter(s);
                return false;
            }

        });

    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {
        if (barangResult.getData()==null){

        }else{

            if (this.sessionExpired(barangResult.getMessage())==0){
//                PrimaItemListAdapter primaItemListAdapter=new PrimaItemListAdapter(getActivity(), barangResult, this);
//                recycler_view.setAdapter(primaItemListAdapter);
                final BarangAdapter barangAdapter = new BarangAdapter(getActivity(),items,this);
                recycler_view.setAdapter(barangAdapter);

                sv=(SearchView)getActivity().findViewById(R.id.sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        barangAdapter.filter(s);
                        return false;
                    }

                });

            }

        }


    }

    private void initMain(View view) {

        //init sqlite
        itemHelper=new ItemHelper(getActivity());

        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;

        barangController=new BarangController(context,this,true);


        //init database
        db                              = new Database(context);

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        //init view
        tv_header                       = view.findViewById(R.id.tv_header);
        recycler_view                   = view.findViewById(R.id.rv_items);
        iv_add                          = view.findViewById(R.id.iv_add);
        sv                              = view.findViewById(R.id.sv);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        //applyfont
        this.applyFontBoldToTextView(tv_header);

        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(20);
        recycler_view.setDrawingCacheEnabled(true);
        recycler_view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(context, R.drawable.item_decorator)));


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent additems = new Intent(getActivity(), ItemsDataActivity.class);
//                startActivityForResult(additems, MainActivity.REQUEST_ITEMS_DATA);
                startActivity(additems);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                barangController.getBarang();
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();

        barangController.getBarang();
    }

    public void editBarang(String id,
                           String category_id,
                           String units_id,
                           String brands_id,
                           String types,
                           String code_product,
                           String name_product,
                           String brands_name,
                           String name_category,
                           String units_name,
                           String image,
                           String purchase_price,
                           String sell_price,
                           String qty_stock,
                           String qty_minimum,
                           String additional,
                           String category_mobile_id,
                           String brand_mobile_id,
                           String unit_mobile_id
                           )

    {
        Intent additems = new Intent(getActivity(), ItemsDataActivity.class);


        additems.putExtra("id", id);
        additems.putExtra("category_id", category_id);
        additems.putExtra("units_id", units_id);
        additems.putExtra("brands_id",brands_id);
        additems.putExtra("types",types);
        additems.putExtra("code_product",code_product);
        additems.putExtra("name_product",name_product);
        additems.putExtra("brands_name",brands_name);
        additems.putExtra("name_category",name_category);
        additems.putExtra("units_name",units_name);
//        additems.putExtra("image", Url.DIKI_IMAGE_URL + image);
        additems.putExtra("image",image);
        additems.putExtra("purchase_price",purchase_price);
        additems.putExtra("sell_price",sell_price);
        additems.putExtra("qty_stock",qty_stock);
        additems.putExtra("qty_minimum",qty_minimum);
        additems.putExtra("additional",additional);
        additems.putExtra("category_mobile_id", category_mobile_id);
        additems.putExtra("brand_mobile_id", brand_mobile_id);
        additems.putExtra("unit_mobile_id",unit_mobile_id);

        startActivity(additems);
    }
}
