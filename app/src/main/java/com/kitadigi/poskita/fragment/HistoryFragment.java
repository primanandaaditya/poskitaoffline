package com.kitadigi.poskita.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.adapter.TransactionReportAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.TransactionDates;
import com.kitadigi.poskita.model.Transactions;
import com.kitadigi.poskita.model.TransactionsList;
import com.kitadigi.poskita.util.DividerItemDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();
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

    private TextView tv_header;
    private ImageView iv_filter;
    private RecyclerView recycler_view;

    /* Shimmer */
    private ShimmerFrameLayout mShimmerViewContainer;

    /* Init Swipe Refresh */
    private SwipeRefreshLayout swipeRefreshLayout;

    //database
    private Database db;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;

    private SweetAlertDialog sweetAlertDialog;
    private AlertDialog alertDialog;
    LayoutInflater inflater = null;
    boolean filterRiwayat = false;

    Calendar calendarfirst, calendarnext;
    SimpleDateFormat today;

    //variable
    String now, nextmonth, periodeAwal, periodeAkhir, jsonListItems, jsonFilter;

    TransactionReportAdapter transactionReportAdapter;
    ArrayList<TransactionDates> transactionDatesArrayList = new ArrayList<TransactionDates>();
    TransactionsList transactionsList = new TransactionsList();



    public HistoryFragment() {
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
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
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
        TimeZone tz                     = TimeZone.getTimeZone("GMT+0700");
        calendarfirst                   = Calendar.getInstance(tz);
        calendarnext                    = Calendar.getInstance(tz);
        calendarnext.add(Calendar.DATE, -30);

        today                           = new SimpleDateFormat("dd-MM-yyyy");
        now                             = today.format(calendarfirst.getTime());
        nextmonth                       = today.format(calendarnext.getTime());

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        //init view
        tv_header                       = view.findViewById(R.id.tv_header);
        recycler_view                   = view.findViewById(R.id.rv_items);
        iv_filter                       = view.findViewById(R.id.iv_filter);

        /* Shimmer */
        mShimmerViewContainer           = view.findViewById(R.id.shimmer_view_container);
        /* Swipe Refresh */
        swipeRefreshLayout              = view.findViewById(R.id.swipe_refresh_layout);

        tv_header.setTypeface(fonts);

        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(20);
        recycler_view.setDrawingCacheEnabled(true);
        recycler_view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(context, R.drawable.item_decorator)));
        transactionReportAdapter = new TransactionReportAdapter(context, transactionsList);
        recycler_view.setAdapter(transactionReportAdapter);

        jsonListItems                               = db.getAllTransactionsTipeByDate("penjualan");
        setData(jsonListItems);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                mShimmerViewContainer.setVisibility(View.VISIBLE);
