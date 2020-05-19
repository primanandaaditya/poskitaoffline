package com.kitadigi.poskita.dao.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriDAO;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

public class RoomHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;

    //constructor
    public RoomHelper(Context context) {
        this.context = context;
        initRoom();

        sessionManager=new SessionManager(context);
        bussiness_id = sessionManager.getLeadingZeroBussinessId();
    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
    }


    //fungsi ini untuk membentuk variabel kategori
    //kalau sukses nembak API, sudahSync diisi true
    public void insertKategoriBuilder(boolean sudahSync,String nama_kategori,String kode_kategori){
        Kategori kategori=new Kategori();

        if (sudahSync==true){
            kategori.setName_category(nama_kategori);
            kategori.setCode_category(kode_kategori);
            kategori.setKode_id(bussiness_id + StringUtil.timeMilis());
            kategori.setSync_insert(Constants.STATUS_SUDAH_SYNC);
            kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
            kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        }else{
            kategori.setName_category(nama_kategori);
            kategori.setCode_category(kode_kategori);
            kategori.setKode_id(bussiness_id + StringUtil.timeMilis());
            kategori.setSync_insert(Constants.STATUS_BELUM_SYNC);
            kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
            kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);
        }

        kategoriDAO = database.getKategoriDAO();
        kategoriDAO.insert(kategori);
    }

}
