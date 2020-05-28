package com.kitadigi.poskita.activities.reportoffline.detailjual;

import java.util.List;

public interface IROHistoriJualDetailRequest {
    void getReport(String nomor_transaksi);
    Integer getTotalItem(List<DetailJualModel> detailJualModels);
    Integer getGrandTotal(List<DetailJualModel> detailJualModels);
}
