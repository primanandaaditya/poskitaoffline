package com.kitadigi.poskita.activities.coba;

public interface ICobaResult {
    void onSuccess(CobaModel cobaModel);
    void onError(String error);
}
