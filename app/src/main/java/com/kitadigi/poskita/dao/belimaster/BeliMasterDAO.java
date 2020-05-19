package com.kitadigi.poskita.dao.belimaster;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface BeliMasterDAO {

    @Insert
    public void insert(BeliMaster...beliMasters);

    @Update
    public void update(BeliMaster...beliMasters);

    @Delete
    public void delete(BeliMaster beliMaster);


    @Query("SELECT * FROM belimaster")
    public List<BeliMaster> semuaBeli();


    @Query("SELECT * FROM belimaster WHERE sync_delete = 1")
    public List<BeliMaster> getBeli();


    @Query("DELETE FROM belimaster")
    public void hapusSemuaBeli();


    @Query("SELECT * FROM belimaster WHERE id = :id")
    public BeliMaster getBeliById(Long id);


    @Query("SELECT * FROM belimaster WHERE id = :id AND sync_insert = 0")
    public List<BeliMaster> getSudahTerbeli(Long id);

    @Query("SELECT * FROM belimaster WHERE nomor = :nomor")
    public BeliMaster getBeliMasterByNomor(String nomor);
}
