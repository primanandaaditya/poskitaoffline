package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface KategoriDAO {

    @Insert
    public void insert(Kategori... kategoris);

    @Update
    public void update(Kategori... kategoris);

    @Delete
    public void delete(Kategori kategori);


    @Query("SELECT * FROM kategori")
    public List<Kategori> semuaKategori();


    @Query("SELECT * FROM kategori WHERE sync_delete = 1")
    public List<Kategori> getKategori();

    @Query("SELECT * FROM kategori WHERE sync_insert = 0")
    public List<Kategori> getKategoriInsertBelumSync();

    @Query("SELECT * FROM kategori WHERE id = :id")
    public Kategori getKategoriById(Long id);

    @Query("DELETE FROM kategori")
    public void hapusSemuaKategori();


    //ini dipakai untuk list kategori yang sync-delete nya 0
    //nanti mau dilooping
    //dan dilempar ke API
    @Query("SELECT * FROM kategori WHERE sync_delete = 0")
    public List<Kategori> getKategoriTerhapus();

    //ini untuk hapus data kategori yang sync-delete nya 0
    //mau dihapus dari sqlite
    //kalau sudah nembak API delete
    @Query("DELETE FROM kategori WHERE sync_delete = 0")
    public void hapusKategoriSudahSync();

    //untuk validasi duplikasi
    @Query("SELECT * FROM kategori WHERE  name_category = :name_category AND sync_delete = 1")
    public List<Kategori> cekNamaKategori(String name_category);
}
