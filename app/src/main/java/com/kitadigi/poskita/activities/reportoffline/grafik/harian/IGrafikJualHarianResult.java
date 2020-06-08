package com.kitadigi.poskita.activities.reportoffline.grafik.harian;

import java.util.List;

public interface IGrafikJualHarianResult {

    void onGrafikJualHarianSuccess(List<GrafikJualHarianModel> grafikJualHarianModels);
    void onGrafikJualHarianError(String error);
}
