package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    ListView lv;
    SearchView sv;



    //init controller untuk nampilin data barang
    BarangController barangController;
    KartuStokAdapter kartuStokAdapter;

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
        lv=(ListView) findViewById(R.id.lv);



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

        //kalau sukses, tampilkan list
        Log.d("jml", String.valueOf(items.size()));
        kartuStokAdapter = new KartuStokAdapter(PilihBarangActivity.this, items);
        lv.setAdapter(kartuStokAdapter);

        //klik listview
        klikListView();

        //biar bisa filter listview
        filterBarang();
    }

    @Override
    public void onError(String error, List<Item> items) {
        Log.d("jml", String.valueOf(items.size()));

        //tampilkan di listview
        kartuStokAdapter = new KartuStokAdapter(PilihBarangActivity.this, items);
        lv.setAdapter(kartuStokAdapter);

        //klik listview
        klikListView();

        //biar bisa filter listview
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
                kartuStokAdapter.filter(s);
                return false;
            }

        });

    }


    //bila listview diklik, maka akan pindah ke ROKartuStokActivity.java
    //untuk ditampilkan kartu stok
    //dalam intent diselipkan 'mobile_id'
    void klikListView(){

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = (Item)parent.getAdapter().getItem(position);
                Log.d("kodeid",item.getKode_id().toString());
                Intent intent = new Intent(PilihBarangActivity.this, ROKartuStokActivity.class);

                //lemparkan variabel dalam intent
                intent.putExtra("kode_id", item.getKode_id());
                intent.putExtra("nama_barang", item.getName_product());
                intent.putExtra("image", item.getImage());
                intent.putExtra("harga_beli", item.getPurchase_price().toString());
                intent.putExtra("harga_jual", item.getSell_price().toString());

                //mulai intent
                startActivity(intent);

            }
        });


    }
}
