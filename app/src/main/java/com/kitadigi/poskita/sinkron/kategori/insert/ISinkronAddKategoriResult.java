package com.kitadigi.poskita.sinkron.kategori.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddKategoriResult {
    void onSinkronAddKategoriSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddKategoriError(String error);
}
