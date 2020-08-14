package com.kitadigi.poskita.activities.kota;


public interface IKotaResult {
    void onGetKotaSuccess(KotaModel kotaModel);
    void onGetKotaError(String error);
}
