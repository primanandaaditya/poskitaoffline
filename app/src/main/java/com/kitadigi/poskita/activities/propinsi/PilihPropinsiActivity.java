package com.kitadigi.poskita.activities.propinsi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PilihPropinsiActivity extends BaseActivity implements IPropinsiResult {

    ListView listView;
    ImageView iv_back;
    TextView tv_nav_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_propinsi);


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

        PropinsiController propinsiController=new PropinsiController(PilihPropinsiActivity.this, this);
        propinsiController.getPropinsi();

    }



    @Override
    public void onGetPropinsiSuccess(PropinsiModel propinsiModel) {

        PropinsiAdapter propinsiAdapter = new PropinsiAdapter(PilihPropinsiActivity.this, propinsiModel);
        listView.setAdapter(propinsiAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //jika diklik, maka akan mengirimkan nama propinsi dan idpropinsi
                Datum datum = (Datum)parent.getAdapter().getItem(position);

                Intent intent = new Intent();

                intent.putExtra("nama_propinsi", datum.getProvince());
                intent.putExtra("id_propinsi", datum.getProvince_id());
                Log.d("nama_propinsi", datum.getProvince());
                Log.d("id_propinsi", datum.getProvince_id());

                ((Activity)view.getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) view.getContext()).finish();

            }
        });
    }

    @Override
    public void onGetPropinsiError(String error) {
        Toast.makeText(PilihPropinsiActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
