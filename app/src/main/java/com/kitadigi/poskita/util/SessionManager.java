package com.kitadigi.poskita.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kitadigi.poskita.activities.login.LoginActivity;
import com.kitadigi.poskita.model.BeliModel;
import com.kitadigi.poskita.model.JualModel;
import com.kitadigi.poskita.model.ListBeliModel;
import com.kitadigi.poskita.model.ListJualModel;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.SubTotalModel;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.google.gson.Gson;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String KEY_AUTH_TOKEN = "auth_token";
    //untuk nyimpan interval sinkron
    public static final String KEY_INTERVAL_SINKRON = "interval_sinkron";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Bussiness id (make variable public to access from outside)
    public static final String KEY_BUSSINESS_ID = "bussiness_id";

    // User id (make variable public to access from outside)
    public static final String KEY_ID_USERS = "id_users";

    //untuk nyimpan nama printer bluetooth
    public static final String KEY_PRINTER_BLUETOOTH_NAME = "nama_printer";

    //nama jual model
    public static final String KEY_JUAL_MODEL = "JualModel";

    //nama untuk last sync
    public static final String KEY_LAST_SYNC = "last_sync";

    //nama beli model
    public static final String KEY_BELI_MODEL = "BeliModel";

    // User id encrypted (make variable public to access from outside)
    public static final String KEY_ID_USERS_ENCRYPTED = "id_users_encrypted";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    public void createLasySync(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        editor.putString(KEY_LAST_SYNC,formatter.format(date));
        editor.commit();
    }

    public void createAuthToken(String auth_token){
        editor.putString(KEY_AUTH_TOKEN, auth_token);
        editor.commit();
    }

    public void createIdUsers(String id_users){
        editor.putString(KEY_ID_USERS, id_users);
        editor.commit();
    }

    public void createBussinessId(String bussiness_id){
        editor.putString(KEY_BUSSINESS_ID, bussiness_id);
        editor.commit();
    }

    public void createIntervalSinkron(long interval_sinkron){
        editor.putLong(KEY_INTERVAL_SINKRON, interval_sinkron);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user bussiness id
        user.put(KEY_BUSSINESS_ID, pref.getString(KEY_BUSSINESS_ID,""));

        // return user
        return user;
    }

    public String getLastSync(){
        String hasil = pref.getString(KEY_LAST_SYNC, "");
        return hasil;
    }

    public String getAuthToken(){
        String hasil = pref.getString(KEY_AUTH_TOKEN,"");
        return hasil;
    }

    public String getIdUsers(){
        String hasil = pref.getString(KEY_ID_USERS, "");
        return hasil;
    }

    public String getBussinessId(){
        String hasil = pref.getString(KEY_BUSSINESS_ID, "");
        return hasil;
    }

    public String getLeadingZeroBussinessId(){
        String hasil = pref.getString(KEY_BUSSINESS_ID, "");
        return StringUtil.leadingZero(hasil);
    }

    public Long getIntervalSinkron(){
        Long hasil = pref.getLong(KEY_INTERVAL_SINKRON, 3600000);
        return hasil;
    }

    //untuk meyimpan nama printer
    public void setPrinterName(String name){

        // Storing email in pref
        editor.putString(KEY_PRINTER_BLUETOOTH_NAME, name);

        // commit changes
        editor.commit();
    }

    //untuk get nama printer bluetooth
    public String getPrinterName(){
        String hasil = pref.getString(KEY_PRINTER_BLUETOOTH_NAME,"");
        return hasil;
    }

    public String getEncryptedIdUsers(){
        SimpleMD5 simpleMD5=new SimpleMD5();
        String hasil = pref.getString(KEY_ID_USERS, "");
        return simpleMD5.generate(hasil);
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            ((Activity) _context).finish();
        }

    }

    public void logoutUser(){


        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



    //======================================================================
    //FUNGSI DI BAWAH UNTUK PENJUALAN OFFLINE
    //======================================================================

    //simpan penjualan offline
    public void simpanPenjualanOfflineBaru(ListJualModel listJualModel){
        Gson gson = new Gson();
        String json = gson.toJson(listJualModel);
        editor.putString(KEY_JUAL_MODEL,json);
        editor.commit();
    }

    public void tambahItemPenjualanOffline(JualModel jualModel){

        if (cekPenjualanOffline()==true){
//            Toast.makeText(_context,"ISI",Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String json = pref.getString(KEY_JUAL_MODEL, "");
            ListJualModel listJualModel = gson.fromJson(json, ListJualModel.class);

            List<JualModel> jualModels = listJualModel.getJualModels();
            jualModels.add(jualModel);

            simpanPenjualanOfflineBaru(listJualModel);
        }else{
//            Toast.makeText(_context,"Kosong",Toast.LENGTH_SHORT).show();
            List<JualModel> jualModels = new ArrayList<>();
            jualModels.add(jualModel);

            ListJualModel listJualModel=new ListJualModel(jualModels);
            simpanPenjualanOfflineBaru(listJualModel);
        }


    }

    //fungsi ini untuk mengecek apakah ada penjualan offline atau tidak
    public boolean cekPenjualanOffline(){
        if (pref.getString(KEY_JUAL_MODEL,"").matches("")){
            return false;
        }else{
            return true;
        }
    }

    public int cekJumlahPenjualanOffline(){

        if (cekPenjualanOffline()==false){
            return 0;
        }else{
            //jika ada penjualan
            ListJualModel listJualModel=getPenjualanOffline();
            return listJualModel.getJualModels().size();
        }

    }

    //fungsi ini untuk mendapatkan total qty dan subtotal harga
    //dari penjualan offline
    public SubTotalModel sumTotalPenjualanOffline(){

        if (cekPenjualanOffline()==false){
            return new SubTotalModel(0,0) ;
        }else{
            //jika ada penjualan

            //variabel untuk sum
            int counter_qty = 0;
            int counter_total = 0;

            //jumlah penjualan
            int qty = 0;

            //harga jual produk
            int harga_jual = 0;

            //qty dikalikan harga jual
            int subtotal = 0;

            ListJualModel listJualModel=getPenjualanOffline();
            List<JualModel> jualModels = listJualModel.getJualModels();
            for (JualModel jualModel: jualModels){
                qty=jualModel.getQty();
                harga_jual=jualModel.getSell_price();
                subtotal = qty * harga_jual;
                counter_total = counter_total + subtotal;
                counter_qty=counter_qty + qty;
            }
            return  new SubTotalModel(counter_qty,counter_total);
        }
    }

    //fungsi ini untuk mendapatkan daftar item yang dijual di layar pos
    //dalam class/object ListJualModel
    public ListJualModel getPenjualanOffline(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_JUAL_MODEL, "");
        ListJualModel listJualModel = gson.fromJson(json, ListJualModel.class);
        return listJualModel;
    }

    //fungsi ini untuk menghapus temporary penjualan
    //jadi nantinya keranjangnya dikosongkan
    public void clearPenjualanOffline(){
        editor.putString(KEY_JUAL_MODEL, "");
        editor.commit();
    }


    //fungsi ini untuk mengupdate item penjualan menjadi grouping
    public void gantiPenjualanOffline(List<TransactionsDetail> transactionsDetails){
        clearPenjualanOffline();

        ListJualModel listJualModel=new ListJualModel();
        List<JualModel> jualModels = new ArrayList<>();
        JualModel jualModel;
        for(TransactionsDetail transactionsDetail: transactionsDetails){
            jualModel=new JualModel(
                    transactionsDetail.getIdItems(),
                    transactionsDetail.getItemsName(),
                    Integer.parseInt(transactionsDetail.getItemsPrice()),
                    0,
                    Integer.parseInt(transactionsDetail.getQty())
                    );
            jualModels.add(jualModel);
        }
        listJualModel.setJualModels(jualModels);
        simpanPenjualanOfflineBaru(listJualModel);
    }

    //fungsi ini untuk menampilkan item yang sudah digrouping
    //dibentuk dari item2 yang belum digrouping
    public ListTransactionDetail penjualanOfflineToTransactionDetail(){

        ListTransactionDetail listTransactionDetail=new ListTransactionDetail();
        TransactionsDetail transactionsDetail;
        List<TransactionsDetail> list = new ArrayList<>();
        listTransactionDetail.setTransactionsDetails(list);

        if (cekPenjualanOffline()==true){
            ListJualModel listJualModel = getPenjualanOffline();
            for (JualModel jualModel:listJualModel.getJualModels()){
                transactionsDetail=new TransactionsDetail();
                transactionsDetail.setIdItems(jualModel.getId());
                transactionsDetail.setItemsName(jualModel.getName_product());
                transactionsDetail.setQty(jualModel.getQty().toString());
                transactionsDetail.setItemsPrice(jualModel.getSell_price().toString());
                listTransactionDetail.getTransactionsDetails().add(transactionsDetail);
            }
        }
        return listTransactionDetail;
    }

    //fungsi ini untuk mendapatkan jumlah suatu item
    //yang sudah diinputkan penjual saat itu
    //contoh : di layar POS, penjual sudah input IndomieGoreng sebanyak 2
    //maka angka 2 akan di-return oleh fungsi ini
    public int jumlahItemYangDiinputKePenjualanOffline(String id){
        int hasil =0;

        //cek dulu apakah ada penjualan offline saat ini
        //jika tidak, langsung return 0
        if (cekPenjualanOffline()==true){
            hasil=0;
            ListJualModel listJualModel = getPenjualanOffline();

            //looping untuk menghitung
            //jumlah item yang sudah diinput
            for (JualModel jualModel: listJualModel.getJualModels()){
                if (jualModel.getId().matches(id)){
                    hasil = hasil + jualModel.getQty();
                }
            }

        }else{
            hasil=0;
        }

        return hasil;
    }

    //======================================================================
    //FUNGSI PENJUALAN OFFLINE SELESAI
    //======================================================================








    //======================================================================
    //FUNGSI DI BAWAH UNTUK PEMBELIAN OFFLINE
    //======================================================================

    //simpan pembelian offline
    public void simpanPembelianOfflineBaru(ListBeliModel listBeliModel){
        Gson gson = new Gson();
        String json = gson.toJson(listBeliModel);
        editor.putString(KEY_BELI_MODEL,json);
        editor.commit();
    }


    //fungsi ini untuk mengecek apakah ada pembelian offline atau tidak
    public boolean cekPembelianOffline(){
        if (pref.getString(KEY_BELI_MODEL,"").matches("")){
            return false;
        }else{
            return true;
        }
    }

    public void tambahItemPembelianOffline(BeliModel beliModel){

        if (cekPembelianOffline()==true){
//            Toast.makeText(_context,"ISI",Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String json = pref.getString(KEY_BELI_MODEL, "");
            ListBeliModel listBeliModel = gson.fromJson(json, ListBeliModel.class);

            List<BeliModel> beliModels = listBeliModel.getBeliModels();
            beliModels.add(beliModel);

            simpanPembelianOfflineBaru(listBeliModel);
        }else{
//            Toast.makeText(_context,"Kosong",Toast.LENGTH_SHORT).show();
            List<BeliModel> beliModels = new ArrayList<>();
            beliModels.add(beliModel);

            ListBeliModel listBeliModel=new ListBeliModel(beliModels);
            simpanPembelianOfflineBaru(listBeliModel);
        }


    }

    public ListBeliModel getPembelianOffline(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_BELI_MODEL, "");
        ListBeliModel listBeliModel = gson.fromJson(json, ListBeliModel.class);
        return listBeliModel;
    }

    public SubTotalModel sumTotalPembelianOffline(){

        if (cekPembelianOffline()==false){
            return new SubTotalModel(0,0) ;
        }else{
            //jika ada penjualan

            //variabel untuk sum
            int counter_qty = 0;
            int counter_total = 0;

            //jumlah pembelian
            int qty = 0;

            //harga beli produk
            int harga_beli = 0;

            //qty dikalikan harga beli
            int subtotal = 0;

            ListBeliModel listBeliModel=getPembelianOffline();
            List<BeliModel> beliModels = listBeliModel.getBeliModels();
            for (BeliModel beliModel:beliModels){
                qty=beliModel.getQty();
                harga_beli=beliModel.getPurchase_price();
                subtotal = qty * harga_beli;
                counter_total = counter_total + subtotal;
                counter_qty=counter_qty + qty;
            }
            return  new SubTotalModel(counter_qty,counter_total);
        }
    }

    public void clearPembelianOffline(){
        editor.putString(KEY_BELI_MODEL, "");
        editor.commit();
    }


    //fungsi ini untuk mengupdate item pembelian menjadi grouping
    public void gantiPembelianOffline(List<TransactionsDetail> transactionsDetails){
        clearPembelianOffline();

        ListBeliModel listBeliModel = new ListBeliModel();
        List<BeliModel> beliModels = new ArrayList<>();
        BeliModel beliModel;

        for(TransactionsDetail transactionsDetail: transactionsDetails){
            beliModel=new BeliModel(
                    transactionsDetail.getIdItems(),
                    transactionsDetail.getItemsName(),
                    Integer.parseInt(transactionsDetail.getItemsPrice()),
                    0,
                    Integer.parseInt(transactionsDetail.getQty())
            );
            beliModels.add(beliModel);
        }
        listBeliModel.setBeliModels(beliModels);
        simpanPembelianOfflineBaru(listBeliModel);
    }

    public ListTransactionDetail pembelianOfflineToTransactionDetail(){

        ListTransactionDetail listTransactionDetail=new ListTransactionDetail();
        TransactionsDetail transactionsDetail;
        List<TransactionsDetail> list = new ArrayList<>();
        listTransactionDetail.setTransactionsDetails(list);

        if (cekPembelianOffline()==true){
            ListBeliModel listBeliModel = getPembelianOffline();
            for (BeliModel beliModel:listBeliModel.getBeliModels()){
                transactionsDetail=new TransactionsDetail();
                transactionsDetail.setIdItems(beliModel.getId());
                transactionsDetail.setItemsName(beliModel.getName_product());
                transactionsDetail.setQty(beliModel.getQty().toString());
                transactionsDetail.setItemsPrice(beliModel.getPurchase_price().toString());
                listTransactionDetail.getTransactionsDetails().add(transactionsDetail);
            }
        }
        return listTransactionDetail;
    }

    public int jumlahItemYangDiinputKePembelianOffline(String id){
        int hasil =0;

        //cek dulu apakah ada pembelian offline saat ini
        //jika tidak, langsung return 0
        if (cekPembelianOffline()==true){
            hasil=0;
            ListBeliModel listBeliModel = getPembelianOffline();

            //looping untuk menghitung
            //jumlah item yang sudah diinput
            for (BeliModel beliModel: listBeliModel.getBeliModels()){
                if (beliModel.getId().matches(id)){
                    hasil = hasil + beliModel.getQty();
                }
            }

        }else{
            hasil=0;
        }

        return hasil;
    }



    //======================================================================
    //FUNGSI PEMBELIAN OFFLINE SELESAI
    //======================================================================



}