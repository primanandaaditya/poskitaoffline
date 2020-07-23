package com.kitadigi.poskita.fragment.unit.dengan_header;

import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.unit.dengan_header.UnitModel;

import java.util.List;

public interface IUnitResult {

    void onUnitSuccess(UnitModel unitModel, List<Unit> units);
    void onUnitError(String error, List<Unit> units);
}
