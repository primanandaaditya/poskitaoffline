package com.kitadigi.poskita.dao.printer;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kitadigi.poskita.dao.database.Db;

import java.util.List;

public class PrinterHelper {

    private Context context;


    //untuk sqlite room
    static Db database;
    static PrinterDAO printerDAO;

    //constructor
    public PrinterHelper(Context context) {
        this.context = context;
        initRoom();
    }


    //fungsi ini untuk inisialisasi database sqlite
    public void initRoom(){
        database = Room.databaseBuilder(context, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();
        printerDAO=database.getPrinterDAO();
    }

    //fungsi ini untuk mendapatkan semua pprinter
    //yang tersimpan di tabel sqlite
    public List<Printer> semuaPrinter(){
        return printerDAO.semuaPrinter();
    }

    public void addPrinter(Printer printer){
        printerDAO.insert(printer);
    }

    //fungsi ini untuk menghapus semua record printer
    public void hapusSemuaPrinter(){
        printerDAO.hapusSemuaPrinter();
    }

    //fungsi ini untuk menyimpann nama printer bluetooth
    public void simpanNamaPrinter(String namaPrinter,String addressPrinter){
        hapusSemuaPrinter();
        Printer printer = new Printer();
        printer.setNama_printer(namaPrinter);
        printer.setAddress_printer(addressPrinter);
        addPrinter(printer);
    }

    public boolean adaPrinter(){
        List<Printer> printers = printerDAO.semuaPrinter();
        if (printers.size()==0){
            return false;
        }else{
            return true;
        }
    }

    public String namaPrinter(){
        boolean ada = adaPrinter();
        if (ada){
            List<Printer> nama = semuaPrinter();
            Printer printer = nama.get(0);
            return printer.getNama_printer();
        }else{
            return "";
        }
    }

    public String addressPrinter(){
        boolean ada = adaPrinter();
        if (ada){
            List<Printer> nama = semuaPrinter();
            Printer printer = nama.get(0);
            return printer.getAddress_printer();
        }else{
            return "";
        }
    }
}
