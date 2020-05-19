package com.kitadigi.poskita.activities.pembelian;

public interface IAddPembelianRequest {
    void addPembelian(
            String supplier_id,
            String ref_no,
            Integer total_pay,
            Integer total_price
    );
}
