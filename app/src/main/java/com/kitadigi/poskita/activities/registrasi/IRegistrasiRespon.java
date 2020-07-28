package com.kitadigi.poskita.activities.registrasi;

import com.kitadigi.poskita.model.Status;

public interface IRegistrasiRespon {

    void onRegistrasiSuccess(Status status);
    void onRegistrasiError(String error);
}
