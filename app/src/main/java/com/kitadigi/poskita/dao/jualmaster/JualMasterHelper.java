package com.kitadigi.poskita.dao.jualmaster;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class JualMasterHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static JualMasterDAO jualDAO;

    //constructor
    public JualMasterHelper(Context context) {
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
       jualDAO=database.getJualMasterDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<JualMaster> semuaJual(){
        return jualDAO.semuaJual();
    }

    public void addJualMaster(JualMaster jualMaster){
        jualDAO.insert(jualMaster);
    }

    public JualMaster getJualMasterById(Long id){
        return jualDAO.getJualById(id);
    }


    public JualMaster getJualMasterByNomor(String nomor){
        return jualDAO.getJualMasterByNomor(nomor);
    }


    //fungsi ini untuk menghapus semua record brand
    public void deleteAllJual(){
        jualDAO.hapusSemuaJual();
    }

    //fungsi ini untuk update brand
    public void updateJualMaster(JualMaster jualMaster){
        jualDAO.update(jualMaster);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server

    public List<JualMaster> getSudahTerjual(Long id){
        return jualDAO.getSudahTerjual(id);
    }


}
