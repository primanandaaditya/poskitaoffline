package com.kitadigi.poskita.sinkron.brand.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddBrandResult {
    void onSinkronAddBrandSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddBrandError(String error);
}
