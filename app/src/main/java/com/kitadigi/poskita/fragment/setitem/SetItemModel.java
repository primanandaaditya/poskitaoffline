package com.kitadigi.poskita.fragment.setitem;

import com.kitadigi.poskita.fragment.brand.BrandModel;
import com.kitadigi.poskita.fragment.kategori.KategoriModel;
import com.kitadigi.poskita.fragment.unit.UnitModel;

public class SetItemModel {
    KategoriModel kategoriModel;
    BrandModel brandModel;
    UnitModel unitModel;

    public KategoriModel getKategoriModel() {
        return kategoriModel;
    }

    public void setKategoriModel(KategoriModel kategoriModel) {
        this.kategoriModel = kategoriModel;
    }

    public BrandModel getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(BrandModel brandModel) {
        this.brandModel = brandModel;
    }

    public UnitModel getUnitModel() {
        return unitModel;
    }

    public void setUnitModel(UnitModel unitModel) {
        this.unitModel = unitModel;
    }
}
