package com.kitadigi.poskita.fragment.unit.dengan_header;

import com.kitadigi.poskita.fragment.unit.dengan_header.UnitModel;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IUnit {

    @GET(Url.DIKI_MASTER_UNIT)
    Call<UnitModel> getUnitList(
            @Header(Constants.auth_token) String auth_token
    );
}
