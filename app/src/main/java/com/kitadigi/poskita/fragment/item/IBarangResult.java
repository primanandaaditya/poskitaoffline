package com.kitadigi.poskita.fragment.item;

import com.kitadigi.poskita.dao.produk.Item;

import java.util.List;

public interface IBarangResult {

    void onSuccess(BarangResult barangResult, List<Item> items);
    void onError(String error,List<Item> items);
}
