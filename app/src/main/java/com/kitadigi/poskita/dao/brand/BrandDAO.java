package com.kitadigi.poskita.dao.brand;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BrandDAO {

    @Insert
    public void insert(Brand... brands);

    @Update
    public void update(Brand... brands);

    @Delete
    public void delete(Brand brand);


    @Query("SELECT * FROM brand")
    public List<Brand> semuaBrand();


    @Query("SELECT * FROM brand WHERE sync_delete = 1")
    public List<Brand> getBrand();


    @Query("SELECT * FROM brand WHERE id = :id")
    public Brand getBrandById(Long id);

    @Query("DELETE FROM brand")
    public void hapusSemuaBrand();


    //ini dipakai untuk list kategori yang sync-delete nya 0
    //nanti mau dilooping
    //dan dilempar ke API
    @Query("SELECT * FROM brand WHERE sync_delete = 0")
    public List<Brand> getBrandTerhapus();

    //ini untuk hapus data kategori yang sync-delete nya 0
    //mau dihapus dari sqlite
    //kalau sudah nembak API delete
    @Query("DELETE FROM brand WHERE sync_delete = 0")
    public void hapusBrandSudahSync();
}
