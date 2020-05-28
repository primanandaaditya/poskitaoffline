package com.kitadigi.poskita.activities.reportoffline.detailbeli;

import com.kitadigi.poskita.activities.reportoffline.detailjual.DetailJualModel;

import java.util.List;

public interface IROHistoriBeliDetailRequest {
    void getReport(String nomor_transaksi);
    Integer getTotalItem(List<DetailBeliModel> detailBeliModels);
    Integer getGrandTotal(List<DetailBeliModel> detailBeliModels);
}
