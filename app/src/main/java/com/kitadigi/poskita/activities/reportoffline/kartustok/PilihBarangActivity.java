package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.fragment.item.BarangAdapter;
import com.kitadigi.poskita.fragment.item.BarangController;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.IBarangResult;
import com.kitadigi.poskita.util.DividerItemDecoration;

import java.util.List;

public class PilihBarangActivity extends BaseActivity implements IBarangResult {

    ImageView iv_back;
    TextView tv_nav_header;
    RecyclerView lv;
    SearchView sv;



    //init controller untuk nampilin data barang
    BarangController barangController;

    BarangAdapter barangAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_barang);

        findID();
    }

    void findID(){



        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        iv_back=(ImageView)findViewById(R.id.iv_back);

        //setting recyclerview
        lv=(RecyclerView)findViewById(R.id.lv);
        lv.setHasFixedSize(true);
        lv.setItemViewCacheSize(20);
        lv.setDrawingCacheEnabled(true);
        lv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PilihBarangActivity.this);
        lv.setLayoutManager(mLayoutManager);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(PilihBarangActivity.this, R.drawable.item_decorator)));



        this.applyFontBoldToTextView(tv_nav_header);

        //untuk tutup activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mulai nampilin data barang
        barangController = new BarangController(this, PilihBarangActivity.this, true);
        barangController.getBarang();

    }

    @Override
    public void onSuccess(BarangResult barangResult, List<Item> items) {
        Log.d("jml", String.valueOf(items.size()));
        barangAdapter = new BarangAdapter(PilihBarangActivity.this, items, true);
        lv.setAdapter(barangAdapter);

        filterBarang();
    }

    @Override
    public void onError(String error, List<Item> items) {
        Log.d("jml", String.valueOf(items.size()));
        barangAdapter = new BarangAdapter(PilihBarangActivity.this, items, true);
        lv.setAdapter(barangAdapter);

        filterBarang();
    }


    void filterBarang(){

        sv=(SearchView)findViewById(R.id.sv);
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
