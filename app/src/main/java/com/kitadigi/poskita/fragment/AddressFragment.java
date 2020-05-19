package com.kitadigi.poskita.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsAddressActivity;
import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.AddressListAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.Address;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {
    private static final String TAG = AddressFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private OnFragmentInteractionListener mListener;

    /* Init Frame Layout */
    private FrameLayout fragmentContainer;

    private TextView tv_header, tv_header_logo, tv_remark_balance, tv_balance;
    private ImageView iv_add;
    private RecyclerView recycler_view;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    /* Init Swipe Refresh */
    private SwipeRefreshLayout swipeRefreshLayout;

    //database
    private Database db;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;

    //items
    private List<Address> addresses = new ArrayList<>();
    private AddressListAdapter addressListAdapter;

    //variable
    String jsonListItems, saldo, nominal;

    private SweetAlertDialog sweetAlertDialog;


    public AddressFragment() {
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
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
        View view = inflater.inflate(R.layout.fragment_address, container, false);
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

        //init database
        db                              = new Database(context);

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        //init view
        tv_header                       = view.findViewById(R.id.tv_header);
        tv_header_logo                  = view.findViewById(R.id.tv_header_logo);
        tv_remark_balance               = view.findViewById(R.id.tv_remark_balance);
        tv_balance                      = view.findViewById(R.id.tv_balance);

        recycler_view                   = view.findViewById(R.id.rv_items);
        iv_add                          = view.findViewById(R.id.iv_add);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        tv_header.setTypeface(fonts);
        tv_header_logo.setTypeface(fontsBold);
        tv_remark_balance.setTypeface(fonts);
        tv_balance.setTypeface(fontsItalic);

        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(20);
        recycler_view.setDrawingCacheEnabled(true);
        recycler_view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(context, R.drawable.item_decorator)));
        addressListAdapter = new AddressListAdapter(context, addresses, this);
        recycler_view.setAdapter(addressListAdapter);

        jsonListItems                               = db.getListAddress();
        setData(jsonListItems);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                jsonListItems                               = db.getListAddress();
                setData(jsonListItems);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent address = new Intent(mainActivity, ItemsAddressActivity.class);
                startActivityForResult(address, MainActivity.REQUEST_ITEMS_DATA);
            }
        });

        HashMap<String, String> balance = db.getBalance();
        saldo = balance.get("balance");
        if(saldo != null){
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            nominal             = formatter.format(Integer.parseInt(saldo));
        }else{
            nominal             = "0.00";
        }

        tv_balance.setText(nominal);

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

    public void fetchListItems(String json){
        addresses.clear();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Hasil 2 => "+array.toString());
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    Address item = new Address();
                    item.setIdAddress(obj.getString("idAddress"));
                    item.setNameAddress(obj.getString("nameAddress"));
                    item.setUserAddress(obj.getString("userAddress"));
                    item.setUsersPhone(obj.getString("usersPhone"));
                    addresses.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            addressListAdapter.notifyDataSetChanged();
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        jsonListItems                               = db.getListAddress();
        setData(jsonListItems);
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

    public void editData(String idAddress, String recipien, String address, String phone){
        Intent addresses = new Intent(getActivity(), ItemsAddressActivity.class);
        addresses.putExtra("idAddress", idAddress);
        addresses.putExtra("recipien", recipien);
        addresses.putExtra("address", address);
        addresses.putExtra("phone", phone);
        startActivity(addresses);
    }

    public void setCancelConfirm(String message, final String idAddress){
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
                                db.deleteAddressById(idAddress);
                                setSuccessDeleteDialog("Alamat Berhasil dihapus");
                                showAlertDialog();
                            }
                        }, 1500);
                    }
                });
        sweetAlertDialog.setCancelable(false);
    }

    public void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText(message);
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
                        jsonListItems                               = db.getListAddress();
                        setData(jsonListItems);
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
}

