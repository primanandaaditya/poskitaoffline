package com.kitadigi.poskita.sinkron.beli.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddBeliResult {
    void onSinkronAddBeliSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddBeliError(String error);
}
