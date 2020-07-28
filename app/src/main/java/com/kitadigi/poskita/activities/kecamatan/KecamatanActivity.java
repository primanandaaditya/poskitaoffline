package com.kitadigi.poskita.activities.kecamatan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;

public class KecamatanActivity extends BaseActivity implements IKecamatanResult {

    ListView listView;
    ImageView iv_back;
    TextView tv_nav_header;
    Intent intent;
    String idKota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecamatan);


        //dapatkan idpropinsi dari intent
        intent = getIntent();

        idKota = intent.getStringExtra("idKota");


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

        KecamatanController kecamatanController = new KecamatanController(KecamatanActivity.this, this);
        Log.d("idKota",idKota);

        kecamatanController.getKecamatan(intent.getStringExtra("idKota"));

    }

    @Override
    public void onGetKecamatanSuccess(KecamatanModel kecamatanModel) {

        KecamatanAdapter kecamatanAdapter= new KecamatanAdapter(KecamatanActivity.this, kecamatanModel);
        listView.setAdapter(kecamatanAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //jika diklik, maka akan mengirimkan nama propinsi dan idpropinsi
                Datum datum = (Datum)parent.getAdapter().getItem(position);

                Intent intent = new Intent();

                intent.putExtra("nama_kecamatan", datum.getSubdistrict_name());
                intent.putExtra("id_kecamatan", datum.getSubdistrict_id());

                ((Activity)view.getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) view.getContext()).finish();

            }
        });

    }

    @Override
    public void onGetKecamatanError(String error) {
        this.showToast(error);
    }
}
