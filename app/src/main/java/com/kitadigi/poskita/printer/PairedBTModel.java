package com.kitadigi.poskita.printer;

public class PairedBTModel {

    String nama_bluetooth,address;

    public PairedBTModel(String nama_bluetooth, String address) {
        this.nama_bluetooth = nama_bluetooth;
        this.address = address;
    }

    public String getNama_bluetooth() {
        return nama_bluetooth;
    }

    public void setNama_bluetooth(String nama_bluetooth) {
        this.nama_bluetooth = nama_bluetooth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
