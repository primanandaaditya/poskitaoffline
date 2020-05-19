package com.kitadigi.poskita.fragment.unit;

import com.kitadigi.poskita.dao.unit.Unit;

import java.util.List;

public interface IUnitResult {

    void onUnitSuccess(UnitModel unitModel, List<Unit> units);
    void onUnitError(String error, List<Unit> units);
}
