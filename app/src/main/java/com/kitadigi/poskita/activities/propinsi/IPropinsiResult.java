package com.kitadigi.poskita.activities.propinsi;

import java.util.List;

public interface IPropinsiResult {
    void onGetPropinsiSuccess(PropinsiModel propinsiModel);
    void onGetPropinsiError(String error);
}
