package com.kitadigi.poskita.activities.massal.item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.IInputMassalKategori;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.fragment.item.Datum;
import com.kitadigi.poskita.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class InputMassalProdukActivity extends BaseActivity implements IInputMassalKategori {

    //var untuk nampung list datum/item
    List<Datum> datumList;
    Datum datum;

    //init view
    ListView lv;
    TextView tv_nav_header;
    ImageView iv_back;
    Button btnSimpan;

    IMProdukAdapter imProdukAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_massal_produk);

        findID();
    }

    @Override
    public void findID() {

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

                //simpan semua inputan
                simpanSemua();
            }
        });

    }

    @Override
    public void buatInput() {


        datumList = new ArrayList<>();
        Integer counter = 0;

        //looping buat 100 inputan
        while (counter < Constants.maxInput50){

            datum = new Datum();
            datumList.add(datum);
            counter = counter + 1;
        }

//        pasangkan di listview
        imProdukAdapter = new IMProdukAdapter(InputMassalProdukActivity.this, datumList);

//        tampilkan di listview
        lv.setAdapter(imProdukAdapter);

    }

    @Override
    public void simpanSemua() {

    }
}
