package com.kitadigi.poskita.activities.reportoffline.analisa;

import java.util.List;

public interface IROAnalisaResult {

    void onROAnalisaSuccess(List<ROAnalisaModel> roAnalisaModels);

    void onROAnalisaGagal(String error);
    
}
