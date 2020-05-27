package com.kitadigi.poskita.dao.jualmaster;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JualMasterDAO {

    @Insert
    public void insert(JualMaster...jualMasters);

    @Update
    public void update(JualMaster...jualMasters);

    @Delete
    public void delete(JualMaster jualMaster);


    @Query("SELECT * FROM jualmaster")
    public List<JualMaster> semuaJual();


    @Query("SELECT * FROM jualmaster WHERE sync_delete = 1")
    public List<JualMaster> getJual();


    @Query("DELETE FROM jualmaster")
    public void hapusSemuaJual();


    @Query("SELECT * FROM jualmaster WHERE id = :id")
    public JualMaster getJualById(Long id);


    @Query("SELECT * FROM jualmaster WHERE nomor = :nomor")
    public JualMaster getJualMasterByNomor(String nomor);


    @Query("SELECT * FROM jualmaster WHERE id = :id AND sync_insert = 0")
    public List<JualMaster> getSudahTerjual(Long id);

    @Query("SELECT * FROM jualmaster WHERE tanggal = :tanggal")
    public List<JualMaster> getJualMasterByTanggal(String tanggal);

}
