package com.kitadigi.poskita.dao.jualdetail;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "jualdetail")
public class JualDetail {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    String nomor;
    String kode_id;
    Integer qty;
    Integer price;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getKode_id() {
        return kode_id;
    }

    public void setKode_id(String kode_id) {
        this.kode_id = kode_id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
