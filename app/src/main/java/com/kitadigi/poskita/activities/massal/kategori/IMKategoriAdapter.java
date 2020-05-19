package com.kitadigi.poskita.activities.massal.kategori;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class IMKategoriAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Datum> datumList;

    public IMKategoriAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @Override
    public int getCount() {
        return datumList.size();
    }

    @Override
    public Object getItem(int position) {
        return datumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.im_kategori, null);

        //find id
        EditText etNamaKategori= (EditText) convertView.findViewById(R.id.et_nama_kategori);
        EditText etKodeKategori = (EditText) convertView.findViewById(R.id.et_kode_kategori);


        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //setting font
        etNamaKategori.setTypeface(fonts);
        etKodeKategori.setTypeface(fonts);

        // getting movie data for the row
        final Datum datum = datumList.get(position);

        //tampilkan nilai
        etNamaKategori.setText(datum.getName());
        etKodeKategori.setText(datum.getCode_category());

        //tambahkan event untuk listen perubahan
        //semua perubahan akan dikirim di var di index masing-masing
        etNamaKategori.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                datum.setName(s.toString());
                Log.d(  " nama kategori" + String.valueOf(position), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //tambahkan event untuk listen perubahan
        //semua perubahan akan dikirim di var di index masing-masing
        etKodeKategori.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                datum.setCode_category(s.toString());
                Log.d("kode kategori" + String.valueOf(position), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    public JSONArray createJSONArray(){
        JSONArray hasil = new JSONArray();
        JSONObject jsonObject;

        for (Datum datum : datumList){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("nama", datum.getName());
                jsonObject.put("mobile_id", StringUtil.timeMilis());
                jsonObject.put("business_id","1");
                jsonObject.put("short_code",datum.getCode_category());
                jsonObject.put("parent_id","0");
                jsonObject.put("created_by","1");
                hasil.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return  hasil;
    }
}
