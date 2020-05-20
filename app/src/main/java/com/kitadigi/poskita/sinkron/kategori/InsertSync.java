package com.kitadigi.poskita.sinkron.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.fragment.addkategori.AddKategoriUtil;
import com.kitadigi.poskita.fragment.addkategori.IAddKategori;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertSync {


    Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //var string untuk user id(MD5)
    String enkriptedUserId;

    //untuk sqlite room
    static Db database;
    KategoriHelper kategoriHelper;

    //untuk retrofit
    IAddKategori iAddKategori;


    //list<kategori>untuk nampung sqlite yg ada sekarang
    List<Kategori> dataOffline;
    List<Datum> dataOnline;

    //untuk hash
    SimpleMD5 simpleMD5;


    public InsertSync(Context context) {
        this.context = context;
        initRoom();

        sessionManager=new SessionManager(context);
        enkriptedUserId=sessionManager.getEncryptedIdUsers();
        kategoriHelper = new KategoriHelper(context);

        //isi variabel dataOffline
        dataOffline = kategoriHelper.semuaKategori();

        //untuk hash
        simpleMD5=new SimpleMD5();
    }

    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    public Future<String> insert() {
        return executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                //get semua kategori
                List<Kategori> kategoris = kategoriHelper.semuaKategori();

                //init retrofit
                iAddKategori = AddKategoriUtil.getInterface();

                //looping untuk dilemparkan ke database online
                for(final Kategori kategori:kategoris){

                    //filter looping untuk mendapatkan semua kategori
                    //yang belum di-sync
                    if (kategori.getSync_insert()== Constants.STATUS_BELUM_SYNC){

                        //mulai nembak API
//                        iAddKategori.addKategori(enkriptedUserId,kategori.getName_category(),kategori.getCode_category())
//                                .enqueue(new Callback<BaseResponse>() {
//                                    @Override
//                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                                        Log.d("sync ktgr OK", call.request().url().toString());
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<BaseResponse> call, Throwable t) {
//                                        Log.d("sync ktgr gagal", call.request().url().toString());
//                                    }
//                                });
                    }
                }
                return "";
            }
        });
    }


    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();

    }
}
