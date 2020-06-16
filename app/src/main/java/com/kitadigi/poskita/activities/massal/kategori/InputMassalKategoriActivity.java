package com.kitadigi.poskita.activities.massal.kategori;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.kategori.IMKategoriAdapter;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class InputMassalKategoriActivity extends BaseActivity {

    //var untuk nampung list datum/kategori
    List<Datum> datumList;
    Datum datum;

    //init view
    ListView lv;
    TextView tv_nav_header;
    ImageView iv_back;
    Button btnSimpan;

    IMKategoriAdapter imKategoriAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_massal_kategori);

        findID();
    }

    void findID(){

        lv=(ListView)findViewById(R.id.lv);
        lv.setItemsCanFocus(true);

        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);

        this.applyFontBoldToTextView(tv_nav_header);

        //untuk nutup activity
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //buat inputan sebanyak 100
        buatInput();

        //untuk simpan ke sqlite
        btnSimpan=(Button)findViewById(R.id.btnSimpan);
        this.applyFontBoldToButton(btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = imKategoriAdapter.createJSONArray();
                Log.d("json", jsonArray.toString());
            }
        });


    }

    void buatInput(){
        datumList = new ArrayList<>();
        Integer counter = 0;

        //looping buat 100 inputan
        while (counter < Constants.maxInput){

            datum = new Datum();
            datumList.add(datum);
            counter = counter + 1;
        }


        //pasangkan di listview
        imKategoriAdapter = new IMKategoriAdapter(InputMassalKategoriActivity.this, datumList);

        lv.setAdapter(imKategoriAdapter);
    }
}
