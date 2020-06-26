package com.kitadigi.poskita.activities.printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PrintingActivity extends BaseActivity {


    //init toolbar dan tab
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //widget
    TextView tv_nav_header;
    ImageView iv_back;

    private BluetoothAdapter mBluetoothAdapter = null;

    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);

        //findviewbyid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        iv_back=(ImageView)findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //setting font
        this.applyFontBoldToTextView(tv_nav_header);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        //setting font pada Tab
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
            this.applyFontRegularToTextView(tv);
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //minta user untuk nyalakan BT
        try {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the session
            }

        }catch (Exception e){
            Toast.makeText(PrintingActivity.this, getResources().getString(R.string.perangkat_tidak_mendukung_bluetooth), Toast.LENGTH_SHORT).show();
        }


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PilihPrinterFragment(), getResources().getString(R.string.pilih_printer));
        adapter.addFragment(new CetakStrukFragment(), getResources().getString(R.string.cetak_struk_lama));
        viewPager.setAdapter(adapter);


    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_ENABLE_BT){
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a session

            } else {
                // User did not enable Bluetooth or an error occured
                Log.d("BT", "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving,
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
}
