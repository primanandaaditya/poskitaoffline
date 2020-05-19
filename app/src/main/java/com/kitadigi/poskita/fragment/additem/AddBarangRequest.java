package com.kitadigi.poskita.fragment.additem;

public class AddBarangRequest {

    String nama_barang;
    Integer harga_beli;
    Integer harga_jual;

    public AddBarangRequest(String nama_barang, Integer harga_beli, Integer harga_jual) {
        this.nama_barang = nama_barang;
        this.harga_beli = harga_beli;
        this.harga_jual = harga_jual;
    }
}
