package com.kitadigi.poskita.sinkron.beli.get_master;

import com.kitadigi.poskita.sinkron.jual.get_master.MasterModel;
import com.kitadigi.poskita.util.Url;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ISinkronGetBeliMaster {

    @GET(Url.SINKRON_PEMBELIAN_GET_MASTER)
    Call<GetBeliMasterModel> getBeliMaster(
    );
}
