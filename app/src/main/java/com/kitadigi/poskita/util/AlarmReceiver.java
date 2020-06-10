package com.kitadigi.poskita.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.kitadigi.poskita.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver implements ISinkronizer {

    Calendar calendar = Calendar.getInstance();
    Sinkronizer sinkronizer;


    @Override
    public void onReceive(Context context, Intent intent) {

        //coba buat notifikasi
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        //pasang icon pada notifikasi
        mBuilder.setSmallIcon(R.drawable.ic_shopping_cart);

        //pasang title
        mBuilder.setContentTitle(context.getResources().getString(R.string.kitadigi_poskita));

        //pasang subtitle
        mBuilder.setContentText(context.getResources().getString(R.string.proses_sinkron_sedang_diproses));

        //mulai create notifikasi dan tampilkan di HP
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        Log.d(calendar.getTime().toString(),"Proses sinkronisasi sedang berjalan...");

        //mulai proses sinkronisasi ke server
        sinkronizer = new Sinkronizer(context, this);
        sinkronizer.doSinkron();

    }

    @Override
    public void onBegin() {
        Log.d("on begin", "BEGIN");
    }

    @Override
    public void onProgress() {
        Log.d("on progress", "PROGRESS");
    }

    @Override
    public void onFinish(String pesan) {
        Log.d("on finish", "FINISH");
    }
}
