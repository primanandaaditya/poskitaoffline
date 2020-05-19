package com.kitadigi.poskita.sinkron.jual.insert;

import com.kitadigi.poskita.sinkron.retrofit.SinkronResponse;

public interface ISinkronAddJualResult {
    void onSinkronAddJualSuccess(SinkronResponse sinkronResponse);
    void onSinkronAddJualError(String error);
}
