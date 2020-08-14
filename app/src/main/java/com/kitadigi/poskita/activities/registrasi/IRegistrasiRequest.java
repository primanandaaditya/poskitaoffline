package com.kitadigi.poskita.activities.registrasi;

public interface IRegistrasiRequest {

    void registrasi(
            String name,
            String province_id,
            String city_id,
            String subdistrict_id,
            String landmark,
            String telephone,
            String store_name,
            String type_store,
            String email,
            String annotation

    );
}