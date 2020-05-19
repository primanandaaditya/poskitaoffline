package com.kitadigi.poskita.dao.brand;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class BrandHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static BrandDAO brandDAO;

    //constructor
    public BrandHelper(Context context) {
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
        brandDAO=database.getBrandDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Brand> semuaBrand(){
        return brandDAO.semuaBrand();
    }



    public void addBrand(Brand brand){
        brandDAO.insert(brand);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllBrand(){
        brandDAO.hapusSemuaBrand();
    }

    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Brand> getAllBrand(){
        return brandDAO.getBrand();
    }




    //fungsi ini untuk get berdasarkan ID
    public Brand getBrandById(Long id){
        return brandDAO.getBrandById(id);
    }

    //fungsi ini untuk update brand
    public void updateBrand(Brand brand){
        brandDAO.update(brand);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server


}
