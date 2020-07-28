package com.kitadigi.poskita.activities.kecamatan;


public interface IKecamatanResult {
    void onGetKecamatanSuccess(KecamatanModel kecamatanModel);
    void onGetKecamatanError(String error);
}
