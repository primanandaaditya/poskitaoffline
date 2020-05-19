package com.kitadigi.poskita.sinkron.kategori.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronDeleteKategoriResult {
    void onSinkronDeleteKategoriSuccess(SinkronResponse sinkronResponse);
    void onSinkronDeleteKategoriError(String error);
}
