package com.kitadigi.poskita.activities.propinsi;

import java.util.List;

public interface IPropinsiResult {
    void onGetPropinsiSuccess(List<String> strings);
    void onGetPropinsiError(String error);
}