//                mShimmerViewContainer.startShimmerAnimation();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        db.deleteItems();
//                        fetchMenu(context);
//                    }
//                }, 1500);
//            }
//        });
//
//        mShimmerViewContainer.setVisibility(View.VISIBLE);
//        mShimmerViewContainer.startShimmerAnimation();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                db.deleteItems();
//                fetchMenu(context);
//            }
//        }, 1500);

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRiwayat = true;
                filter(context, now, nextmonth);
            }
        });

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
//
//    private void fetchMenu (Context context){
//        JSONArray array = null;
//        String jsonfilename;
//        try {
//            jsonfilename = "json/listItems.json";
//            array = new JSONArray(loadJSONFromAsset(context, jsonfilename));
//            Log.d(TAG, "Hasil 1 => " + array.toString());
//            for (int i = 0; i < array.length(); i++) {
//                try {
//                    JSONObject obj = array.getJSONObject(i);
//                    db.addListItems(obj.getString("itemsName"), obj.getString("itemsPrice"), obj.getString("itemsImage"), obj.getString("itemsDescription"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            jsonListItems                               = db.getListItems();
//            setData(jsonListItems);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    public void fetchListItems(String json){
        transactionDatesArrayList.clear();;
        JSONArray array = null;
        JSONArray array2 = null;
        try {
            array = new JSONArray(json);
            Log.d(TAG, "Hasil array 1 => "+array.toString());
            for (int i = 0; i < array.length(); i++) {
                try {
                    TransactionDates transactionDates = new TransactionDates();
                    JSONObject obj = array.getJSONObject(i);
                    String date = obj.getString("date");
                    transactionDates.setDate(date);
                    //String jsonListItems = db.getListTransactionsDetail(obj.getString("no"));
                    String jsonListItems = db.getListTransactionsByDate("penjualan", obj.getString("date"));
                    array2 = new JSONArray(jsonListItems);
                    Log.d(TAG, "Hasil array 2 => "+array2.toString());
                    ArrayList<Transactions> details = new ArrayList<>();
                    for (int j=0;j<array2.length();j++){
                        Transactions transactionsDetail = new Transactions();
                        JSONObject eventObj = array2.getJSONObject(j);
                        transactionsDetail.setNo(eventObj.getString("no"));
                        transactionsDetail.setTipe(eventObj.getString("tipe"));
                        transactionsDetail.setTotal(eventObj.getString("total"));
                        details.add(transactionsDetail);
                    }
                    transactionDates.setTransactionsDetailArrayList(details);
                    transactionDatesArrayList.add(transactionDates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            transactionsList.setTransactionDatesArrayList(transactionDatesArrayList);
            mShimmerViewContainer.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Filter Riwayat Penjualan */
    public void filter(final Context context, final String nowday, final String nextmonthday){
        /* Custom fonts */
        fonts                               = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                         = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        AlertDialog.Builder dialogBuilder   = new AlertDialog.Builder(context);
        LayoutInflater inflater             = null;
        inflater                            = this.getLayoutInflater();
        View dialogView                     = inflater.inflate(R.layout.popup_filter_data_dialog, null);

        TimeZone tz                         = TimeZone.getTimeZone("GMT+0700");
        final Calendar tglPeriodeAwal       = Calendar.getInstance(tz);
        final Calendar tglPeriodeAkhir      = Calendar.getInstance(tz);
        tglPeriodeAwal.add(Calendar.DATE, -30);

        TextView tv_title                   = dialogView.findViewById(R.id.tv_title);
        TextView tv_periode                 = dialogView.findViewById(R.id.tv_periode);
        final EditText et_periode_awal      = dialogView.findViewById(R.id.et_periode_awal);
        final EditText et_periode_akhir     = dialogView.findViewById(R.id.et_periode_akhir);

        et_periode_awal.setText(nextmonthday);
        et_periode_akhir.setText(nowday);

        /* Date Picker Tanggal Awal */
        final DatePickerDialog.OnDateSetListener tglawal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tglPeriodeAwal.set(Calendar.YEAR, year);
                tglPeriodeAwal.set(Calendar.MONTH, month);
                tglPeriodeAwal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTglPeriodeAwal(et_periode_awal, tglPeriodeAwal);
            }
        };

        et_periode_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, tglawal, tglPeriodeAwal.get(Calendar.YEAR), tglPeriodeAwal.get(Calendar.MONTH),
                        tglPeriodeAwal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /* Date Picker Tanggal Akhir */
        final DatePickerDialog.OnDateSetListener tglakhir = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tglPeriodeAkhir.set(Calendar.YEAR, year);
                tglPeriodeAkhir.set(Calendar.MONTH, month);
                tglPeriodeAkhir.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTglPeriodeAkhir(et_periode_akhir, tglPeriodeAkhir);
            }
        };

        et_periode_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, tglakhir, tglPeriodeAkhir.get(Calendar.YEAR), tglPeriodeAkhir.get(Calendar.MONTH),
                        tglPeriodeAkhir.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        et_periode_awal.setTypeface(fonts);
        et_periode_akhir.setTypeface(fonts);
        tv_title.setTypeface(fonts);
        tv_periode.setTypeface(fonts);

        dialogBuilder.setView(dialogView);

        alertDialog                         = dialogBuilder.create();

        Window window                       = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center
        // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom
        Button btnCancel                            = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(fonts);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                filterRiwayat = false;

            }
        });

        // Send Button
        Button btnFilter                            = dialogView.findViewById(R.id.btnFilter);
        btnFilter.setText("Filter");
        btnFilter.setTypeface(fonts);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.dismiss();
                periodeAwal        = reverseDate(et_periode_awal.getText().toString());
                periodeAkhir       = reverseDate(et_periode_akhir.getText().toString());
                jsonFilter         = db.getAllTransactionsFilterByTipeDate("penjualan", periodeAwal, periodeAkhir);
                setData(jsonFilter);

                //Toast.makeText(context, "Tgl Awal " + reverseDate(et_periode_awal.getText().toString()) + ", Tgl Akhir " + reverseDate(et_periode_akhir.getText().toString()), Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }

    private void updateTglPeriodeAwal(final EditText etPeriodeAwal, Calendar tglPeriodeAwal) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etPeriodeAwal.setText(sdf.format(tglPeriodeAwal.getTime()));

    }

    private void updateTglPeriodeAkhir(final EditText etPeriodeAkhir, Calendar tglPeriodeAkhir) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etPeriodeAkhir.setText(sdf.format(tglPeriodeAkhir.getTime()));

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
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

    public String reverseDate(String date){
        String input    = "dd-MM-yyyy";
        String output   = "yyyy-MM-dd";

        SimpleDateFormat formatInput    = new SimpleDateFormat(input);
        SimpleDateFormat formatOutput   = new SimpleDateFormat(output);

        Date dates = null;
        String str = null;

        try {
            dates = formatInput.parse(date);
            str   = formatOutput.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}

