package com.kitadigi.poskita.dao.brand;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "brand")
public class Brand {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    private String kode_id;
    private String business_id;
    private String name;
    private String description;
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

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
