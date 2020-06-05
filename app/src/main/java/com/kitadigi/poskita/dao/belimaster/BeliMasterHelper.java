package com.kitadigi.poskita.dao.belimaster;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.activities.reportoffline.analisa.ROAnalisaModel;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class BeliMasterHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static BeliMasterDAO beliDAO;

    //constructor
    public BeliMasterHelper(Context context) {
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
       beliDAO=database.getBeliMasterDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<BeliMaster> semuaBeli(){
        return beliDAO.semuaBeli();
    }

    public void addBeli(BeliMaster beliMaster){
        beliDAO.insert(beliMaster);
    }

    public BeliMaster getBeliById(Long id){
        return beliDAO.getBeliById(id);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllBeli(){
        beliDAO.hapusSemuaBeli();
    }

    //fungsi ini untuk update brand
    public void updateBeli(BeliMaster beliMaster){
        beliDAO.update(beliMaster);
    }

    public List<BeliMaster> getSudahTerbeli(Long id){
        return beliDAO.getSudahTerbeli(id);
    }

    public BeliMaster getBeliMasterByNomor(String nomor){
        return beliDAO.getBeliMasterByNomor(nomor);
    }

    public List<BeliMaster> getBeliMasterByTanggal(String tanggal){
        return beliDAO.getBeliMasterByTanggal(tanggal);
    }


    public List<ROAnalisaModel> analisaModel (String tanggalDari,String tanggalSampai){
        return beliDAO.analisaModel(tanggalDari, tanggalSampai);
    }
}
