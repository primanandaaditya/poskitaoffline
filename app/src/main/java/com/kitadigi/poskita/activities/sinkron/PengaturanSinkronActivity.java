package com.kitadigi.poskita.activities.sinkron;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.AlarmReceiver;
import com.kitadigi.poskita.util.SessionManager;

public class PengaturanSinkronActivity extends BaseActivity {

    RadioButton radio_satu_jam,radio_dua_jam,radio_satu_hari;
    TextView tv_nav_header, tv_internal;
    ImageView iv_back;
    Button btn_save;

    private PendingIntent pendingIntent;

    //session untukk nyimpaan interval
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_sinkron);

        findID();

    }

    void findID(){

        //init session
        sessionManager = new SessionManager(PengaturanSinkronActivity.this);


        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(PengaturanSinkronActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(PengaturanSinkronActivity.this, 0, alarmIntent, 0);



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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //buat var untuk nampung interval
                long interval=3600000;

                //cek radio mana yang dipilih
                if (radio_satu_jam.isChecked()){
                    interval = 60 * 60 * 1000;
                }else if (radio_dua_jam.isChecked()){
                    interval = 2 * 60 * 60 * 1000;
                }else if (radio_satu_hari.isChecked()){
                    interval = 24 * 60 * 60 * 1000;
                }

                //simpan di session
                sessionManager.createIntervalSinkron(interval);

                //mulai pasang alarm
                start(interval);
            }
        });
    }


    //fungsi ini untuk mulai alarmmanager
    public void start(long interval) {
        Log.d("interval", String.valueOf(interval));
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

}
