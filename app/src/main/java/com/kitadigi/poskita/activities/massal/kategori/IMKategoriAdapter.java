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


            //find id dari control
            viewHolder.etNamaKategori = (EditText) convertView
                    .findViewById(R.id.et_nama_kategori);
            viewHolder.etKodeKategori = (EditText) convertView
                    .findViewById(R.id.et_kode_kategori);


            //pasang tag dengan index list dari model
            viewHolder.etNamaKategori.setTag(position);
            viewHolder.etKodeKategori.setTag(position);

            //set teks dari tiap model
            viewHolder.etNamaKategori.setText(datumList.get(position).getName());
            viewHolder.etKodeKategori.setText(datumList.get(position).getCode_category());

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //buat var integer
        int tag_position_nama=(Integer) viewHolder.etNamaKategori.getTag();
        int tag_position_kode = (Integer) viewHolder.etKodeKategori.getTag();

        viewHolder.etNamaKategori.setId(tag_position_nama);
        viewHolder.etKodeKategori.setId(tag_position_kode);


        //find id untuk teks nomor urut
        TextView tvNomor = (TextView) convertView.findViewById(R.id.tv_nomor);

        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //tampilkana nomor urut
        tvNomor.setText(String.valueOf(position+1) + ".");
        tvNomor.setTypeface(fonts);


        //saat edittext berubah, kirim juga perubahan ke model ybs
        //nama kategori
        viewHolder.etNamaKategori.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etNamaKategori.getId();

                //init id
                final EditText et_Nama_Kategori = (EditText) viewHolder.etNamaKategori;

                if(et_Nama_Kategori.getText().toString().length()>0){

                   Datum datum = datumList.get(position2);
                   datum.setName(et_Nama_Kategori.getText().toString());
                }else{
//                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
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


        //saat edittext berubah, kirim juga perubahan ke model ybs
        //kode kategori
        viewHolder.etKodeKategori.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etKodeKategori.getId();

                //init id
                final EditText et_Kode_Kategori = (EditText) viewHolder.etKodeKategori;

                if(et_Kode_Kategori.getText().toString().length()>0){

                    Datum datum = datumList.get(position2);
                    datum.setCode_category(et_Kode_Kategori.getText().toString());
                }else{
//                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
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

        return convertView;
    }

    public JSONArray createJSONArray(){
        JSONArray hasil = new JSONArray();
        JSONObject jsonObject;

        for (Datum datum : datumList){

            if (datum.getName()==null || datum.getName().matches("")){

            }else{
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("nama", datum.getName());
                    jsonObject.put("mobile_id", StringUtil.getRandomString(20));
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