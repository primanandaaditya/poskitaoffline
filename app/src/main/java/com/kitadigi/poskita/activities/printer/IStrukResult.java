package com.kitadigi.poskita.activities.printer;

import com.kitadigi.poskita.dao.struk.Struk;

import java.util.List;

public interface IStrukResult {
    void onStrukSuccess(List<Struk> struks);
    void onStrukError(String error);
}
