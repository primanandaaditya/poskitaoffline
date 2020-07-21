package com.kitadigi.poskita.fragment.kategori;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriListDAO;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.ItemsFragment;
import com.kitadigi.poskita.fragment.addkategori.AddKategoriActivity;
import com.kitadigi.poskita.model.Items;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrimaKategoriFragment extends BaseFragment implements IKategoriResult {

    //controller offline
    KategoriListDAO kategoriListDAO;

    KategoriController kategoriController;
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

    public PrimaKategoriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prima_kategori, container, false);
        initMain(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //batalkan panggillan HTTP
        kategoriController.cancelCall();
    }

    @Override
    public void onResume() {
        super.onResume();

        kategoriController.getKategoriList();

    }

    private void initMain(View view){

        final MainActivity mainActivity = (MainActivity) getActivity();
        context                         = mainActivity;

        kategoriController=new KategoriController(context,this, true);
        kategoriListDAO=new KategoriListDAO(context,this);



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
        sv                              = view.findViewById(R.id.sv);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        tv_header.setTypeface(fonts);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent additems = new Intent(getActivity(), AddKategoriActivity.class);
                startActivity(additems);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                onResume();
            }
        });


    }

    @Override
    public void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline) {
             if (this.sessionExpired(kategoriModel.getMessage())==0){
                 final KategoriSyncAdapter kategoriSyncAdapter=new KategoriSyncAdapter(this,kategoriOffline);
                 listView.setAdapter(kategoriSyncAdapter);

                 //perform filter pada listview
                 filterListView(kategoriSyncAdapter);
             }
//        KategoriSyncAdapter kategoriSyncAdapter = new KategoriSyncAdapter(this,kategoriOffline);
//        listView.setAdapter(kategoriSyncAdapter);
    }

    @Override
    public void onKategoriError(String error,List<Kategori> kategoriOffline) {
//        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
        KategoriSyncAdapter kategoriSyncAdapter=new KategoriSyncAdapter(this,kategoriOffline);
        listView.setAdapter(kategoriSyncAdapter);

        //perform filter listview
        filterListView(kategoriSyncAdapter);

//        listView.setAdapter(null);
    }


    public void tampil(){
        kategoriController.getKategoriList();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

//            kategoriController.kategoriInsertBelumSync();
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
//           kategoriController.getKategoriList();
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


    void filterListView(final KategoriSyncAdapter kategoriSyncAdapter){
        //perform filter pada listview
        sv=(SearchView)getActivity().findViewById(R.id.sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                kategoriSyncAdapter.filter(s);
                return false;
            }
        });
    }
}
