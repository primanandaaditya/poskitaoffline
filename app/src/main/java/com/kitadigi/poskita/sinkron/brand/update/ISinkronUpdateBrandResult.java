package com.kitadigi.poskita.sinkron.brand.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronUpdateBrandResult {
    void onSinkronUpdateBrandSuccess(SinkronResponse sinkronResponse);
    void onSinkronUpdateBrandError(String error);
}
