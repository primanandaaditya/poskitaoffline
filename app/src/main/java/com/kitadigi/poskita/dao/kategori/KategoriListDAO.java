package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.fragment.kategori.IKategoriRequest;
import com.kitadigi.poskita.fragment.kategori.IKategoriResult;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class KategoriListDAO implements IKategoriRequest {

    Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;
    private IKategoriResult iKategoriResult;

    public KategoriListDAO(Context context, IKategoriResult iKategoriResult) {
        this.context = context;
        this.iKategoriResult = iKategoriResult;

        initRoom();

        sessionManager=new SessionManager(context);
        bussiness_id = sessionManager.getLeadingZeroBussinessId();
    }

    @Override
    public void getKategoriList() {
        try {
            List<Kategori> kategoris = kategoriDAO.getKategori();
            iKategoriResult.onKategoriSuccess(null,kategoris);
        }catch (Exception e){
            iKategoriResult.onKategoriError(e.getMessage(),null);
        }
    }

    @Override
    public void cancelCall() {

    }

    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        kategoriDAO=database.getKategoriDAO();
    }

}
