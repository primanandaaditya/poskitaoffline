package com.kitadigi.poskita.fragment.additem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface IAddBarangRequest {

    public void addBarang(MultipartBody.Part image,
                          RequestBody name_product,
                          Integer brand_id,
                          Integer category_id,
                          Integer unit_id,
                          Integer purchase_price,
                          Integer sell_price,
                          String code_product,
                          Integer qty_stock,
                          Integer qty_minimum,
                          String category_mobile_id,
                          String brand_mobile_id,
                          String unit_mobile_id
                          );

}
