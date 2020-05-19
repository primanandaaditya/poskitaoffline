package com.kitadigi.poskita.fragment.unit;


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
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.ItemsFragment;
import com.kitadigi.poskita.fragment.addunit.AddUnitActivity;
import com.kitadigi.poskita.model.Items;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnitFragment extends BaseFragment implements IUnitResult {



    UnitController unitController;
    Context context;

    private ItemsFragment.OnFragmentInteractionListener mListener;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_header;
    private ImageView iv_add;
    private ListView listView;
    private SearchView searchView;

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

    public UnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit, container, false);
        initMain(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        unitController.getUnitList();
    }

    private void initMain(View view){

        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;

        unitController=new UnitController(getActivity(), this, true);


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
        searchView                      = view.findViewById(R.id.sv);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        tv_header.setTypeface(fonts);


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent additems = new Intent(getActivity(), AddUnitActivity.class);
                startActivity(additems);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                unitController.getUnitList();
            }
        });

    }

    @Override
    public void onUnitSuccess(UnitModel unitModel,List<Unit> units) {
        if (this.sessionExpired(unitModel.getMessage())==0){
//            UnitAdapter unitAdapter=new UnitAdapter(this,unitModel);
//            listView.setAdapter(unitAdapter);
            final UnitSyncAdapter unitSyncAdapter = new UnitSyncAdapter(this,units);
            listView.setAdapter(unitSyncAdapter);

            //untuk filter
            searchView=(SearchView)getActivity().findViewById(R.id.sv);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    unitSyncAdapter.filter(s);
                    return false;
                }

            });

        }
    }

    @Override
    public void onUnitError(String error,List<Unit> units) {
//        this.showToast(error);
        final UnitSyncAdapter unitSyncAdapter = new UnitSyncAdapter(this,units);
        listView.setAdapter(unitSyncAdapter);

        //untuk filter
        searchView=(SearchView)getActivity().findViewById(R.id.sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                unitSyncAdapter.filter(s);
                return false;
            }

        });
    }
}
