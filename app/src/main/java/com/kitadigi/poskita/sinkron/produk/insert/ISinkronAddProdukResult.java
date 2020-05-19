package com.kitadigi.poskita.sinkron.produk.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddProdukResult {
    void onSinkronAddProdukSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddProdukError(String error);
}
