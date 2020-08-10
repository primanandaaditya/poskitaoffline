package com.kitadigi.poskita;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.activities.coba.CobaActivity;
import com.kitadigi.poskita.activities.printer.PrintingActivity;
import com.kitadigi.poskita.adapter.ItemsAdapter;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.fragment.POSFragment;
import com.kitadigi.poskita.fragment.brand.BrandFragment;
import com.kitadigi.poskita.fragment.dashboard.DashboardFragment;
import com.kitadigi.poskita.fragment.inputmassal.InputMassalFragment;
import com.kitadigi.poskita.fragment.item.PrimaItemFragment;
import com.kitadigi.poskita.fragment.kategori.PrimaKategoriFragment;
import com.kitadigi.poskita.fragment.pembelian.PembelianFragment;
import com.kitadigi.poskita.fragment.pos.JualFragment;
import com.kitadigi.poskita.fragment.report.list.ReportFragment;
import com.kitadigi.poskita.fragment.sinkron.SinkronFragment;
import com.kitadigi.poskita.fragment.unit.UnitFragment;
import com.kitadigi.poskita.model.Items;
import com.kitadigi.poskita.util.AlarmReceiver;
import com.kitadigi.poskita.util.CustomTypefaceSpan;
import com.kitadigi.poskita.util.OnFragmentInteractionListener;
import com.kitadigi.poskita.util.SessionManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private static final String TAG         = MainActivity.class.getSimpleName();
    private Context context;


    private SessionManager sessionManager;


    //init ui
    private TextView tv_nav_header, tv_toolbar, tv_wib, tv_remark_balance, tv_balance;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private int count = 0;

    Calendar calendar;
    SimpleDateFormat today;
    TextClock tc;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    public static final int REQUEST_ITEMS_DATA  = 10;
    public static final int REQUEST_HOME        = 20;

    // tags used to attach the fragments
    private static final String TAG_POS         = "pos";
    private static final String TAG_PEMBELIAN   = "pembelian";
    private static final String TAG_ITEMS       = "items";
    private static final String TAG_REPORTS     = "reports";
    private static final String TAG_PROFILE     = "profile";
    private static final String TAG_WHOLESALE   = "wholesale";
    private static final String TAG_KATEGORI    = "kategori";
    private static final String TAG_BRAND       = "brand";
    private static final String TAG_UNIT        = "unit";
    private static final String TAG_PRINTER     = "printer";
    private static final String TAG_SINKRON     = "sinkron";
    private static final String TAG_INPUT_MASSAL = "inputmassal";
    private static final String TAG_DASHBOARD   = "dashboard";
    public static String CURRENT_TAG            = TAG_POS;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    //database
    private Database db;

    //fonts
    Typeface fonts, fontsItalic, fontsBold;

    //items
    private List<Items> items = new ArrayList<>();
    private ItemsAdapter itemsAdapter;

    //variable
    int check, checkBalance, checkTransaction;
    String jsonTransaction, now, saldo, nominal, totalQty;

    private SweetAlertDialog sweetAlertDialog;
    private AlertDialog alertDialog;
    LayoutInflater inflater = null;

    //untuk menampilkan full_name dan email
    //hasil dari login
    String nama,email;

//    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context                         = this;


        //init session
        sessionManager=new SessionManager(context);

        sessionManager.checkLogin();

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();
        nama = user.get(SessionManager.KEY_NAME);
        email = user.get(SessionManager.KEY_EMAIL);


        //init database
        db                              = new Database(context);

        toolbar                         = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ActionBar ab                    = getSupportActionBar();
        ab.setTitle("");

        mHandler                        = new Handler();

        drawer                          = findViewById(R.id.drawer_layout);
        navigationView                  = findViewById(R.id.nav_view);
        //fab                             = findViewById(R.id.fab);


        Menu m                          = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        TimeZone tz                     = TimeZone.getTimeZone("GMT+0700");
        calendar                        = Calendar.getInstance(tz);

        today                           = new SimpleDateFormat("dd MMMM yyyy");
        now                             = today.format(calendar.getTime());

        // Navigation view header
        navHeader                       = navigationView.getHeaderView(0);
        tv_nav_header                   = navHeader.findViewById(R.id.tv_nav_header);
        tc                              = navHeader.findViewById(R.id.tc_jam);
        tv_wib                          = navHeader.findViewById(R.id.tv_wib);
        tv_remark_balance               = navHeader.findViewById(R.id.tv_remark_balance);
        tv_balance                      = navHeader.findViewById(R.id.tv_balance);
        tv_toolbar                      = toolbar.findViewById(R.id.tv_toolbar);

        tv_toolbar.setTypeface(fontsBold);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        checkBalance = db.getCountBalance();
        if(check == 0){
            db.addBalance("25000000", "0", "0");
        }

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            loadHomeFragment();
        }



    }

    // show or hide the fab
