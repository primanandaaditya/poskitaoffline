package com.kitadigi.poskita.activities.massal.unit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.IInputMassalKategori;
import com.kitadigi.poskita.activities.massal.kategori.IMKategoriAdapter;
import com.kitadigi.poskita.activities.massal.kategori.InputMassalKategoriActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.unit.UnitHelper;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.util.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InputMassalUnitActivity extends BaseActivity implements IInputMassalKategori {

    //var untuk nampung list datum/kategori
    List<UnitData> unitDataList;
    UnitData unitData;

    //init view
    ListView lv;
    TextView tv_nav_header;
    ImageView iv_back;
    Button btnSimpan;

    IMUnitAdapter imUnitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_massal_unit);

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


        unitDataList = new ArrayList<>();
        Integer counter = 0;

        //looping buat 100 inputan
        while (counter < Constants.maxInput){

            unitData = new UnitData();
            unitDataList.add(unitData);
            counter = counter + 1;
        }

        //pasangkan di listview
        imUnitAdapter = new IMUnitAdapter(InputMassalUnitActivity.this, unitDataList);

        //tampilkan di listview
        lv.setAdapter(imUnitAdapter);
    }

    @Override
    public void simpanSemua() {

        //String untuk pemberitahuan hasil input
        String hasil;

        //dialog untuk nyimpen
        SweetAlertDialog sweetAlertDialog;

        sweetAlertDialog = new SweetAlertDialog(InputMassalUnitActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        //tampung array datum dalam var
        List<UnitData> list = imUnitAdapter.getList();
        Log.d("jumlah", String.valueOf(list.size()));

        //init sqlite
        UnitHelper unitHelper= new UnitHelper(InputMassalUnitActivity.this);
        hasil = unitHelper.inputMassal(list);

        sweetAlertDialog.dismissWithAnimation();

        //tampilkan pesan
        Toast.makeText(InputMassalUnitActivity.this, hasil ,Toast.LENGTH_SHORT).show();

        //tutup
        finish();

    }
}
