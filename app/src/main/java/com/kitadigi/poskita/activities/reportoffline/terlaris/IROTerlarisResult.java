package com.kitadigi.poskita.activities.reportoffline.terlaris;

import java.util.List;

public interface IROTerlarisResult {

    void onTerlarisSuccess(List<ROTerlarisModel> roTerlarisModels);
    void onTerlarisError(String error);
}
