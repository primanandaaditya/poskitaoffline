package com.kitadigi.poskita.activities.massal.kategori;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

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

        final ViewHolder viewHolder;
        convertView = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.im_kategori, null);

            viewHolder.etNamaKategori = (EditText) convertView
                    .findViewById(R.id.et_nama_kategori);
            viewHolder.etKodeKategori = (EditText) convertView
                    .findViewById(R.id.et_kode_kategori);

            viewHolder.etNamaKategori.setTag(position);
//            viewHolder.etKodeKategori.setTag(position);

            viewHolder.etNamaKategori.setText(datumList.get(position).getName());
//            viewHolder.etKodeKategori.setText(datumList.get(position).getCode_category());


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int tag_position_nama=(Integer) viewHolder.etNamaKategori.getTag();
//        int tag_position_kode = (Integer) viewHolder.etKodeKategori.getTag();

        viewHolder.etNamaKategori.setId(tag_position_nama);
//        viewHolder.etKodeKategori.setId(tag_position_kode);

//        if (inflater == null)
//            inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.im_kategori, null);
//
//
//        //find id
//        final EditText etNamaKategori= (EditText) convertView.findViewById(R.id.et_nama_kategori);
//        final EditText etKodeKategori = (EditText) convertView.findViewById(R.id.et_kode_kategori);
        TextView tvNomor = (TextView) convertView.findViewById(R.id.tv_nomor);



        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //setting font
//        etNamaKategori.setTypeface(fonts);
//        etKodeKategori.setTypeface(fonts);

        //set tag
//        etNamaKategori.setTag(position);
//        etKodeKategori.setTag(position);



        // getting movie data for the row


        //tampilkan nilai
//        etNamaKategori.setText(datum.getName());
//        etKodeKategori.setText(datum.getCode_category());

        //tampilkana nomor urut
        tvNomor.setText(String.valueOf(position+1));
        tvNomor.setTypeface(fonts);


        viewHolder.etNamaKategori.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                final int position2 = viewHolder.etNamaKategori.getId();
                final EditText Caption = (EditText) viewHolder.etNamaKategori;
                if(Caption.getText().toString().length()>0){
//                    datumList.set(position2,Integer.parseInt(Caption.getText().toString()));
                   Datum datum = datumList.get(position2);
                    datum.setName(Caption.getText().toString());
                }else{
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        //tambahkan event untuk listen perubahan
        //semua perubahan akan dikirim di var di index masing-masing
//        etNamaKategori.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (etNamaKategori.hasFocus()){
//                    datum.setName(s.toString());
//                    Log.d(  " nama kategori" + String.valueOf(position), s.toString());
//                }
////
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        etNamaKategori.setText(datum.getName());
//


        //tambahkan event untuk listen perubahan
        //semua perubahan akan dikirim di var di index masing-masing
//        etKodeKategori.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (etKodeKategori.hasFocus()){
//                    datum.setCode_category(s.toString());
//                    Log.d("kode kategori" + String.valueOf(position), s.toString());
//
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        etKodeKategori.setText(datum.getCode_category());

        return convertView;
    }

    public JSONArray createJSONArray(){
        JSONArray hasil = new JSONArray();
        JSONObject jsonObject;

        for (Datum datum : datumList){

            if (datum.getName()==null || datum.getName() == ""){

            }else{
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



        }
        return  hasil;
    }
}

class ViewHolder {
    EditText etNamaKategori;
    EditText etKodeKategori;
}