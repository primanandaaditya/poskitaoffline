package com.kitadigi.poskita.dao.beli;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BeliDAO {

    @Insert
    public void insert(Beli...belis);

    @Update
    public void update(Beli...belis);

    @Delete
    public void delete(Beli beli);


    @Query("SELECT * FROM beli")
    public List<Beli> semuaBeli();


    @Query("SELECT * FROM beli WHERE sync_delete = 1")
    public List<Beli> getBeli();


    @Query("DELETE FROM beli")
    public void hapusSemuaBeli();


    @Query("SELECT * FROM beli WHERE id = :id")
    public Beli getBeliById(Long id);


    @Query("SELECT * FROM beli WHERE id = :id AND sync_insert = 0")
    public List<Beli> getSudahTerbeli(Long id);
}
