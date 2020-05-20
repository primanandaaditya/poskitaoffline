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
import com.kitadigi.poskita.fragment.deletekategori.DeleteKategoriUtil;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteKategori;
import com.kitadigi.poskita.fragment.editkategori.EditKategoriUtil;
import com.kitadigi.poskita.fragment.editkategori.IEditKategori;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.fragment.kategori.IKategori;
import com.kitadigi.poskita.fragment.kategori.KategoriModel;
import com.kitadigi.poskita.fragment.kategori.KategoriUtil;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//class ini untuk looping
//data kategori yang belum disync
//INSERT-nya

public  class InsertKategori  {

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

    public InsertKategori(Context context) {
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



    //sync yang pertama adalah INSERT dulu
    //fungsi dibawah untuk menambahkan data di server
    //data-data yang belum disync online
    //sekaligus juga untuk mendapatkan ID server
    //karena ID server beda dengan ID sqlite
    public void syncKategoriInsert() {

        //get semua kategori
        List<Kategori> kategoris = kategoriHelper.semuaKategori();

        //init retrofit
        iAddKategori = AddKategoriUtil.getInterface();

        //looping untuk dilemparkan ke database online
        for(final Kategori kategori:kategoris){

            //filter looping untuk mendapatkan semua kategori
            //yang belum di-sync
            if (kategori.getSync_insert()==Constants.STATUS_BELUM_SYNC){

                //mulai nembak API
//                iAddKategori.addKategori(enkriptedUserId,kategori.getName_category(),kategori.getCode_category())
//                        .enqueue(new Callback<BaseResponse>() {
//                            @Override
//                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                                Log.d("sync ktgr OK", call.request().url().toString());
//                            }
//
//                            @Override
//                            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                                Log.d("sync ktgr gagal", call.request().url().toString());
//                            }
//                        });
            }
        }
    }




    //sync yang kedua adalah SELECT
    //fungsi dibawah untuk get semua data online
    //yang ditampung sementara dalam variabel dataOffline
    public void syncKategoriSelect() {

        //init retrofit
        IKategori iKategori;
        iKategori = KategoriUtil.getInterface();

        //get semua kategori online
        iKategori.getKategoriList(enkriptedUserId)
        .enqueue(new Callback<KategoriModel>() {
            @Override
            public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {

                //kalau API sukses
                Log.d("select OK", call.request().url().toString());

                //kalau API sukses, tampung di dataOffline
                dataOnline = response.body().getData();

                //sync update
                syncKategoriUpdate();
            }

            @Override
            public void onFailure(Call<KategoriModel> call, Throwable t) {
                //kalau API gagal
                Log.d("gagal select", t.getMessage().toString());
            }
        });

    }




    //sync untuk menghapus di db online
    //untuk mencari id online, harus looping
    //mencari berdasarkan short code
    public void syncKategoriDelete() {

        //var untuk nampung shortcode
        String shortCode;

        //var untuk nampung id online
        String idOnline;


        //init retrofit
        IDeleteKategori iDeleteKategori;
        iDeleteKategori= DeleteKategoriUtil.getInterface();

        //looping pada db offline
        //filter data mana saja yang sync_delete harus di-sync

        for(Kategori kategori:dataOffline){

            //jika data harus disync
            if (kategori.getSync_delete()==Constants.STATUS_BELUM_SYNC){

                //tampung shortCode
                shortCode = kategori.getCode_category();

                //cari id online
                //berdasarkan shortCode
                Datum datum = findUsingIterator(shortCode,dataOnline);
                if (datum ==null){
                    //tidak ketemu id
                }else{
                    //jika ketemu id
                    idOnline = simpleMD5.generate(datum.getId());


                    //mulai nembak API
                    iDeleteKategori.deleteKategori(idOnline,enkriptedUserId,"").enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            Log.d("sukses delete", call.request().url().toString());
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.d("gagal delete", call.request().url().toString());
                        }
                    });
                }
            }

        }


    }



    //sync untuk mengedit di db online
    //untuk mencari id online, harus looping
    //mencari berdasarkan short code
    public void syncKategoriUpdate() {

        //var untuk nampung shortcode
        String shortCode;

        //var untuk nampung id online
        String idOnline;

        //init retrofit
        IEditKategori iEditKategori;
        iEditKategori = EditKategoriUtil.getInterface();


        //looping pada db offline
        //filter data mana saja yang sync_update harus di-sync

        for(Kategori kategori:dataOffline){

            //jika data harus disync
            if (kategori.getSync_update()==Constants.STATUS_BELUM_SYNC){

                //tampung shortcode
                shortCode = kategori.getCode_category();

                //cari id online
                //berdasarkan shortCode
                Datum datum = findUsingIterator(shortCode,dataOnline);
                if (datum ==null){
                    //tidak ketemu id
                }else{
                    //jika ketemu id
                    idOnline = simpleMD5.generate(datum.getId());


                    //mulai nembak API
                    iEditKategori.editKategori(idOnline,enkriptedUserId,kategori.getName_category(),kategori.getCode_category())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            Log.d("sukses update", call.request().url().toString());
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.d("gagal update", call.request().url().toString());
                        }
                    });

                }
            }

        }


    }


    //ini adalah fungsi get semua dari server
    //lalu ditampung di tabel sqlite
    public void syncLastSelect() {

        //init retrofit
        IKategori iKategori;
        iKategori = KategoriUtil.getInterface();

        //get semua kategori online
        iKategori.getKategoriList(enkriptedUserId)
                .enqueue(new Callback<KategoriModel>() {
                    @Override
                    public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {

                        //kalau API sukses
                        Log.d("select OK", call.request().url().toString());

                        //kalau API sukses, tampung di dataOffline
                        dataOnline = response.body().getData();

                        //simpan data online ke data offline
                        //pakai looping
                        Kategori kategori;
                        for (Datum datum:dataOnline){
                            kategori=new Kategori();
                            kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);
                            kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
                            kategori.setSync_insert(Constants.STATUS_SUDAH_SYNC);
                            kategori.setName_category(datum.getName());
                            kategori.setCode_category(datum.getCode_category());
                            kategori.setId(Long.parseLong(datum.getId()));

                            //commit insert ke sqlite
                            kategoriHelper.addKategori(kategori);
                        }


                    }

                    @Override
                    public void onFailure(Call<KategoriModel> call, Throwable t) {
                        //kalau API gagal
                        Log.d("gagal select", t.getMessage().toString());
                    }
                });

    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();

    }


    private Datum findUsingIterator(
            String shortCode, List<Datum> datumList) {
        Iterator<Datum> iterator = datumList.iterator();
        while (iterator.hasNext()) {
            Datum datum = iterator.next();
            if (datum.getCode_category().equals(shortCode)) {
                return datum;
            }
        }
        return null;
    }


}
