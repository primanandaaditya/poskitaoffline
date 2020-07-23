package com.kitadigi.poskita.activities.coba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.brand.BrandData;
import com.kitadigi.poskita.fragment.brand.dengan_header.BrandController;
import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.brand.IBrandResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.Datum;
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriController;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.fragment.unit.dengan_header.IUnitResult;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitController;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitModel;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class CobaActivity extends AppCompatActivity implements IUnitResult {

    UnitController unitController;
    Button button;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);


        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              coba();
            }
        });
    }

    void coba(){

       unitController = new UnitController(CobaActivity.this, this);
       unitController.getUnitList();
    }



    @Override
    public void onUnitSuccess(UnitModel unitModel, List<Unit> units) {
        for (UnitData unitData: unitModel.getData()){
            Log.d("unit", unitData.getName());
        }
    }

    @Override
    public void onUnitError(String error, List<Unit> units) {

    }
}
