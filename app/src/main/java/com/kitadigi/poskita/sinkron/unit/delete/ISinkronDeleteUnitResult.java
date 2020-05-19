package com.kitadigi.poskita.sinkron.unit.delete;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronDeleteUnitResult {
    void onSinkronDeleteUnitSuccess(SinkronResponse sinkronResponse);
    void onSinkronDeleteUnitError(String error);
}