//    private void toggleFab() {
//        if (navItemIndex == 0 || navItemIndex == 3)
//            fab.show();
//        else
//            fab.hide();
//    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name
        tv_nav_header.setTypeface(fonts);
        tc.setTypeface(fonts);
        tv_wib.setTypeface(fonts);
        tv_remark_balance.setTypeface(fonts);
        tv_balance.setTypeface(fontsItalic);
        tv_wib.setTypeface(fonts);

        tv_nav_header.setText(now + ",");
        tv_wib.setText("");

        HashMap<String, String> balance = db.getBalance();
        saldo = balance.get("balance");
        if(saldo != null){
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            nominal             = formatter.format(Integer.parseInt(saldo));
        }else{
            nominal             = "0.00";
        }


        //menampilkan fullname dan email dari login
        tv_remark_balance.setText(nama);
        tv_balance.setText(email);

//        tv_balance.setText(nominal);

//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(iv_nav_header);

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            //toggleFab();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        // this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            Log.d(TAG, "Result " + getSupportFragmentManager().getBackStackEntryCount());
//            drawer.closeDrawers();
//            //getSupportFragmentManager().popBackStack();
////            getSupportFragmentManager().popBackStack("wholesale", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            // this will clear the back stack and displays no animation on the screen
//            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            return;
//        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Current tag " + CURRENT_TAG);
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //toggleFab();
        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 1:
//                POSFragment homeFragment = new POSFragment();
//                return homeFragment;
                JualFragment jualFragment=new JualFragment();
                return jualFragment;
            case 2:
                PembelianFragment pembelianFragment =new PembelianFragment();
                return pembelianFragment;
            case 3:
                PrimaItemFragment primaItemFragment=new PrimaItemFragment();
                return primaItemFragment;
            case 4:
                PrimaKategoriFragment primaKategoriFragment=new PrimaKategoriFragment();
                return primaKategoriFragment;
            case 5:
                BrandFragment brandFragment=new BrandFragment();
                return brandFragment;
            case 6:
                UnitFragment unitFragment=new UnitFragment();
                return unitFragment;
            case 7:
//                HistoryFragment historyFragment = new HistoryFragment();
//                return historyFragment;
                ReportFragment reportFragment = new ReportFragment();
                return reportFragment;
            case 8:

                SinkronFragment sinkronFragmenta=new SinkronFragment();
                return sinkronFragmenta;
            case 9:
//                Intent syncKategori = new Intent(MainActivity.this, SyncKategoriActivity.class);
//                startActivity(syncKategori);
                SinkronFragment sinkronFragment=new SinkronFragment();
                return sinkronFragment;
            case 10:
