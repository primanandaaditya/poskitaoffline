package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.fragment.editkategori.IEditKategoriOfflineRequest;
import com.kitadigi.poskita.fragment.editkategori.IEditKategoriResult;
import com.kitadigi.poskita.util.Constants;

public class KategoriEditDAO implements IEditKategoriOfflineRequest {

    Context context;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;
    private IEditKategoriResult iEditKategoriResult;

    public KategoriEditDAO(Context context, IEditKategoriResult iEditKategoriResult) {
        this.context = context;
        this.iEditKategoriResult = iEditKategoriResult;

        initRoom();

    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        kategoriDAO=database.getKategoriDAO();
    }


    @Override
    public void editKategori(Kategori kategori) {
        try {

            kategori.setSync_update(Constants.STATUS_BELUM_SYNC);
            kategoriDAO.update(kategori);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setMessage(context.getResources().getString(R.string.berhasil_diedit));

            if (iEditKategoriResult!=null){
                iEditKategoriResult.onEditSuccess(baseResponse);
            }

        }catch (Exception e){
            if (iEditKategoriResult!=null){
                iEditKategoriResult.onEditError(e.getMessage().toString());
            }
        }
    }

    @Override
    public Kategori getKategoriById(Long id) {
        Kategori kategori = kategoriDAO.getKategoriById(id);
        return kategori;
    }
}
