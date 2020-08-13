package com.kitadigi.poskita.activities.kota;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class KotaActivity extends BaseActivity implements IKotaResult {

    ListView listView;
    ImageView iv_back;
    TextView tv_nav_header;
    Intent intent;
    String idPropinsi;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kota);


        //dapatkan idpropinsi dari intent
        intent = getIntent();
        idPropinsi = intent.getStringExtra("idPropinsi");


        //findid
        listView=(ListView)findViewById(R.id.lv);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        this.applyFontBoldToTextView(tv_nav_header);

        //close
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        KotaController kotaController = new KotaController(KotaActivity.this, this);
        kotaController.getKota(idPropinsi);
    }

    @Override
    public void onGetKotaSuccess(final KotaModel kotaModel) {

        //tampilkan list kota
        final KotaAdapter kotaAdapter = new KotaAdapter(KotaActivity.this, kotaModel);
        listView.setAdapter(kotaAdapter);


        //jika listview diklik maka akan muncul
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //jika diklik, maka akan mengirimkan nama propinsi dan idpropinsi
                Datum datum = (Datum)parent.getAdapter().getItem(position);

                Intent intent = new Intent();

                intent.putExtra("nama_kota", datum.getCity_name());
                intent.putExtra("id_kota", datum.getCity_id());

                ((Activity)view.getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) view.getContext()).finish();

            }
        });

        //filter kota
        //search untuk filter kota
        sv=(SearchView)findViewById(R.id.sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                kotaAdapter.filter(s);
                return false;

            }

        });
    }

    @Override
    public void onGetKotaError(String error) {
        this.showToast(error);
    }
}
