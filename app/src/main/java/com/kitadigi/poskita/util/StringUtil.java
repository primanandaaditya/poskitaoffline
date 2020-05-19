package com.kitadigi.poskita.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StringUtil {




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
