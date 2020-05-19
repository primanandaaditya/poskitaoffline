package com.kitadigi.poskita.dao.stok;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StokDAO {

    @Insert
    public void insert(Stok...stoks);

    @Update
    public void update(Stok...stoks);

    @Delete
    public void delete(Stok stok);

    @Query("SELECT * FROM stok")
    public List<Stok> semuaStok();

    @Query("SELECT * FROM stok WHERE id = :id")
    public Stok getStokById(Long id);

    @Query("DELETE FROM stok")
    public void hapusSemuaStok();

}
