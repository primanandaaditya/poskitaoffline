package com.kitadigi.poskita.dao.printer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "printer")
public class Printer {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    private String nama_printer;
    private String address_printer;

    public String getAddress_printer() {
        return address_printer;
    }

    public void setAddress_printer(String address_printer) {
        this.address_printer = address_printer;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getNama_printer() {
        return nama_printer;
    }

    public void setNama_printer(String nama_printer) {
        this.nama_printer = nama_printer;
    }
}
