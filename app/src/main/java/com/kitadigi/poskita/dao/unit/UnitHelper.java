package com.kitadigi.poskita.dao.unit;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;

import java.util.List;

public class UnitHelper {

    private Context context;

    //var session untuk get bussiness id
    static SessionManager sessionManager;

    //var string untuk bussiness id
    static String bussiness_id;

    //untuk sqlite room
    static Db database;
    static UnitDAO unitDAO;

    //constructor
    public UnitHelper(Context context) {
        this.context = context;
        initRoom();

        sessionManager=new SessionManager(context);
        bussiness_id = sessionManager.getLeadingZeroBussinessId();
    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        unitDAO=database.getUnitDAO();
    }


    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Unit> semuaUnit(){
        return unitDAO.semuaUnit();
    }



    public void addUnit(Unit unit){
        unitDAO.insert(unit);
    }

    //fungsi ini untuk menghapus semua record brand
    public void deleteAllUnit(){
        unitDAO.hapusSemuaUnit();
    }

    //fungsi ini untuk mendapatkan semua brand
    //yang tersimpan di tabel sqlite
    public List<Unit> getAllUnit(){
        return unitDAO.getUnit();
    }




    //fungsi ini untuk get berdasarkan ID
    public Unit getUnitById(Long id){
        return unitDAO.getUnitById(id);
    }

    //fungsi ini untuk update brand
    public void updateUnit(Unit unit){
        unitDAO.update(unit);
    }

    //fungsi ini untuk looping brand sqlite yg belum disync
    //lalu disimpan ke server


    //cek duplikasi
    public boolean cekNamaUnit(String nama_unit){

        boolean hasil = false;

        //cari nama  yang sama
        List<Unit> units = unitDAO.cekNamaUnit(nama_unit);

        //jika jumlah list =0 (belum ada), return false
        int jumlah = units.size();

        if (jumlah==0){
            hasil=false;
        }else{
            hasil=true;
        }

        return hasil;
    }

    public String inputMassal(List<UnitData> unitDataList){

        String hasil = "";

        //jika parameter null
        if (unitDataList == null || unitDataList.size() == 0){

            hasil = context.getResources().getString(R.string.tidak_ada_input);
        }else{

            //boolean untuk cek duplikasi
            boolean ada;

            //buat variabel kategori model
            Unit unit;

            //looping array
            for (UnitData unitData: unitDataList){

                //cek duplikasi dulu
                ada = cekNamaUnit(unitData.getName());

                //jika ada, jangan masukkan ke database
                if (ada){

                }else{

                    //init unit model
                    unit = new Unit();

                    //setting nilai
                    unit.setName(unitData.getName());
                    unit.setSingkatan(unitData.getShort_name());
                    unit.setKode_id(unitData.getId());
                    unit.setSync_insert(Constants.STATUS_BELUM_SYNC);
                    unit.setSync_update(Constants.STATUS_SUDAH_SYNC);
                    unit.setSync_delete(Constants.STATUS_SUDAH_SYNC);

                    //commit perubahan
                    unitDAO.insert(unit);
                }

            }

            hasil = context.getResources().getString(R.string.input_telah_tersimpan);
        }

        return hasil;
    }
}
