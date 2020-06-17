package com.kitadigi.poskita.activities.massal.brand;

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
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.fragment.brand.BrandData;
import com.kitadigi.poskita.util.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InputMassalBrandActivity extends BaseActivity implements IInputMassalKategori {

    //var untuk nampung list datum/kategori
    List<BrandData> brandDataList;
    BrandData brandData;

    //init view
    ListView lv;
    TextView tv_nav_header;
    ImageView iv_back;
    Button btnSimpan;

    IMBrandAdapter imBrandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_massal_brand);

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

        brandDataList = new ArrayList<>();
        Integer counter = 0;

        //looping buat 100 inputan
        while (counter < Constants.maxInput){

            brandData = new BrandData();
            brandDataList.add(brandData);
            counter = counter + 1;
        }

        //pasangkan di listview
        imBrandAdapter = new IMBrandAdapter(InputMassalBrandActivity.this, brandDataList);

        //tampilkan di listview
        lv.setAdapter(imBrandAdapter);
    }

    @Override
    public void simpanSemua() {

        //String untuk pemberitahuan hasil input
        String hasil;

        //dialog untuk nyimpen
        SweetAlertDialog sweetAlertDialog;

        sweetAlertDialog = new SweetAlertDialog(InputMassalBrandActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.now_loading));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        //tampung array datum dalam var
        List<BrandData> brandDataList = imBrandAdapter.getList();
        Log.d("jumlah", String.valueOf(brandDataList.size()));

        //init sqlite
        BrandHelper brandHelper = new BrandHelper(InputMassalBrandActivity.this);
        hasil = brandHelper.inputMassal(brandDataList);

        sweetAlertDialog.dismissWithAnimation();

        //tampilkan pesan
        Toast.makeText(InputMassalBrandActivity.this, hasil ,Toast.LENGTH_SHORT).show();

        //tutup
        finish();

    }
}
