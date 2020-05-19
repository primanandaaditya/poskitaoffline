package com.kitadigi.poskita.sinkron.produk.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronUpdateProdukResult {
    void onSinkronUpdateProdukSuccess(SinkronResponse sinkronResponse);
    void onSinkronUpdateProdukError(String error);
}
