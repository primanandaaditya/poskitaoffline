package com.kitadigi.poskita.dao.produk;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class ItemHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static ItemDAO itemDAO;

    //constructor
    public ItemHelper(Context context) {
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
        itemDAO=database.getItemDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Item> semuaItem(){
        return itemDAO.semuaItem();
    }

    public void addItem(Item item){
        itemDAO.insert(item);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllItem(){
        itemDAO.hapusSemuaItem();
    }

    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Item> getAllItem(){
        return itemDAO.getItem();
    }

    //fungsi ini untuk get berdasarkan ID
    public Item getItemById(Long id){
        return itemDAO.getItemById(id);
    }

    //fungsi ini untuk update brand
    public void updateItem(Item item){
        itemDAO.update(item);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server


    public boolean cekNamaItem(String nama_item){

        boolean hasil = false;

        //cari nama  yang sama
        List<Item> items = itemDAO.cekNamaItem(nama_item);

        //jika jumlah list =0 (belum ada), return false
        int jumlah = items.size();

        if (jumlah==0){
            hasil=false;
        }else{
            hasil=true;
        }

        return hasil;
    }

    public Item getItemByKodeId(String kode_id){
        return itemDAO.getItemByKodeId(kode_id);
    }

    public Item getItemByBarkode(String barkode){
        return itemDAO.getItemByBarkode(barkode);
    }

    public List<Item> getItemByCategoryMobileId(String categoryMobileId){
        return itemDAO.getItemByCategoryMobileId(categoryMobileId);
    }

    public List<Item> getItemByBrandMobileId(String brandMobileId){
        return itemDAO.getItemByBrandMobileId(brandMobileId);
    }

    public List<Item> getItemByUnitMobileId(String unitMobileId){
        return itemDAO.getItemByUnitMobileId(unitMobileId);
    }

    public boolean adaItemByCategoryMobileId(String categoryMobileId){

        boolean hasil;

        //query list item yang punya mobile id sesuai param
        List<Item> items = getItemByCategoryMobileId(categoryMobileId);

        //jika jumlahnya 0
        if (items.size()==0){

            //berarti belum ada yang pakai
            hasil = false;
        }else{

            //sudah ada yang pakai
            hasil = true;
        }

        Log.d("ada", String.valueOf(hasil));
        return hasil;
    }

    public boolean adaItemByBrandMobileId(String brandMobileId){

        boolean hasil;

        //query list item yang punya mobile id sesuai param
        List<Item> items = getItemByBrandMobileId(brandMobileId);

        //jika jumlahnya 0
        if (items.size()==0){

            //berarti belum ada yang pakai
            hasil = false;
        }else{

            //sudah ada yang pakai
            hasil = true;
        }

        Log.d("ada", String.valueOf(hasil));
        return hasil;
    }

    public boolean adaItemByUnitMobileId(String unitMobileId){

        boolean hasil;

        //query list item yang punya mobile id sesuai param
        List<Item> items = getItemByUnitMobileId(unitMobileId);

        //jika jumlahnya 0
        if (items.size()==0){

            //berarti belum ada yang pakai
            hasil = false;
        }else{

            //sudah ada yang pakai
            hasil = true;
        }

        Log.d("ada", String.valueOf(hasil));
        return hasil;
    }

    public void kurangiStok(Integer jumlah,String kode_id){
        itemDAO.kurangiStok(jumlah,kode_id);
    }

    public void tambahStok(Integer jumlah, String kode_id){
        itemDAO.tambahStok(jumlah, kode_id);
    }
}
