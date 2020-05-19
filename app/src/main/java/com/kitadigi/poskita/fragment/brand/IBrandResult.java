package com.kitadigi.poskita.fragment.brand;

import com.kitadigi.poskita.dao.brand.Brand;

import java.util.List;

public interface IBrandResult {
    void onBrandSuccess(BrandModel brandModel, List<Brand> brandOffline);
    void onBrandError(String error,List<Brand> brandOffline);
}
