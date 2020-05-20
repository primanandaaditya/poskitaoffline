package com.kitadigi.poskita.fragment.brand;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.ItemsListAdapter;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.ItemsFragment;
import com.kitadigi.poskita.fragment.addbrand.AddBrandActivity;
import com.kitadigi.poskita.model.Items;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandFragment extends BaseFragment implements IBrandResult {


    BrandController brandController;
    Context context;

    private ItemsFragment.OnFragmentInteractionListener mListener;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_header;
    private ImageView iv_add;
    private ListView listView;
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

    public BrandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brand, container, false);
        initMain(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        brandController.getBrandList();
    }

    private void initMain(View view){

        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;

        brandController=new BrandController(getActivity(),this, true);

        //init database
        db                              = new Database(context);

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        //init view
        tv_header                       = view.findViewById(R.id.tv_header);
        listView                        = view.findViewById(R.id.lv_kategori);
        iv_add                          = view.findViewById(R.id.iv_add);
        sv                              = (SearchView)getActivity().findViewById(R.id.sv);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        tv_header.setTypeface(fonts);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent additems = new Intent(getActivity(), AddBrandActivity.class);
                startActivity(additems);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                brandController.getBrandList();
            }
        });

    }

    @Override
    public void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline) {
        if (this.sessionExpired(brandModel.getMessage())==0){
            final BrandAdapter brandAdapter=new BrandAdapter(BrandFragment.this, brandOffline);
            listView.setAdapter(brandAdapter);

            //filter listview
            sv=(SearchView)getActivity().findViewById(R.id.sv);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    brandAdapter.filter(s);
                    return false;
                }

            });
        }
    }

    @Override
    public void onBrandError(String error, List<Brand> brandOffline) {
        final BrandAdapter brandAdapter=new BrandAdapter(BrandFragment.this, brandOffline);
        listView.setAdapter(brandAdapter);

        //filter listview
        sv=(SearchView)getActivity().findViewById(R.id.sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                brandAdapter.filter(s);
                return false;
            }

        });
    }
}
