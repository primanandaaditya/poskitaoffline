package com.kitadigi.poskita.dao.stok;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class StokHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static StokDAO stokDAO;

    //constructor
    public StokHelper(Context context) {
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
        stokDAO=database.getStokDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Stok> semuaStok(){
        return stokDAO.semuaStok();
    }

    public void addStok(Stok stok){
        stokDAO.insert(stok);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllStok(){
        stokDAO.hapusSemuaStok();
    }

    //fungsi ini untuk get berdasarkan ID
    public Stok getStokById(Long id){
        return stokDAO.getStokById(id);
    }

    //fungsi ini untuk update brand
    public void updateStok(Stok stok){
        stokDAO.update(stok);
    }

}
