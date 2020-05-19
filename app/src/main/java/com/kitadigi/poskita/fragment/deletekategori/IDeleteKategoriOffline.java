package com.kitadigi.poskita.fragment.deletekategori;

import com.kitadigi.poskita.dao.kategori.Kategori;

public interface IDeleteKategoriOffline {

    void deleteKategori(Kategori kategori);
    Kategori getKategoriById(Long id);
}
