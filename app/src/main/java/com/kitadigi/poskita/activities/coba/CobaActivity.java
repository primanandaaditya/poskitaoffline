package com.kitadigi.poskita.activities.coba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.fragment.kategori.dengan_header.Datum;
import com.kitadigi.poskita.fragment.kategori.dengan_header.IKategoriResult;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriController;

import java.util.List;

public class CobaActivity extends AppCompatActivity implements IKategoriResult {

    KategoriController kategoriHeaderController;
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

        kategoriHeaderController = new KategoriController(CobaActivity.this, this);
        kategoriHeaderController.getKategoriList();

    }



    @Override
    public void onKategoriSuccess(com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriModel kategoriModel, List<Kategori> kategoriOffline) {

        for (Datum datum: kategoriModel.getData()){
            Log.d("datum" , datum.getName());
        }
    }

    @Override
    public void onKategoriError(String error, List<Kategori> kategoriOffline) {
        Toast.makeText(CobaActivity.this, error, Toast.LENGTH_SHORT).show();

    }
}
