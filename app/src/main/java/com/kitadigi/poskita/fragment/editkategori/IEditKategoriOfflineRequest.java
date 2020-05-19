package com.kitadigi.poskita.fragment.editkategori;

import com.kitadigi.poskita.dao.kategori.Kategori;

public interface IEditKategoriOfflineRequest {
    void editKategori(Kategori kategori);
    Kategori getKategoriById(Long id);
}
