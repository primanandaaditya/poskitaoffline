package com.kitadigi.poskita.activities.sinkron;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;

public class PengaturanSinkronActivity extends BaseActivity {

    RadioButton radio_satu_jam,radio_dua_jam,radio_satu_hari;
    TextView tv_nav_header, tv_internal;
    ImageView iv_back;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_sinkron);

        findID();

    }

    void findID(){

        //find id
        radio_satu_jam = (RadioButton)findViewById(R.id.radio_satu_jam);
        radio_dua_jam = (RadioButton)findViewById(R.id.radio_dua_jam);
        radio_satu_hari = (RadioButton)findViewById(R.id.radio_satu_hari);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_internal=(TextView)findViewById(R.id.tv_interval);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        btn_save=(Button)findViewById(R.id.btn_save);

        //aply font
        this.applyFontRegularToRadioButton(radio_dua_jam);
        this.applyFontRegularToRadioButton(radio_satu_hari);
        this.applyFontRegularToRadioButton(radio_satu_jam);
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontBoldToTextView(tv_internal);
        this.applyFontBoldToButton(btn_save);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
