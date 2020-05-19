package com.kitadigi.poskita.dao.belidetail;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class BeliDetailHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static BeliDetailDAO beliDAO;

    //constructor
    public BeliDetailHelper(Context context) {
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
       beliDAO=database.getBeliDetailDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<BeliDetail> semuaBeli(){
        return beliDAO.semuaBeli();
    }

    public void addBeli(BeliDetail beliDetail){
        beliDAO.insert(beliDetail);
    }

    public BeliDetail getBeliById(Long id){
        return beliDAO.getBeliById(id);
    }

    public List<BeliDetail> getBeliByNomor(String nomor){
        return beliDAO.getBeliByNomor(nomor);
    }

    public List<BeliDetail> getBeliDetailByMobileId(String kode_produk){
        return beliDAO.getBeliDetailByMobileId(kode_produk);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllBeli(){
        beliDAO.hapusSemuaBeli();
    }

    //fungsi ini untuk update brand
    public void updateBeli(BeliDetail beliDetail){
        beliDAO.update(beliDetail);
    }

}
