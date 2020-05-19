package com.kitadigi.poskita.dao.belimaster;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "belimaster")
public class BeliMaster {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    String nomor;
    String supplier_id;
    String ref_no;
    String contact_id;
    Integer total_pay;
    Integer total_price;
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

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public Integer getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(Integer total_pay) {
        this.total_pay = total_pay;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
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
