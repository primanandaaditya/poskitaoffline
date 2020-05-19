package com.kitadigi.poskita.sinkron.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriDAO;
import com.kitadigi.poskita.dao.kategori.KategoriDeleteDAO;
import com.kitadigi.poskita.fragment.deletekategori.DeleteKategoriUtil;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteKategori;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.SimpleMD5;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//class ini untuk looping
//data kategori yang belum disync
//INSERT-nya

public class DeleteKategori extends AsyncTask<String,String,String> {

    Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //var string untuk user id(MD5)
    String enkriptedUserId;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;
    KategoriDeleteDAO kategoriDeleteDAO;

    //untuk retrofit
    IDeleteKategori iDeleteKategori;

    //
    SimpleMD5 simpleMD5;
    String enkripIdKategori;


    public DeleteKategori(Context context) {
        this.context = context;
        initRoom();

        //untuk hapus record, kalau nembak API sukses
        kategoriDeleteDAO=new KategoriDeleteDAO(context,null);

        sessionManager=new SessionManager(context);
        enkriptedUserId=sessionManager.getEncryptedIdUsers();

        simpleMD5=new SimpleMD5();
    }

    @Override
    protected String doInBackground(String... strings) {

        //get semua record kategori
        List<Kategori> kategoris = kategoriDAO.getKategoriTerhapus();
        Log.d("jml hapus",String.valueOf(kategoris.size()));
       //init retrofit
        iDeleteKategori= DeleteKategoriUtil.getInterface();


        //looping untuk dilemparkan ke database online
        for(final Kategori kategori:kategoris){

            enkripIdKategori = simpleMD5.generate(kategori.getId().toString());
            Log.d("enkrip",enkripIdKategori);

            iDeleteKategori.deleteKategori( enkripIdKategori,enkriptedUserId,"")
            .enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Log.d("del ktgr OK", call.request().url().toString());
                    //jika berhasil

                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.d("del ktgr gagal", call.request().url().toString());
                }
            });

        }

        return "";

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d("hasil","Delete kategori SYNC selesai");

        //hapus kategori yang sync-deletenya 0
        //kalau sudah selesai looping
//        kategoriDAO.hapusKategoriSudahSync();
    }

    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        kategoriDAO=database.getKategoriDAO();
    }
}
