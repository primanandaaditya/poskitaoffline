package com.kitadigi.poskita.util;

public interface ISinkronizer {
    void onNoInternet();
    void onBegin();
    void onProgress();
    void onFinish(String pesan);
}
