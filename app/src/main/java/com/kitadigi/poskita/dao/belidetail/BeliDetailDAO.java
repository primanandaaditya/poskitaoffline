package com.kitadigi.poskita.dao.belidetail;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BeliDetailDAO {

    @Insert
    public void insert(BeliDetail...beliDetails);

    @Update
    public void update(BeliDetail...beliDetails);

    @Delete
    public void delete(BeliDetail beliDetail);


    @Query("SELECT * FROM belidetail")
    public List<BeliDetail> semuaBeli();


    @Query("DELETE FROM belidetail")
    public void hapusSemuaBeli();


    @Query("SELECT * FROM belidetail WHERE id = :id")
    public BeliDetail getBeliById(Long id);

    @Query("SELECT * FROM belidetail WHERE nomor = :nomor")
    public List<BeliDetail> getBeliByNomor(String nomor);

    @Query("SELECT * FROM belidetail WHERE kode_id_produk = :kode_id_produk")
    public List<BeliDetail> getBeliDetailByMobileId(String kode_id_produk);

}
