package com.kitadigi.poskita.util;

public interface ISinkronizer {
    void onNoInternet();
    void onBegin();
    void onProgress(Integer progress);
    void onFinish(String pesan);
    void onSukses();
}
