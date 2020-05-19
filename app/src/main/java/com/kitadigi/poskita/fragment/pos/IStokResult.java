package com.kitadigi.poskita.fragment.pos;

import com.kitadigi.poskita.dao.stok.Stok;

import java.util.List;

public interface IStokResult {
    void onStokSuccess(StokModel stokModel, List<Stok> stokOffline);
    void onStokError(String error, List<Stok> stoksOffline);
}