//                AddressFragment addressFragment = new AddressFragment();
//                return addressFragment;

                InputMassalFragment inputMassalFragment = new InputMassalFragment();
                return inputMassalFragment;

            default:
                return new POSFragment();
        }
    }

    private void setToolbarTitle() {
        tv_toolbar.setText(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.nav_dashboard:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.nav_pos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_POS;
                        break;
                    case R.id.nav_pembelian:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PEMBELIAN;
                        break;
                    case R.id.nav_items:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ITEMS;
                        break;
                    case R.id.nav_kategori:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_KATEGORI;
                        break;
                    case R.id.nav_brand:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_BRAND;
                        break;
                    case R.id.nav_unit:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_UNIT;
                        break;
                    case R.id.nav_report:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_REPORTS;
                        break;
                    case R.id.nav_printer:
                        Intent intent = new Intent(MainActivity.this, PrintingActivity.class);
                        startActivity(intent);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_POS;
                        break;
                    case R.id.nav_sinkron:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_SINKRON;
                        break;
                    case R.id.nav_wholesale:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_INPUT_MASSAL;
                        break;
                    case R.id.nav_input_massal:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_INPUT_MASSAL;
                        break;
                    case R.id.nav_logout:

                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.mau_logout))
                                .setConfirmText(getResources().getString(R.string.ya))
                                .setCancelText(getResources().getString(R.string.tidak))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sessionManager.logoutUser();

                                        //saat logout, matikan juga alarm sinkronsisasi
                                        PackageManager pm  = getPackageManager();
                                        ComponentName componentName = new ComponentName(MainActivity.this, AlarmReceiver.class);
                                        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                                PackageManager.DONT_KILL_APP);
//                                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alarm_dimatikan), Toast.LENGTH_LONG).show();


                                        finish();

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


                        break;
                    //case R.id.nav_contact_us:
                    // launch new intent instead of loading fragment
                    //startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                    //drawer.closeDrawers();
                    //return true;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void applyFontToMenuItem(MenuItem mi){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
//        checkTransaction = db.getCountTransactionsDetail("1");
//
//        if(checkTransaction != 0){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    totalQty                    = String.valueOf(db.getTotalQty("1"));
//                    count = Integer.parseInt(totalQty);
//                    invalidateOptionsMenu();
//                    Log.d(TAG, "total QTY " + totalQty);
//                }
//            }, 200);
//        }else{
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    count = 0;
//                    invalidateOptionsMenu();
//                }
//            }, 200);
//        }
    }

//    boolean doubleBackToExitPressedOnce = false;
    boolean doubleBackToExitPressedOnce = true;
    @Override
    public void onBackPressed() {



        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            //getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_POS;
                loadHomeFragment();

                return;
            }
        }

        if (doubleBackToExitPressedOnce) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.anda_ingin_keluar_aplikasi))
                    .showContentText(false)
                    .setCancelText(getResources().getString(R.string.tidak))
                    .setConfirmText(getResources().getString(R.string.ya))
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            finish();
                        }
                    })
                    .show();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_ITEMS_DATA:
                Log.e(TAG, "Back Items");
                setTabItems(); // switch to tab2
                break;
            case REQUEST_HOME:
                Log.e(TAG, "Back Home");
                setTabHome(); // switch to tab2
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.menu_toolbar, menu);
//            MenuItem menuItem = menu.findItem(R.id.item_cart);
//            menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));
//
//        }else{
            getMenuInflater().inflate(R.menu.menu_toolbar_hide, menu);
        //}
        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_cart_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.rl_value);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setTypeface(fontsBold);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.item_cart) {
////            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
//            Intent transaction = new Intent(MainActivity.this, ItemsTransactionsActivity.class);
//            jsonTransaction = db.getListTransactionsDetail("0", "1");
//            transaction.putExtra("transaction", jsonTransaction);
//            startActivityForResult(transaction, REQUEST_HOME);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void setTabItems(){
        navItemIndex = 1;
        CURRENT_TAG = TAG_ITEMS;
        loadHomeFragment();
    }

    public void setTabHome(){
        navItemIndex = 0;
        CURRENT_TAG = TAG_POS;
        loadHomeFragment();
    }

    public void setLoadingDialog(String message){
        //Loading dialog
        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE).setTitleText(message);
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

    @Override
    public void replaceFragment(int id) {
        if(id == 2){
            PrimaKategoriFragment primaKategoriFragment=new PrimaKategoriFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, primaKategoriFragment, "kategori");
            fragmentTransaction.addToBackStack("kategori");
            fragmentTransaction.commit();

//            WholeSaleFragment wholeSaleFragment = new WholeSaleFragment();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fl_container, wholeSaleFragment, "wholesale");
//            fragmentTransaction.addToBackStack("wholesale");
//            fragmentTransaction.commit();
        }
    }
}
