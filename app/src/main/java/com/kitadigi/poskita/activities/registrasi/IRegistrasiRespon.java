package com.kitadigi.poskita.activities.registrasi;

public interface IRegistrasiRespon {

    void onRegistrasiSuccess(RegistrasiRespon registrasiRespon);
    void onRegistrasiError(String error);
}
