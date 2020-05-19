package com.kitadigi.poskita.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kitadigi.poskita.fragment.Payment;
import com.kitadigi.poskita.model.Address;
import com.kitadigi.poskita.model.Items;
import com.kitadigi.poskita.model.Transactions;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String TAG                     = Database.class.getSimpleName();
    private static final int DATABASE_VERSION           = 1;
    private static final String DATABASE_NAME           = "dbPos";

    //Tables name
    private static final String TABLE_ITEMS                 = "tbl_items";
    private static final String TABLE_ITEMS_TEMP            = "tbl_items_temp";
    private static final String TABLE_TRANSACTIONS          = "tbl_transactions";
    private static final String TABLE_TRANSACTIONS_DETAIL   = "tbl_transactions_detail";
    private static final String TABLE_TRANSACTIONS_PAYMENT  = "tbl_transactions_payment";
    private static final String TABLE_TRANSACTIONS_ADDRESS  = "tbl_transactions_address";
    private static final String TABLE_USER_BALANCE          = "tbl_users_balance";
    private static final String TABLE_USER_ADDRESS          = "tbl_users_address";

    //columns items
    private static final String ITEMS_ID                = "items_id";
    private static final String ITEMS_NAME              = "items_name";
    private static final String ITEMS_PRICE             = "items_price";
    private static final String ITEMS_PRICE_SELL        = "items_price_sell";
    private static final String ITEMS_IMAGE             = "items_image";
    private static final String ITEMS_DESCRIPTION       = "items_description";
    private static final String ITEMS_TIPE              = "items_tipe";
    private static final String ITEMS_SHORTCUT          = "items_shorcut";

    //columns items
    private static final String ITEMS_TEMP_ID                = "items_temp_id";
    private static final String ITEMS_TEMP_ID_ITEMS          = "items_temp_id_items";
    private static final String ITEMS_TEMP_NAME              = "items_temp_name";
    private static final String ITEMS_TEMP_PRICE             = "items_temp_price";
    private static final String ITEMS_TEMP_PRICE_SELL        = "items_temp_price_sell";
    private static final String ITEMS_TEMP_IMAGE             = "items_temp_image";
    private static final String ITEMS_TEMP_DESCRIPTION       = "items_temp_description";
    private static final String ITEMS_TEMP_TIPE              = "items_temp_item";
    private static final String ITEMS_TEMP_SHORTCUT          = "items_temp_shorcut";

    //columns transactions
    private static final String TRANSACTIONS_ID         = "transactions_id";
    private static final String TRANSACTIONS_NO         = "transactions_no";
    private static final String TRANSACTIONS_DATE       = "transactions_date";
    private static final String TRANSACTIONS_TOTAL      = "transactions_total";
    private static final String TRANSACTIONS_STATUS     = "transactions_status";
    private static final String TRANSACTIONS_TIPE       = "transactions_tipe";

    //columns transactions detail
    private static final String TRANSACTIONS_DETAIL_ID         = "transactions_detail_id";
    private static final String TRANSACTIONS_DETAIL_NO         = "transactions_detail_no";
    private static final String TRANSACTIONS_DETAIL_ITEMS      = "transactions_detail_items";
    private static final String TRANSACTIONS_DETAIL_ITEMS_NAME = "transactions_detail_items_name";
    private static final String TRANSACTIONS_DETAIL_QTY        = "transactions_detail_qty";
    private static final String TRANSACTIONS_DETAIL_PRICE      = "transactions_detail_price";
    private static final String TRANSACTIONS_DETAIL_SUBTOTAL   = "transactions_detail_subtotal";
    private static final String TRANSACTIONS_DETAIL_TIPE       = "transactions_detail_tipe";

    //columns transaction address
    private static final String TRANSACTIONS_ADDRESS_ID         = "transactions_address_id";
    private static final String TRANSACTIONS_ADDRESS_NO         = "transactions_address_no";
    private static final String TRANSACTIONS_ADDRESS_RECIPIENT  = "transactions_address_recipient";
    private static final String TRANSACTIONS_ADDRESS            = "transactions_address";
    private static final String TRANSACTIONS_ADDRESS_PHONE      = "transactions_address_phone";

    //columns transactions
    private static final String TRANSACTIONS_PAYMENT_ID         = "transactions_payment_id";
    private static final String TRANSACTIONS_PAYMENT_NO         = "transactions_payment_no";
    private static final String TRANSACTIONS_PAYMENT_METHOD     = "transactions_payment_method";
    private static final String TRANSACTIONS_PAYMENT_TOTAL      = "transactions_payment_total";
    private static final String TRANSACTIONS_PAYMENT_AMOUNT     = "transactions_payment_amount";
    private static final String TRANSACTIONS_PAYMENT_CHANGE     = "transactions_payment_change";

    //columns balance
    private static final String USER_BALANCE_ID                 = "user_balance_id";
    private static final String USER_BALANCE_DATE               = "user_balance_date";
    private static final String USER_BALANCE_                   = "users_balance";
    private static final String USER_BALANCE_OUT                = "users_balance_out";
    private static final String USER_BALANCE_IN                 = "users_balance_in";

    //columns address
    private static final String USER_ADDRESS_ID                 = "user_address_id";
    private static final String USER_ADDRESS_NAME               = "user_address_name";
    private static final String USER_ADDRESS                    = "user_address";
    private static final String USER_ADDRESS_PHONE              = "user_address_phone";

    //table items init
    private static final String CREATE_TABLE_ITEMS      = "CREATE TABLE "
            + TABLE_ITEMS + " (" + ITEMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ITEMS_NAME + " TEXT," + ITEMS_PRICE + " TEXT," + ITEMS_PRICE_SELL + " TEXT,"
            + ITEMS_IMAGE + " TEXT," + ITEMS_DESCRIPTION + " TEXT,"
            + ITEMS_TIPE + " TEXT," + ITEMS_SHORTCUT + " TEXT)";

    //table items init
    private static final String CREATE_TABLE_ITEMS_TEMP      = "CREATE TABLE "
            + TABLE_ITEMS_TEMP + " (" + ITEMS_TEMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ITEMS_TEMP_ID_ITEMS + " TEXT," + ITEMS_TEMP_NAME + " TEXT," + ITEMS_TEMP_PRICE + " TEXT," + ITEMS_TEMP_PRICE_SELL + " TEXT,"
            + ITEMS_TEMP_IMAGE + " TEXT," + ITEMS_TEMP_DESCRIPTION + " TEXT,"
            + ITEMS_TEMP_TIPE + " TEXT," + ITEMS_TEMP_SHORTCUT + " TEXT)";

    //table transactions init
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "
            + TABLE_TRANSACTIONS + " (" + TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTIONS_NO + " TEXT," + TRANSACTIONS_DATE + " TEXT,"
            + TRANSACTIONS_TOTAL + " TEXT, " + TRANSACTIONS_STATUS + " TEXT, " + TRANSACTIONS_TIPE + " TEXT)";

    //table transactions detail init
    private static final String CREATE_TABLE_TRANSACTIONS_DETAIL = "CREATE TABLE "
            + TABLE_TRANSACTIONS_DETAIL + " (" + TRANSACTIONS_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTIONS_DETAIL_NO + " TEXT," + TRANSACTIONS_DETAIL_ITEMS + " TEXT,"
            + TRANSACTIONS_DETAIL_ITEMS_NAME + " TEXT, " + TRANSACTIONS_DETAIL_QTY + " TEXT, "
            + TRANSACTIONS_DETAIL_PRICE + " TEXT, " + TRANSACTIONS_DETAIL_SUBTOTAL + " TEXT, "
            + TRANSACTIONS_DETAIL_TIPE + " TEXT)";

    //table transactions detail init
    private static final String CREATE_TABLE_TRANSACTIONS_PAYMENT = "CREATE TABLE "
            + TABLE_TRANSACTIONS_PAYMENT + " (" + TRANSACTIONS_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTIONS_PAYMENT_NO + " TEXT," + TRANSACTIONS_PAYMENT_METHOD + " TEXT,"
            + TRANSACTIONS_PAYMENT_TOTAL + " TEXT, " + TRANSACTIONS_PAYMENT_AMOUNT + " TEXT, "
            + TRANSACTIONS_PAYMENT_CHANGE + " TEXT)";

    //table transactions address init
    private static final String CREATE_TABLE_TRANSACTIONS_ADDRESS = "CREATE TABLE "
            + TABLE_TRANSACTIONS_ADDRESS + " (" + TRANSACTIONS_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTIONS_ADDRESS_NO + " TEXT," + TRANSACTIONS_ADDRESS_RECIPIENT + " TEXT,"
            + TRANSACTIONS_ADDRESS + " TEXT, " + TRANSACTIONS_ADDRESS_PHONE + " TEXT)";

    //table balance init
    private static final String CREATE_TABLE_USER_BALANCE      = "CREATE TABLE "
            + TABLE_USER_BALANCE + " (" + USER_BALANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_BALANCE_DATE + " TEXT," + USER_BALANCE_ + " TEXT,"
            + USER_BALANCE_OUT + " TEXT," + USER_BALANCE_IN + " TEXT)";

    //table items init
    private static final String CREATE_TABLE_USER_ADDRESS      = "CREATE TABLE "
            + TABLE_USER_ADDRESS + " (" + USER_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_ADDRESS_NAME + " TEXT," + USER_ADDRESS + " TEXT,"
            + USER_ADDRESS_PHONE + " TEXT)";


    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_ITEMS_TEMP);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_TRANSACTIONS_DETAIL);
        db.execSQL(CREATE_TABLE_TRANSACTIONS_PAYMENT);
        db.execSQL(CREATE_TABLE_TRANSACTIONS_ADDRESS);
        db.execSQL(CREATE_TABLE_USER_BALANCE);
        db.execSQL(CREATE_TABLE_USER_ADDRESS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS_TEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ADDRESS);

        onCreate(db);
    }

    //Add List Items
    public void addListItems(String itemsName, String itemsPrice, String itemsPriceSell, String itemsImage, String itemsDescription, String tipe, String shortcut){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMS_NAME, itemsName);
        values.put(ITEMS_PRICE, itemsPrice);
        values.put(ITEMS_PRICE_SELL, itemsPriceSell);
        values.put(ITEMS_IMAGE, itemsImage);
        values.put(ITEMS_DESCRIPTION, itemsDescription);
        values.put(ITEMS_TIPE, tipe);
        values.put(ITEMS_SHORTCUT, shortcut);

        long id = db.insert(TABLE_ITEMS, null, values);
        db.close();
        Log.d(TAG, "New items inserted into sqlite: " + id);
    }

    public int updateItems(String idItems, String itemsName, String itemsPrice, String itemsPriceSell, String itemsImage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMS_NAME, itemsName);
        values.put(ITEMS_PRICE, itemsPrice);
        values.put(ITEMS_PRICE_SELL, itemsPriceSell);
        values.put(ITEMS_IMAGE, itemsImage);
        values.put(ITEMS_DESCRIPTION, itemsName);
        Log.e(TAG, "Update items from sqlite ");
        // updating row
        return db.update(TABLE_ITEMS, values, ITEMS_ID + " = ? ",
                new String[] {idItems});
    }

    public int updateItemsTemp(String idItems, String itemsName, String itemsPrice, String itemsPriceSell, String itemsImage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMS_TEMP_NAME, itemsName);
        values.put(ITEMS_TEMP_PRICE, itemsPrice);
        values.put(ITEMS_TEMP_PRICE_SELL, itemsPriceSell);
        values.put(ITEMS_TEMP_IMAGE, itemsImage);
        values.put(ITEMS_TEMP_DESCRIPTION, itemsName);
        Log.e(TAG, "Update items temp from sqlite ");
        // updating row
        return db.update(TABLE_ITEMS_TEMP, values, ITEMS_TEMP_ID_ITEMS + " = ? ",
                new String[] {idItems});
    }

    public int getCountListItems(){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_ITEMS;
            c = db.rawQuery(query, null);
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
        db.close();
        Log.d(TAG, "Deleted items from sqlite");
    }

    public void deleteItemsById(String idItems){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, ITEMS_ID + " = ?", new String[]{idItems});
        db.close();
        Log.d(TAG, "Deleted items from sqlite");
    }

    public void deleteItemsTempsById(String idItems){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS_TEMP, ITEMS_TEMP_ID_ITEMS + " = ?", new String[]{idItems});
        db.close();
        Log.d(TAG, "Deleted items temp from sqlite");
    }

    public String getListItems() {
        List<Items> itemsList = new ArrayList<Items>();
        String query = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();
                items.setIdItems(cursor.getString(0));
                items.setItemsName(cursor.getString(1));
                items.setItemsPrice(cursor.getString(2));
                items.setItemsPriceSell(cursor.getString(3));
                items.setItemsImage(cursor.getString(4));
                items.setItemsDescription(cursor.getString(5));
                items.setTipe(cursor.getString(6));
                items.setShortcut(cursor.getString(7));
                itemsList.add(items);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(itemsList);
    }

    //Add List Items
    public void addListItemsTemp(String idItems, String itemsName, String itemsPrice, String itemsPriceSell, String itemsImage, String itemsDescription, String tipe, String shortcut){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMS_TEMP_ID_ITEMS, idItems);
        values.put(ITEMS_TEMP_NAME, itemsName);
        values.put(ITEMS_TEMP_PRICE, itemsPrice);
        values.put(ITEMS_TEMP_PRICE_SELL, itemsPriceSell);
        values.put(ITEMS_TEMP_IMAGE, itemsImage);
        values.put(ITEMS_TEMP_DESCRIPTION, itemsDescription);
        values.put(ITEMS_TEMP_TIPE, tipe);
        values.put(ITEMS_TEMP_SHORTCUT, shortcut);

        long id = db.insert(TABLE_ITEMS_TEMP, null, values);
        db.close();
        Log.d(TAG, "New items temp inserted into sqlite: " + id);
    }

    public int getCountListItemsTemp(){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_ITEMS_TEMP;
            c = db.rawQuery(query, null);
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public int getCountListItemsTempTipe(String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_ITEMS_TEMP + " WHERE " + ITEMS_TEMP_TIPE + " = ?";
            c = db.rawQuery(query, new String[]{tipe});
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public int getCountListItemsTemp(String idItems, String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_ITEMS_TEMP + " WHERE " + ITEMS_TEMP_NAME + " = ? AND " + ITEMS_TEMP_TIPE + " = ?";
            c = db.rawQuery(query, new String[]{idItems, tipe});
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteItemsTemp(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS_TEMP, null, null);
        db.close();
        Log.d(TAG, "Deleted items temp from sqlite");
    }

    public void deleteItemsTempById(String idItems){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS_TEMP, ITEMS_TEMP_ID_ITEMS + " = ?", new String[]{idItems});
        db.close();
        Log.d(TAG, "Deleted items");
    }

    public String getListItemsTemp(String tipe) {
        List<Items> itemsList = new ArrayList<Items>();
        String query = "SELECT * FROM " + TABLE_ITEMS_TEMP + " WHERE " + ITEMS_TEMP_TIPE + " <> ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{tipe});
        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();
                items.setIdItems(cursor.getString(1));
                items.setItemsName(cursor.getString(2));
                items.setItemsPrice(cursor.getString(3));
                items.setItemsPriceSell(cursor.getString(4));
                items.setItemsImage(cursor.getString(5));
                items.setItemsDescription(cursor.getString(6));
                items.setTipe(cursor.getString(7));
                items.setShortcut(cursor.getString(8));
                itemsList.add(items);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(itemsList);
    }

    public String getListItemsTemp(String tipe, String tipe2) {
        List<Items> itemsList = new ArrayList<Items>();
        String query = "SELECT * FROM " + TABLE_ITEMS_TEMP + " WHERE " + ITEMS_TEMP_TIPE + " <> ? AND " + ITEMS_TEMP_TIPE + " <> ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{tipe, tipe2});
        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();
                items.setIdItems(cursor.getString(1));
                items.setItemsName(cursor.getString(2));
                items.setItemsPrice(cursor.getString(3));
                items.setItemsPriceSell(cursor.getString(4));
                items.setItemsImage(cursor.getString(5));
                items.setItemsDescription(cursor.getString(6));
                items.setTipe(cursor.getString(7));
                items.setShortcut(cursor.getString(8));
                itemsList.add(items);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(itemsList);
    }

    //Add List Transactions
    public void addTransactions(String no, String date, String total, String status, String tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_NO, no);
        values.put(TRANSACTIONS_DATE, date);
        values.put(TRANSACTIONS_TOTAL, total);
        values.put(TRANSACTIONS_STATUS, status);
        values.put(TRANSACTIONS_TIPE, tipe);

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        Log.d(TAG, "New transactions inserted into sqlite: " + id);
    }

    public void addTransactionsDetail(String no, String itemsId, String itemsName, String qty, String price, String subtotal, String tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_DETAIL_NO, no);
        values.put(TRANSACTIONS_DETAIL_ITEMS, itemsId);
        values.put(TRANSACTIONS_DETAIL_ITEMS_NAME, itemsName);
        values.put(TRANSACTIONS_DETAIL_QTY, qty);
        values.put(TRANSACTIONS_DETAIL_PRICE, price);
        values.put(TRANSACTIONS_DETAIL_SUBTOTAL, subtotal);
        values.put(TRANSACTIONS_DETAIL_TIPE, tipe);

        long id = db.insert(TABLE_TRANSACTIONS_DETAIL, null, values);
        db.close();
        Log.d(TAG, "New transactions detail inserted into sqlite: " + id);
    }

    public int updateTransactions(String no, String itemsId, String itemsName, String qty, String price, String subtotal, String tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_DETAIL_ITEMS_NAME, itemsName);
        values.put(TRANSACTIONS_DETAIL_QTY, qty);
        values.put(TRANSACTIONS_DETAIL_PRICE, price);
        values.put(TRANSACTIONS_DETAIL_SUBTOTAL, subtotal);
        Log.e(TAG, "Update transactions detail from sqlite ");
        // updating row
        return db.update(TABLE_TRANSACTIONS_DETAIL, values, TRANSACTIONS_DETAIL_NO + " = ? AND " + TRANSACTIONS_DETAIL_ITEMS + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?",
                new String[] {no, itemsId, tipe});
    }

    public int updateNoTransactionDetail(String no, String kodeTransaction, String tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_DETAIL_NO, kodeTransaction);
        Log.d(TAG, "Update kode transaction");

        return db.update(TABLE_TRANSACTIONS_DETAIL, values, TRANSACTIONS_DETAIL_NO + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?", new String[]{no, tipe});
    }

    public HashMap<String, String> getTransactionsNo(String today){
        HashMap<String, String> transactions = new HashMap<String, String>();
        String selectQuery = "SELECT MAX(" + TRANSACTIONS_NO + ") AS " + TRANSACTIONS_NO + " FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_DATE + " = ?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{today});

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            transactions.put("no", cursor.getString(0));
        }
        cursor.close();
        db.close();
        Log.e(TAG, "Fetching no from Sqlite: " + transactions.toString());
        return transactions;
    }

    public HashMap<String, String> getTransactionsItems(String no, String idItems, String tipe){
        HashMap<String, String> transactions = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = ? AND " + TRANSACTIONS_DETAIL_ITEMS + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{no, idItems, tipe});

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            transactions.put("qty", cursor.getString(4));
            transactions.put("subtotal", cursor.getString(6));
        }
        cursor.close();
        db.close();
        Log.e(TAG, "Fetching items from Sqlite: " + transactions.toString());
        return transactions;
    }


    public int getCountTransactions(){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_TRANSACTIONS;
            c = db.rawQuery(query, null);
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public int getCountTransactionsDetail(String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = 0 AND " + TRANSACTIONS_DETAIL_TIPE + " = ?";
            c = db.rawQuery(query, new String[]{tipe});
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
    public int getCountTransactionsDetailItems(String idItems, String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = 0 AND " + TRANSACTIONS_DETAIL_ITEMS + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?"
                    ;
            c = db.rawQuery(query, new String[]{idItems, tipe});
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public int getTotalQty(String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT SUM(" + TRANSACTIONS_DETAIL_QTY + ") AS " + TRANSACTIONS_DETAIL_QTY + " FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = 0 AND " + TRANSACTIONS_DETAIL_TIPE + " = ?";
            c = db.rawQuery(query, new String[]{tipe});
            if(c.moveToFirst())
                return c.getInt(0);
            else
                return -1;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public int getTotal(String tipe){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT SUM(" + TRANSACTIONS_DETAIL_SUBTOTAL + ") AS " + TRANSACTIONS_DETAIL_SUBTOTAL + " FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = 0 AND " + TRANSACTIONS_DETAIL_TIPE + " = ?";
            c = db.rawQuery(query, new String[]{tipe});
            if(c.moveToFirst())
                return c.getInt(0);
            else
                return -1;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }

    }

    public void deleteTransactionsItems(String no, String idItems, String tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS_DETAIL, TRANSACTIONS_DETAIL_NO + " = ? AND " + TRANSACTIONS_DETAIL_ITEMS + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?", new String[]{no, idItems, tipe});
        db.close();
        Log.d(TAG, "Deleted transactions items from sqlite");
    }

    public long fetchTransactionsItems(String tipe) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = 0 AND " + TRANSACTIONS_DETAIL_TIPE + " = " + tipe;
        SQLiteStatement statement = db.compileStatement(query);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public String getListTransactions(String no) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_NO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{no});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setNo(cursor.getString(1));
                transactions.setDate(cursor.getString(2));
                transactions.setTotal(cursor.getString(3));
                transactions.setStatus(cursor.getString(4));
                transactions.setTipe(cursor.getString(5));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getListPayment(String no) {
        List<Payment> payments = new ArrayList<Payment>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS_PAYMENT + " WHERE " + TRANSACTIONS_PAYMENT_NO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{no});
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setIdPayment(cursor.getString(0));
                payment.setNo(cursor.getString(1));
                payment.setMethod(cursor.getString(2));
                payment.setTotal(cursor.getString(3));
                payment.setAmount(cursor.getString(4));
                payment.setChange(cursor.getString(5));
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(payments);
    }

    public String getListAddress(String no) {
        List<Address> addresses = new ArrayList<Address>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS_ADDRESS + " WHERE " + TRANSACTIONS_ADDRESS_NO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{no});
        if (cursor.moveToFirst()) {
            do {
                Address address = new Address();
                address.setUserAddress(cursor.getString(2));
                address.setNameAddress(cursor.getString(3));
                address.setUsersPhone(cursor.getString(4));
                addresses.add(address);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(addresses);
    }

    public String getAllTransactions() {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setNo(cursor.getString(1));
                transactions.setDate(cursor.getString(2));
                transactions.setTotal(cursor.getString(3));
                transactions.setStatus(cursor.getString(4));
                transactions.setTipe(cursor.getString(5));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getAllTransactionsByDate() {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " GROUP BY " + TRANSACTIONS_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setDate(cursor.getString(2));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getAllTransactionsTipeByDate(String tipe) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_TIPE + " = ? GROUP BY " + TRANSACTIONS_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{tipe});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setDate(cursor.getString(2));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getAllTransactionsFilterByDate(String dateFirst, String dateEnd) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_DATE + " BETWEEN ? AND ?  GROUP BY " + TRANSACTIONS_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{dateFirst, dateEnd});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setDate(cursor.getString(2));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        Log.d(TAG, "result " + new Gson().toJson(transactionsList));
        return new Gson().toJson(transactionsList);
    }

    public String getAllTransactionsFilterByTipeDate(String tipe, String dateFirst, String dateEnd) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_TIPE + " = ? AND " + TRANSACTIONS_DATE + " BETWEEN ? AND ?  GROUP BY " + TRANSACTIONS_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{tipe, dateFirst, dateEnd});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setDate(cursor.getString(2));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        Log.d(TAG, "result " + new Gson().toJson(transactionsList));
        return new Gson().toJson(transactionsList);
    }

    public String getListTransactionsDetail(String no, String tipe) {
        List<TransactionsDetail> transactionsList = new ArrayList<TransactionsDetail>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = ? AND " + TRANSACTIONS_DETAIL_TIPE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{no, tipe});
        if (cursor.moveToFirst()) {
            do {
                TransactionsDetail transactions = new TransactionsDetail();
                transactions.setNo(cursor.getString(1));
                transactions.setIdItems(cursor.getString(2));
                transactions.setItemsName(cursor.getString(3));
                transactions.setQty(cursor.getString(4));
                transactions.setItemsPrice(cursor.getString(5));
                transactions.setSubTotal(cursor.getString(6));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getListTransactionsDetail(String no) {
        List<TransactionsDetail> transactionsList = new ArrayList<TransactionsDetail>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS_DETAIL + " WHERE " + TRANSACTIONS_DETAIL_NO + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{no});
        if (cursor.moveToFirst()) {
            do {
                TransactionsDetail transactions = new TransactionsDetail();
                transactions.setNo(cursor.getString(1));
                transactions.setIdItems(cursor.getString(2));
                transactions.setItemsName(cursor.getString(3));
                transactions.setQty(cursor.getString(4));
                transactions.setItemsPrice(cursor.getString(5));
                transactions.setSubTotal(cursor.getString(6));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getListTransactionsByDate(String date) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_DATE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setNo(cursor.getString(1));
                transactions.setDate(cursor.getString(2));
                transactions.setTotal(cursor.getString(3));
                transactions.setStatus(cursor.getString(4));
                transactions.setTipe(cursor.getString(5));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getListTransactionsByDate(String tipe, String date) {
        List<Transactions> transactionsList = new ArrayList<Transactions>();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTIONS_TIPE + " = ? AND " + TRANSACTIONS_DATE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{tipe, date});
        if (cursor.moveToFirst()) {
            do {
                Transactions transactions = new Transactions();
                transactions.setNo(cursor.getString(1));
                transactions.setDate(cursor.getString(2));
                transactions.setTotal(cursor.getString(3));
                transactions.setStatus(cursor.getString(4));
                transactions.setTipe(cursor.getString(5));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    public String getListTransactionsDetailByDate(String date) {
        List<TransactionsDetail> transactionsList = new ArrayList<TransactionsDetail>();
        String query = "SELECT TRANSACTIONS." + TRANSACTIONS_NO + " AS " + TRANSACTIONS_NO
                + ", DETAIL." + TRANSACTIONS_DETAIL_ITEMS + " AS " + TRANSACTIONS_DETAIL_ITEMS
                + ", DETAIL." + TRANSACTIONS_DETAIL_ITEMS_NAME + " AS " + TRANSACTIONS_DETAIL_ITEMS_NAME
                + ", DETAIL." + TRANSACTIONS_DETAIL_QTY + " AS " + TRANSACTIONS_DETAIL_QTY
                + ", DETAIL." + TRANSACTIONS_DETAIL_PRICE + " AS " + TRANSACTIONS_DETAIL_PRICE
                + ", DETAIL." + TRANSACTIONS_DETAIL_SUBTOTAL + " AS " + TRANSACTIONS_DETAIL_SUBTOTAL
                + " FROM " + TABLE_TRANSACTIONS + " AS TRANSACTIONS JOIN " + TABLE_TRANSACTIONS_DETAIL + " AS DETAIL ON "
                + " TRANSACTIONS." + TRANSACTIONS_NO + " = DETAIL." + TRANSACTIONS_DETAIL_NO
                + " WHERE TRANSACTIONS." + TRANSACTIONS_DATE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                TransactionsDetail transactions = new TransactionsDetail();
                transactions.setNo(cursor.getString(0));
                transactions.setIdItems(cursor.getString(1));
                transactions.setItemsName(cursor.getString(2));
                transactions.setQty(cursor.getString(3));
                transactions.setItemsPrice(cursor.getString(4));
                transactions.setSubTotal(cursor.getString(5));
                transactionsList.add(transactions);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(transactionsList);
    }

    //Add Payment
    public void addPayment(String no, String method, String total, String amount, String change){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_PAYMENT_NO, no);
        values.put(TRANSACTIONS_PAYMENT_METHOD, method);
        values.put(TRANSACTIONS_PAYMENT_TOTAL, total);
        values.put(TRANSACTIONS_PAYMENT_AMOUNT, amount);
        values.put(TRANSACTIONS_PAYMENT_CHANGE, change);

        long id = db.insert(TABLE_TRANSACTIONS_PAYMENT, null, values);
        db.close();
        Log.d(TAG, "New transactions payment inserted into sqlite: " + id);
    }

    //Add Payment
    public void addTransactionAddress(String no, String recipient, String address, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTIONS_ADDRESS_NO, no);
        values.put(TRANSACTIONS_ADDRESS_RECIPIENT, recipient);
        values.put(TRANSACTIONS_ADDRESS, address);
        values.put(TRANSACTIONS_ADDRESS_PHONE, phone);

        long id = db.insert(TABLE_TRANSACTIONS_ADDRESS, null, values);
        db.close();
        Log.d(TAG, "New transactions address inserted into sqlite: " + id);
    }

    //Add List Address
    public void addAdress(String nameAddress, String address, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ADDRESS_NAME, nameAddress);
        values.put(USER_ADDRESS, address);
        values.put(USER_ADDRESS_PHONE, phone);

        long id = db.insert(TABLE_USER_ADDRESS, null, values);
        db.close();
        Log.d(TAG, "New address inserted into sqlite: " + id);
    }

    public int updateAddress(String idAddress, String nameAddress, String address, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ADDRESS_NAME, nameAddress);
        values.put(USER_ADDRESS, address);
        values.put(USER_ADDRESS_PHONE, phone);

        Log.e(TAG, "Update address from sqlite ");
        // updating row
        return db.update(TABLE_USER_ADDRESS, values, USER_ADDRESS_ID + " = ? ",
                new String[] {idAddress});
    }

    public void deleteAddressById(String idAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_ADDRESS, USER_ADDRESS_ID + " = ?", new String[]{idAddress});
        db.close();
        Log.d(TAG, "Deleted address from sqlite");
    }

    public int getCountListAddress(){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_USER_ADDRESS;
            c = db.rawQuery(query, null);
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteAddress(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_ADDRESS, null, null);
        db.close();
        Log.d(TAG, "Deleted address from sqlite");
    }

    public String getListAddress() {
        List<Address> addressList = new ArrayList<Address>();
        String query = "SELECT * FROM " + TABLE_USER_ADDRESS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Address address = new Address();
                address.setIdAddress(cursor.getString(0));
                address.setUserAddress(cursor.getString(1));
                address.setNameAddress(cursor.getString(2));
                address.setUsersPhone(cursor.getString(3));
                addressList.add(address);
            } while (cursor.moveToNext());
        }
        db.close();
        return new Gson().toJson(addressList);
    }

    public void addBalance(String balance, String out, String in){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_BALANCE_, balance);
        values.put(USER_BALANCE_OUT, out);
        values.put(USER_BALANCE_IN, in);

        long id = db.insert(TABLE_USER_BALANCE, null, values);
        db.close();
        Log.d(TAG, "New balance inserted into sqlite: " + id);
    }

    public int getCountBalance(){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_USER_BALANCE;
            c = db.rawQuery(query, null);
            if(c.moveToFirst()){
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
    public int updateBalance(String id, String balance, String out){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_BALANCE_, balance);
        values.put(USER_BALANCE_OUT, out);
        Log.d(TAG, "Update balance");

        return db.update(TABLE_USER_BALANCE, values, USER_BALANCE_ID + " = ?", new String[]{id});
    }

    public HashMap<String, String> getBalance(){
        HashMap<String, String> balance = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_BALANCE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            balance.put("id", cursor.getString(0));
            balance.put("balance", cursor.getString(2));
        }
        cursor.close();
        db.close();
        Log.e(TAG, "Fetching balance from Sqlite: " + balance.toString());
        return balance;
    }
}
