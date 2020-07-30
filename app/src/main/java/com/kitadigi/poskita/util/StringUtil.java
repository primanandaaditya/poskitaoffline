package com.kitadigi.poskita.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kitadigi.poskita.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public static String createFileFromDrawable(Context context) {

        String hasil = "";

        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_printer);
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            final byte[] bitmapdata = stream.toByteArray();

            hasil = Constants.ICON_STOCK;

        }

        return hasil;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    public static void fileFromAsset(Context context) {

        File f = new File(context.getCacheDir(), Constants.ICON_STOCK);

        try {

            f.createNewFile();


//Convert bitmap to byte array
            Bitmap bitmap = getBitmapFromAsset(context, Constants.ICON_STOCK_PATH);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


        }catch (Exception e){

        }


    }


    public static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is = null;
        try {
            AssetManager manager = context.getAssets();
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getFileNameFromURL(String fileName){

        if (fileName == null || fileName.matches("")){

            return "";
        }else{

            // full file name
            String[] parts = fileName.split("/"); // String array, each element is text between dots

            int jml = parts.length;

            String hasil = parts[jml-1].toString();
            return  hasil;
        }

    }

    //fungsi ini untuk membuat qrcode
    //lalu menampilkannya pada ImageView
    public static void generateQrCode(String teks, ImageView imageView){

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(teks, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    // fungsi untuk generate string random berdasarkan jumlah n
    public static String getRandomString(int n) {

        //pilih random karakter dari string ini
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        //buat stringbuffer
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate nomor acak antara 0 sampai n
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            //tambahkan karakter satupersatu dalam akhir
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    public static String timeMilis(){
        return String.valueOf(System.currentTimeMillis());
    }

    public static String leadingZero(String s){
        String hasil = "";

        //buat angka 1juta
        Integer red = 1000000;

        //convert parameter menjadi integer
        Integer ubah = Integer.parseInt(s);

        //tambahkan parameter dengan 1juta
        Integer total = red + ubah;

        //potong angka 1 di paling depan
        hasil = total.toString().substring(1);

        return hasil;
    }

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static String tanggalSekarang(){

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String hasil = formatter.format(date);

        return hasil;
    }

    public static String tanggalDariDatePicker(DatePicker datePicker){

        Integer tahun = datePicker.getYear();
        Integer bulan = datePicker.getMonth();
        Integer tanggal = datePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(tahun,bulan,tanggal);

        String hasil = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return hasil;
    }

    public static String tanggalIndonesia(String tanggal){

         SimpleDateFormat outSDF = new SimpleDateFormat("dd-MM-yyyy");
         SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String outDate = "";
        if (tanggal != null) {
            try {
                Date date = inSDF.parse(tanggal);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
            }
        }
        return outDate;


    }

    public static String formatRupiah(Integer angka){
        String hasil;

        DecimalFormat formatters = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatters.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatters.setDecimalFormatSymbols(symbols);

        hasil = formatters.format(angka);

        return  hasil;
    }

    public static String StringArrayToArray(List<String> stringList){
        String hasil="";

        for(String string:stringList){
            hasil = hasil + ","  + string;
        }
        hasil = hasil.substring(1);
        return hasil;
    }

    public static class NumberTextWatcher implements TextWatcher {

        private DecimalFormat df;
        private DecimalFormat dfnd;
        private boolean hasFractionalPart;

        private EditText et;

        public NumberTextWatcher(EditText et)
        {
            df = new DecimalFormat("#,###.##");
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,###");
            this.et = et;
            hasFractionalPart = false;
        }

        @SuppressWarnings("unused")
        private static final String TAG = "ReportActivity";

        @Override
        public void afterTextChanged(Editable s)
        {
            et.removeTextChangedListener(this);
            try {
                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    et.setText(df.format(n));
                } else {
                    et.setText(dfnd.format(n));
                }
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (ParseException e) {
                // do nothing?
            }

            et.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
            {
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }

    }


}
