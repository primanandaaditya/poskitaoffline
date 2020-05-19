package com.kitadigi.poskita.dao.produk;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class ItemHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static ItemDAO itemDAO;

    //constructor
    public ItemHelper(Context context) {
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
        itemDAO=database.getItemDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Item> semuaItem(){
        return itemDAO.semuaItem();
    }

    public void addItem(Item item){
        itemDAO.insert(item);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllItem(){
        itemDAO.hapusSemuaItem();
    }

    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Item> getAllItem(){
        return itemDAO.getItem();
    }

    //fungsi ini untuk get berdasarkan ID
    public Item getItemById(Long id){
        return itemDAO.getItemById(id);
    }

    //fungsi ini untuk update brand
    public void updateItem(Item item){
        itemDAO.update(item);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server


}
