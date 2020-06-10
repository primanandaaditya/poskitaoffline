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

public class AlarmReceiver extends BroadcastReceiver {

    Calendar calendar = Calendar.getInstance();
    Sinkronizer sinkronizer;

    @Override
    public void onReceive(Context context, Intent intent) {

        //coba buat notifikasi
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setSmallIcon(R.drawable.ic_shopping_cart);

        mBuilder.setContentTitle("KitaDigi PosKita");
        mBuilder.setContentText("Sinkronisasi sedang diproses");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

        // For our recurring task, we'll just display a message
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
//        sinkronizer=new Sinkronizer(context);
//        sinkronizer.doSinkron();
//
        Log.d(calendar.getTime().toString(),"Proses sinkronisasi...");
    }
}
