package com.kitadigi.poskita.dao.produk;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kitadigi.poskita.dao.brand.Brand;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    public void insert(Item...items);

    @Update
    public void update(Item...items);

    @Delete
    public void delete(Item item);


    @Query("SELECT * FROM item")
    public List<Item> semuaItem();


    @Query("SELECT * FROM item WHERE sync_delete = 1")
    public List<Item> getItem();


    @Query("SELECT * FROM item WHERE id = :id")
    public Item getItemById(Long id);

    @Query("DELETE FROM item")
    public void hapusSemuaItem();


    //ini dipakai untuk list kategori yang sync-delete nya 0
    //nanti mau dilooping
    //dan dilempar ke API
    @Query("SELECT * FROM item WHERE sync_delete = 0")
    public List<Item> getItemTerhapus();




    //ini untuk hapus data kategori yang sync-delete nya 0
    //mau dihapus dari sqlite
    //kalau sudah nembak API delete
    @Query("DELETE FROM item WHERE sync_delete = 0")
    public void hapusItemSudahSync();


    //fungsi untuk cek duplikasi
    @Query("SELECT * FROM item WHERE name_product = :nama AND sync_delete = 1")
    public List<Item> cekNamaItem(String nama);

    @Query("SELECT * FROM item WHERE kode_id =:kode_id")
    public Item getItemByKodeId(String kode_id);

    @Query("SELECT * FROM item WHERE code_product = :code_product")
    public Item getItemByBarkode(String code_product);

    @Query("SELECT * FROM item WHERE category_mobile_id =:category_mobile_id")
    public List<Item> getItemByCategoryMobileId(String category_mobile_id);

    @Query("SELECT * FROM item WHERE brand_mobile_id =:brand_mobile_id")
    public List<Item> getItemByBrandMobileId(String brand_mobile_id);

    @Query("SELECT * FROM item WHERE unit_mobile_id =:unit_mobile_id")
    public  List<Item> getItemByUnitMobileId(String unit_mobile_id);


    //fungsi untuk mengurangi stok barang, karena penjualan
    @Query("UPDATE item SET qty_stock = qty_stock - :jumlah WHERE kode_id = :kode_id")
    public void kurangiStok(Integer jumlah,String kode_id);


    //fungsi untuk menambah stok barang, karena pembelian
    @Query("UPDATE item SET qty_stock = qty_stock + :jumlah WHERE kode_id = :kode_id")
    public void tambahStok(Integer jumlah,String kode_id);

}
