package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class KategoriHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;

    //constructor
    public KategoriHelper(Context context) {
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
        kategoriDAO=database.getKategoriDAO();
    }


    //fungsi ini untuk mendapatkan semua kategori
    //yang tersimpan di tabel sqlite
    public List<Kategori> semuaKategori(){
        return kategoriDAO.semuaKategori();
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

        kategoriDAO.insert(kategori);
    }


    public void addKategori(Kategori kategori){
        kategoriDAO.insert(kategori);
    }

    //fungsi ini untuk menghapus semua record kategori
    public void deleteAllKategori(){
        kategoriDAO.hapusSemuaKategori();
    }

    //fungsi ini untuk mendapatkan semua kategori
    //yang tersimpan di tabel sqlite
    public List<Kategori> getAllKategori(){
        return kategoriDAO.getKategori();
    }

    //fungsi ini untuk mendapatkan semua kategori
    //yang tersimpan di tabel sqlite tapi belum di-sync
    public List<Kategori> getKategoriInsertBelumSync(){
        return kategoriDAO.getKategoriInsertBelumSync();
    }


    //fungsi ini untuk get berdasarkan ID
    public Kategori getKategoriById(Long id){
        return kategoriDAO.getKategoriById(id);
    }

    //fungsi ini untuk update kategori
    public void updateKategori(Kategori kategori){
        kategoriDAO.update(kategori);
    }

    //fungsi ini untuk looping kategori sqlite yg belum disync
    //lalu disimpan ke server


}
