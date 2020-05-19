package com.kitadigi.poskita.dao.beli;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class BeliHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static BeliDAO beliDAO;

    //constructor
    public BeliHelper(Context context) {
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
       beliDAO=database.getBeliDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Beli> semuaBeli(){
        return beliDAO.semuaBeli();
    }

    public void addBeli(Beli beli){
        beliDAO.insert(beli);
    }

    public Beli getBeliById(Long id){
        return beliDAO.getBeliById(id);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllBeli(){
        beliDAO.hapusSemuaBeli();
    }

    //fungsi ini untuk update brand
    public void updateBeli(Beli beli){
        beliDAO.update(beli);
    }


    public List<Beli> getSudahTerbeli(Long id){
        return beliDAO.getSudahTerbeli(id);
    }

    public int sudahTerbeli(Long id){
        int hasil = 0;
        List<Beli> belis = getSudahTerbeli(id);

        //looping untuk sum qty terbeli
        for (Beli beli:belis){
            hasil = hasil + Integer.parseInt(beli.getQty());
        }
        return hasil;
    }

}
