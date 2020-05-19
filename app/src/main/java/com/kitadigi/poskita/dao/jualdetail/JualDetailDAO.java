package com.kitadigi.poskita.dao.jualdetail;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface JualDetailDAO {

    @Insert
    public void insert(JualDetail...jualDetails);

    @Update
    public void update(JualDetail...jualDetails);

    @Delete
    public void delete(JualDetail jualDetail);

    @Query("SELECT * FROM jualdetail")
    public List<JualDetail> semuaJual();


    @Query("DELETE FROM jualdetail")
    public void hapusSemuaJual();


    @Query("SELECT * FROM jualdetail WHERE nomor = :nomor")
    public List<JualDetail> getJualDetailByNomor(String nomor);


    @Query("SELECT * FROM jualdetail WHERE kode_id = :kode_id")
    public List<JualDetail> getJualDetailByKodeId(String kode_id);

}
