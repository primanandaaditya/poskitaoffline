package com.kitadigi.poskita.dao.database;


import android.content.Context;

import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.dao.brand.BrandHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.dao.kategori.KategoriHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.dao.unit.UnitHelper;

//class ini berfungsi untuk
//menghapus semua isi tabel pada sqlite
//dipakai jika user logout
public class HapusTabel implements IHapusTabel {

    Context context;

    //deklarasikan semua tabel sqlite
    BeliDetailHelper beliDetailHelper;
    BeliMasterHelper beliMasterHelper;
    BrandHelper brandHelper;
    ItemHelper itemHelper;
    JualDetailHelper jualDetailHelper;
    JualMasterHelper jualMasterHelper;
    KategoriHelper kategoriHelper;
    UnitHelper unitHelper;

    public HapusTabel(Context context) {
        this.context = context;
        initTabel();
    }

    @Override
    public void initTabel() {
        beliDetailHelper = new BeliDetailHelper(context);
        beliMasterHelper = new BeliMasterHelper(context);
        brandHelper = new BrandHelper(context);
        itemHelper = new ItemHelper(context);
        jualDetailHelper = new JualDetailHelper(context);
        jualMasterHelper = new JualMasterHelper(context);
        kategoriHelper = new KategoriHelper(context);
        unitHelper = new UnitHelper(context);
    }

    @Override
    public void execute() {
        beliDetailHelper.deleteAllBeli();
        beliMasterHelper.deleteAllBeli();
        brandHelper.deleteAllBrand();
        itemHelper.deleteAllItem();
        jualDetailHelper.deleteAllJual();
        jualMasterHelper.deleteAllJual();
        kategoriHelper.deleteAllKategori();
        unitHelper.deleteAllUnit();
    }
}
