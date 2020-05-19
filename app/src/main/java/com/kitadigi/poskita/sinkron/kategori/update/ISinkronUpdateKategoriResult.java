package com.kitadigi.poskita.sinkron.kategori.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronUpdateKategoriResult {
    void onSinkronUpdateKategoriSuccess(SinkronResponse sinkronResponse);
    void onSinkronUpdateKategoriError(String error);
}
