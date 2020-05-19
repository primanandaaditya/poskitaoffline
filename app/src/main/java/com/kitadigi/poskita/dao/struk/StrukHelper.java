package com.kitadigi.poskita.dao.struk;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StrukHelper {

    private Context context;

    //untuk sqlite room
    static Db database;
    static StrukDAO strukDAO;

    //constructor
    public StrukHelper(Context context) {
        this.context = context;
        initRoom();

    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        strukDAO=database.getStrukDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Struk> semuaStruk(){
        return strukDAO.semuaStruk();
    }



    public void addStruk(Struk struk){
        strukDAO.insert(struk);
    }

    //fungsi ini untuk menghapus semua record struk
    public void deleteAllStruk(){
        strukDAO.hapusSemuaStruk();
    }

    //fungsi ini untuk get berdasarkan ID
    public Struk getStrukById(Long id){
        return strukDAO.getStrukById(id);
    }


    public void newStruk(String kontenStruk){

        //init struk baru
        Struk struk = new Struk();

        //init current datetime
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        //string tanggal jam sekarang
        String tanggal = dateFormat.format(date).toString();

        struk.setTanggal(tanggal);
        struk.setDeskripsi(kontenStruk);

        addStruk(struk);

    }

    public void deleteStrukById(Long id){
        strukDAO.deleteStrukById(id);
    }

}
