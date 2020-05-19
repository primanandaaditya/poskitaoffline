package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteKategoriOffline;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteResult;
import com.kitadigi.poskita.util.Constants;

public class KategoriDeleteDAO implements IDeleteKategoriOffline {

    Context context;



    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;
    private IDeleteResult iDeleteResult;

    public KategoriDeleteDAO(Context context, IDeleteResult iDeleteResult) {
        this.context = context;
        this.iDeleteResult=iDeleteResult;
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
    public Kategori getKategoriById(Long id) {
        Kategori kategori = kategoriDAO.getKategoriById(id);
        return kategori;
    }

    @Override
    public void deleteKategori(Kategori kategori) {
        try {

//            kategoriDAO.delete(kategori);
            kategori.setSync_delete(Constants.STATUS_BELUM_SYNC);
            kategoriDAO.update(kategori);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setMessage(context.getResources().getString(R.string.berhasil_dihapus));

            iDeleteResult.onDeleteSuccess(baseResponse);
        }catch (Exception e){
            iDeleteResult.onDeleteError(e.getMessage().toString());
        }
    }
}
