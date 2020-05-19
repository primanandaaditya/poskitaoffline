package com.kitadigi.poskita.dao.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kitadigi.poskita.dao.beli.Beli;
import com.kitadigi.poskita.dao.beli.BeliDAO;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailDAO;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterDAO;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.brand.BrandDAO;
import com.kitadigi.poskita.dao.jual.Jual;
import com.kitadigi.poskita.dao.jual.JualDAO;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterDAO;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailDAO;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriDAO;
import com.kitadigi.poskita.dao.printer.Printer;
import com.kitadigi.poskita.dao.printer.PrinterDAO;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemDAO;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.dao.stok.StokDAO;
import com.kitadigi.poskita.dao.struk.Struk;
import com.kitadigi.poskita.dao.struk.StrukDAO;
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.dao.unit.UnitDAO;


@Database(entities =
        {
                Printer.class,
            Kategori.class,
                Brand.class,
                Struk.class,
                Unit.class,
                Item.class,
                Stok.class,
                Jual.class,
                Beli.class,
                JualMaster.class,
                JualDetail.class,
                BeliMaster.class,
                BeliDetail.class
        }, version = 1, exportSchema = false)

public abstract class Db extends RoomDatabase {
    public abstract KategoriDAO getKategoriDAO();
    public abstract BrandDAO getBrandDAO();
    public abstract PrinterDAO getPrinterDAO();
    public abstract StrukDAO getStrukDAO();
    public abstract UnitDAO getUnitDAO();
    public abstract ItemDAO getItemDAO();
    public abstract StokDAO getStokDAO();
    public abstract JualDAO getJualDAO();
    public abstract BeliDAO getBeliDAO();
    public abstract JualMasterDAO getJualMasterDAO();
    public abstract JualDetailDAO getJualDetailDAO();
    public abstract BeliMasterDAO getBeliMasterDAO();
    public abstract BeliDetailDAO getBeliDetailDAO();
}
