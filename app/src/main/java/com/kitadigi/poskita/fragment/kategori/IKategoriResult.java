package com.kitadigi.poskita.fragment.kategori;

import com.kitadigi.poskita.dao.kategori.Kategori;

import java.util.List;

public interface IKategoriResult {
    void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline);
    void onKategoriError(String error,List<Kategori> kategoriOffline);
}
