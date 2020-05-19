package com.kitadigi.poskita.sinkron.unit.update;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronUpdateUnitResult {
    void onSinkronUpdateUnitSuccess(SinkronResponse sinkronResponse);
    void onSinkronUpdateUnitError(String error);
}
