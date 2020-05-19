package com.kitadigi.poskita.sinkron.kategori;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SyncKategoriActivity extends AppCompatActivity {

    KategoriHelper kategoriHelper;
    Button btnList,btnToast;
    TextView tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_kategori);

        //siapkan json
        final JSONArray jsonArray = new JSONArray();


        //find iD
        tvHasil=(TextView)findViewById(R.id.tvHasil);
        btnList=(Button)findViewById(R.id.btnList);
        btnToast=(Button)findViewById(R.id.btnToast);

        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvHasil.setText(jsonArray.toString());
                Log.d("hasil",jsonArray.toString());
//                Toast.makeText(SyncKategoriActivity.this,jsonArray.toString(),Toast.LENGTH_LONG).show();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriHelper=new KategoriHelper(SyncKategoriActivity.this);

               JSONObject jsonObject;

                //get semua kategori
                List<Kategori> kategoris = kategoriHelper.semuaKategori();

                //looping, kumpulkan yg belum sync insert-nya
                for (Kategori kategori:kategoris){
                    if (kategori.getSync_insert().equals(Constants.STATUS_BELUM_SYNC)){
                        jsonObject=new JSONObject();
                        try {
                            jsonObject.put("id",kategori.getId().toString());
                            jsonObject.put("nama",kategori.getName_category().toString());
                            jsonObject.put("kode_id",kategori.getKode_id());
                            jsonObject.put("kode",kategori.getCode_category());
                            jsonObject.put("bussiness_id",1);
                            jsonObject.put("parent_id",0);
                            jsonObject.put("created_by",1);

                            //tambahkan di array
                            jsonArray.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{

                    }
                }
            }
        });


    }
}
