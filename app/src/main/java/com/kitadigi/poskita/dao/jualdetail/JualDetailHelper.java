package com.kitadigi.poskita.dao.jualdetail;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.dao.models.SumQtyJualDetail;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class JualDetailHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static JualDetailDAO jualDAO;

    //constructor
    public JualDetailHelper(Context context) {
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
       jualDAO=database.getJualDetailDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<JualDetail> semuaJual(){
        return jualDAO.semuaJual();
    }

    public void addJualDetail(JualDetail jualDetail){
        jualDAO.insert(jualDetail);
    }

    public List<JualDetail> getJualDetailByNomor(String nomor){
        return jualDAO.getJualDetailByNomor(nomor);
    }


    public List<JualDetail> getJualDetailByKodeId(String kode_id){
        return jualDAO.getJualDetailByKodeId(kode_id);
    }

    public List<SumQtyJualDetail> getSumQtyJualDetail(){
        return jualDAO.getSumQtyJualDetail();
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllJual(){
        jualDAO.hapusSemuaJual();
    }

    //fungsi ini untuk update brand
    public void updateJualDetail(JualDetail jualDetail){
        jualDAO.update(jualDetail);
    }




}
