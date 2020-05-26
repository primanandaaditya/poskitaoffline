package com.kitadigi.poskita.dao.kategori;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.fragment.addkategori.IAddKategoriRequest;
import com.kitadigi.poskita.fragment.addkategori.IAddKategoriResult;
import com.kitadigi.poskita.fragment.kategori.IKategoriResult;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

public class KategoriInsertDAO implements IAddKategoriRequest {

    Context context;
    IAddKategoriResult iAddKategoriResult;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static KategoriDAO kategoriDAO;
    private IKategoriResult iKategoriResult;

    public KategoriInsertDAO(Context context, IAddKategoriResult iAddKategoriResult) {
        this.context = context;
        this.iAddKategoriResult = iAddKategoriResult;

        initRoom();

        sessionManager=new SessionManager(context);
        bussiness_id = sessionManager.getLeadingZeroBussinessId();
    }

    @Override
    public void addKategori(String name_category, String code_category) {

        try {

            //ciptakan var kategori
            Kategori kategori = new Kategori();
            kategori.setName_category(name_category);
            kategori.setCode_category(code_category);
            kategori.setKode_id(bussiness_id + StringUtil.timeMilis());
            kategori.setSync_insert(Constants.STATUS_BELUM_SYNC);
            kategori.setSync_update(Constants.STATUS_SUDAH_SYNC);
            kategori.setSync_delete(Constants.STATUS_SUDAH_SYNC);

            //simpan di sqlite
            kategoriDAO.insert(kategori);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setMessage(context.getResources().getString(R.string.berhasil_disimpan));

            iAddKategoriResult.onSuccess(baseResponse);
        }catch (Exception e){
            iAddKategoriResult.onError(e.getMessage().toString());
        }
    }

    @Override
    public void simpanOffline(boolean sudahSync, String name_category, String code_category) {

    }

    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        kategoriDAO=database.getKategoriDAO();
    }
}
