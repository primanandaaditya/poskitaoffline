package com.kitadigi.poskita.sinkron.unit.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddUnitResult {
    void onSinkronAddUnitSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddUnitError(String error);
}
