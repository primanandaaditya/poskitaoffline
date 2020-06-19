package com.kitadigi.poskita.activities.registrasi;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrasiController implements IRegistrasiRequest {

    private Context context;
    private IRegistrasiRespon iRegistrasiRespon;
    private IRegistrasi iRegistrasi;

    public RegistrasiController(Context context, IRegistrasiRespon iRegistrasiRespon) {
        this.context = context;
        this.iRegistrasiRespon = iRegistrasiRespon;
    }

    @Override
    public void registrasi(String email, String nama, String telepon, String jenis_toko, String nama_toko, String alamat_toko, String alamat_pemilik, String keterangan) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

        iRegistrasi = RegistrasiUtil.getInterface();
        iRegistrasi.doRegistrasi(email, nama, telepon, jenis_toko, nama_toko, alamat_toko, alamat_pemilik, keterangan).enqueue(new Callback<RegistrasiRespon>() {
            @Override
            public void onResponse(Call<RegistrasiRespon> call, Response<RegistrasiRespon> response) {

                iRegistrasiRespon.onRegistrasiSuccess(response.body());
            }

            @Override
            public void onFailure(Call<RegistrasiRespon> call, Throwable t) {

                iRegistrasiRespon.onRegistrasiError(t.getMessage());
            }
        });
    }
}
