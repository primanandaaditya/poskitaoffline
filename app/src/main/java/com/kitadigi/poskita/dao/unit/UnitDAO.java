package com.kitadigi.poskita.dao.unit;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kitadigi.poskita.dao.brand.Brand;

import java.util.List;

@Dao
public interface UnitDAO {

    @Insert
    public void insert(Unit...units);

    @Update
    public void update(Unit...units);

    @Delete
    public void delete(Unit unit);


    @Query("SELECT * FROM unit")
    public List<Unit> semuaUnit();


    @Query("SELECT * FROM unit WHERE sync_delete = 1")
    public List<Unit> getUnit();

    @Query("SELECT * FROM unit WHERE id = :id")
    public Unit getUnitById(Long id);

    @Query("DELETE FROM unit")
    public void hapusSemuaUnit();


    //ini dipakai untuk list kategori yang sync-delete nya 0
    //nanti mau dilooping
    //dan dilempar ke API
    @Query("SELECT * FROM unit WHERE sync_delete = 0")
    public List<Unit> getUnitTerhapus();

    //ini untuk hapus data kategori yang sync-delete nya 0
    //mau dihapus dari sqlite
    //kalau sudah nembak API delete
    @Query("DELETE FROM unit WHERE sync_delete = 0")
    public void hapusUnitSudahSync();


    //fungsi untuk cek duplikasi
    @Query("SELECT * FROM unit WHERE name = :nama AND sync_delete = 1")
    public List<Unit> cekNamaUnit(String nama);
}
