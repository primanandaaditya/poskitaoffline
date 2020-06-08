package com.kitadigi.poskita.activities.reportoffline.kartustok;

import java.util.List;

public interface IKartuStokResult {

    void onSuccessKartuStok(List<KartuStokModel> kartuStokModels);
    void onErrorKartuStok(String error);
}
