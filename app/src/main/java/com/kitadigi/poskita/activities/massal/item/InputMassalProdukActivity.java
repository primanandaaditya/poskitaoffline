package com.kitadigi.poskita.activities.massal.item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.IInputMassalKategori;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.fragment.item.Datum;

import java.util.List;

public class InputMassalProdukActivity extends BaseActivity implements IInputMassalKategori {

    //var untuk nampung list datum/kategori
    List<Datum> datumList;
    Datum datum;

    //init view
    ListView lv;
    TextView tv_nav_header;
    ImageView iv_back;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_massal_produk);

        findID();
    }

    @Override
    public void findID() {

    }

    @Override
    public void buatInput() {

    }

    @Override
    public void simpanSemua() {

    }
}
