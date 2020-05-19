package com.kitadigi.poskita.sinkron.produk.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronDeleteProdukResult {
    void onSinkronDeleteProdukSuccess(SinkronResponse sinkronResponse);
    void onSinkronDeleteProdukError(String error);
}
