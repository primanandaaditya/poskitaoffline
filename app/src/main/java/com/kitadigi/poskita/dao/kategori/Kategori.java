package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "kategori")
public class Kategori {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    private String kode_id;
    private String name_category;
    private String code_category;
    private Integer sync_insert;
    private Integer sync_delete;
    private Integer sync_update;



    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getKode_id() {
        return kode_id;
    }

    public void setKode_id(String kode_id) {
        this.kode_id = kode_id;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public String getCode_category() {
        return code_category;
    }

    public void setCode_category(String code_category) {
        this.code_category = code_category;
    }

    public Integer getSync_insert() {
        return sync_insert;
    }

    public void setSync_insert(Integer sync_insert) {
        this.sync_insert = sync_insert;
    }

    public Integer getSync_delete() {
        return sync_delete;
    }

    public void setSync_delete(Integer sync_delete) {
        this.sync_delete = sync_delete;
    }

    public Integer getSync_update() {
        return sync_update;
    }

    public void setSync_update(Integer sync_update) {
        this.sync_update = sync_update;
    }
}
