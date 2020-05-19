package com.kitadigi.poskita.dao.jual;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JualDAO {

    @Insert
    public void insert(Jual...juals);

    @Update
    public void update(Jual...juals);

    @Delete
    public void delete(Jual jual);


    @Query("SELECT * FROM jual")
    public List<Jual> semuaJual();


    @Query("SELECT * FROM jual WHERE sync_delete = 1")
    public List<Jual> getJual();


    @Query("DELETE FROM jual")
    public void hapusSemuaJual();


    @Query("SELECT * FROM jual WHERE id = :id")
    public Jual getJualById(Long id);


    @Query("SELECT * FROM jual WHERE id = :id AND sync_insert = 0")
    public List<Jual> getSudahTerjual(Long id);

}
