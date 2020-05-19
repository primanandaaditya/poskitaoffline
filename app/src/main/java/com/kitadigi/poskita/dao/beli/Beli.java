package com.kitadigi.poskita.dao.beli;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "beli")
public class Beli {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    String id_product_master;
    String kode_id_produk;
    String supplier_id;
    String ref_no;
    String qty;
    String price;
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

    public String getId_product_master() {
        return id_product_master;
    }

    public void setId_product_master(String id_product_master) {
        this.id_product_master = id_product_master;
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
