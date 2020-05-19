package com.kitadigi.poskita.sinkron.brand.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronDeleteBrandResult {
    void onSinkronDeleteBrandSuccess(SinkronResponse sinkronResponse);
    void onSinkronDeleteBrandError(String error);
}
