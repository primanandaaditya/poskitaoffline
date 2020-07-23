package com.kitadigi.poskita.fragment.kategori.dengan_header;

import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.fragment.kategori.dengan_header.KategoriModel;

import java.util.List;

public interface IKategoriResult {
    void onKategoriSuccess(KategoriModel kategoriModel, List<Kategori> kategoriOffline);
    void onKategoriError(String error, List<Kategori> kategoriOffline);
}
