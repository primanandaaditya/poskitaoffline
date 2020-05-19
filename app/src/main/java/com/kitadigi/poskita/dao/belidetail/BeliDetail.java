package com.kitadigi.poskita.dao.belidetail;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "belidetail")
public class BeliDetail {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    String nomor;
    String kode_id_produk;
    String qty;
    String price;


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

    public String getKode_id_produk() {
        return kode_id_produk;
    }

    public void setKode_id_produk(String kode_id_produk) {
        this.kode_id_produk = kode_id_produk;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
