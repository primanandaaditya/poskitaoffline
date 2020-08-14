package com.kitadigi.poskita.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kitadigi.poskita.R;

import java.util.Calendar;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver implements ISinkronizer {

    InternetChecker internetChecker;
    Calendar calendar = Calendar.getInstance();
    Sinkronisasi sinkronizer;
    RemoteViews remoteViews;
    SessionManager sessionManager;

    String selesai;
    int random = 1010101;
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        //cek dulu apakah ada internet
        internetChecker = new InternetChecker();

        //jika ada internet
        if (internetChecker.haveNetwork(context)){

            selesai= context.getResources().getString(R.string.proses_sinkron_telah_selesai);

            //siapkan layout untuk notif
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.notifikasi_sinkron_small);


            //coba buat notifikasi
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            //pasang icon pada notifikasi
            mBuilder.setSmallIcon(R.drawable.ic_shopping_cart);


            //pasang icon large/besar
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_shopping_cart);
            mBuilder.setLargeIcon(icon);


            //pasang layout buatan sendiri
            mBuilder.setCustomContentView(remoteViews);
            mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

            //mulai create notifikasi dan tampilkan di HP

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(random, mBuilder.build());

            //init session manager untuk nyimpen jam dan tanggal lastsync
            sessionManager = new SessionManager(context);

            //mulai proses sinkronisasi ke server
            sinkronizer = new Sinkronisasi(context, this);
            sinkronizer.mulaiSinkron();

        }else{

            try{

                //coba buat notifikasi
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

                //pasang icon pada notifikasi
                mBuilder.setSmallIcon(R.drawable.ic_shopping_cart);


                //pasang icon large/besar
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_shopping_cart);
                mBuilder.setLargeIcon(icon);


                //pasang title
                mBuilder.setContentTitle(context.getResources().getString(R.string.kitadigi_poskita));

                //pasang subtitle
                mBuilder.setContentText(context.getResources().getString(R.string.tidak_ada_koneksi_internet));

                //mulai create notifikasi dan tampilkan di HP
                mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(random, mBuilder.build());

            }catch (Exception e){

            }



        }



    }

    @Override
    public void onNoInternet() {

        //jika tidak ada internet
        //hilangkan notifikasi dari hp user
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                mNotificationManager.cancel(random);
            }
        }, 3000);   //3 detik
    }

    @Override
    public void onBegin() {
        Log.d("on begin", "BEGIN");
    }

    @Override
    public void onProgress(Integer progress) {
        Log.d("on progress", "PROGRESS");
    }

    @Override
    public void onFinish(String pesan) {
        Log.d("on finish", "FINISH");
        remoteViews.setTextViewText(R.id.tv_text, selesai );

        //hilangkan notifikasi dari hp user
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                mNotificationManager.cancel(random);
            }
        }, 3000);   //3

    }

    @Override
    public void onSukses() {

        Log.d("on Sukses", "Sukses");
        remoteViews.setTextViewText(R.id.tv_text, selesai );

        //hilangkan notifikasi dari hp user
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                mNotificationManager.cancel(random);
            }
        }, 3000);   //3

        //simpan tanggal dan jam last sync
        sessionManager.createLasySync();
    }


}
