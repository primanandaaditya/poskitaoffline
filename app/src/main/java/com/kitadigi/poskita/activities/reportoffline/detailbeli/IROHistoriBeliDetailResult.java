package com.kitadigi.poskita.activities.reportoffline.detailbeli;

import com.kitadigi.poskita.activities.reportoffline.detailjual.DetailJualModel;

import java.util.List;

public interface IROHistoriBeliDetailResult {
    void onHistoriBeliDetailSuccess(List<DetailBeliModel> detailBeliModels);
    void onHistoriBeliDetailError(String error);
}
