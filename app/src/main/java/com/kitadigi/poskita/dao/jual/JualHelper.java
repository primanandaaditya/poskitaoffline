package com.kitadigi.poskita.dao.jual;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class JualHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static JualDAO jualDAO;

    //constructor
    public JualHelper(Context context) {
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
       jualDAO=database.getJualDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Jual> semuaJual(){
        return jualDAO.semuaJual();
    }

    public void addJual(Jual jual){
        jualDAO.insert(jual);
    }

    public Jual getJualById(Long id){
        return jualDAO.getJualById(id);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllJual(){
        jualDAO.hapusSemuaJual();
    }

    //fungsi ini untuk update brand
    public void updateJual(Jual jual){
        jualDAO.update(jual);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server

    public List<Jual> getSudahTerjual(Long id){
        return jualDAO.getSudahTerjual(id);
    }

    public int sudahTerjual(Long id){
        int hasil = 0;
        List<Jual> juals = getSudahTerjual(id);

        //looping untuk sum qty terjual
        for (Jual jual:juals){
            hasil = hasil + Integer.parseInt(jual.getQty());
        }
        return hasil;
    }
}
