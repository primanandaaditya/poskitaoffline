package com.kitadigi.poskita.activities.reportoffline.revenue;

import java.util.List;

public interface IRORevenueResult {
    void onRORevenueSuccess(List<RevenueModel> revenueModels);
    void onRORevenueError(String error);
}
