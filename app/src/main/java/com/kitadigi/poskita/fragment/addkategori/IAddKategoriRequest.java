package com.kitadigi.poskita.fragment.addkategori;

public interface IAddKategoriRequest {

    void addKategori(String name_category,String code_category);
    void simpanOffline(boolean sudahSync, String name_category, String code_category);
}
