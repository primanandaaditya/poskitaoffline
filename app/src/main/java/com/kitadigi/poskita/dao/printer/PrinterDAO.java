package com.kitadigi.poskita.dao.printer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PrinterDAO {

    @Insert
    public void insert(Printer printer);

    @Update
    public void update(Printer printer);

    @Delete
    public void delete(Printer printer);

    @Query("SELECT * FROM printer")
    public List<Printer> semuaPrinter();

    @Query("DELETE FROM printer")
    public void hapusSemuaPrinter();


}
