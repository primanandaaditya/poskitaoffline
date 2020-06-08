package com.kitadigi.poskita.activities.reportoffline.detailjual;

import com.kitadigi.poskita.dao.jualmaster.JualMaster;

import java.util.List;

public interface IROHistoriJualDetailResult {
    void onHistoriJualDetailSuccess(List<DetailJualModel> detailJualModels);
    void onHistoriJualDetailError(String error);
}
