package com.kitadigi.poskita.dao.struk;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StrukDAO {

    @Insert
    public void insert(Struk...struks);

    @Update
    public void update(Struk...struks);

    @Delete
    public void delete(Struk struk);

    @Query("SELECT * FROM struk")
    public List<Struk> semuaStruk();

    @Query("SELECT * FROM struk WHERE id = :id")
    public Struk getStrukById(Long id);

    @Query("DELETE FROM struk")
    public void hapusSemuaStruk();

    @Query("DELETE FROM struk WHERE id = :id")
    public void deleteStrukById(Long id);

}
