package com.kitadigi.poskita.activities.massal.unit;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.kategori.Datum;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IMUnitAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<UnitData> unitDataList;

    public IMUnitAdapter(Context context, List<UnitData> unitDataList) {
        this.context = context;
        this.unitDataList = unitDataList;
    }

    @Override
    public int getCount() {
        return unitDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return unitDataList.get(position);
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
            viewHolder.etNamaUnit = (EditText) convertView
                    .findViewById(R.id.et_nama_unit);
            viewHolder.etSingkatanUnit = (EditText) convertView
                    .findViewById(R.id.et_singkatan_unit);


            //pasang tag dengan index list dari model
            viewHolder.etNamaUnit.setTag(position);
            viewHolder.etSingkatanUnit.setTag(position);

            //set teks dari tiap model
            viewHolder.etNamaUnit.setText(unitDataList.get(position).getName());
            viewHolder.etSingkatanUnit.setText(unitDataList.get(position).getShort_name());

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //buat var integer
        int tag_position_nama=(Integer) viewHolder.etNamaUnit.getTag();
        int tag_position_kode = (Integer) viewHolder.etSingkatanUnit.getTag();

        viewHolder.etNamaUnit.setId(tag_position_nama);
        viewHolder.etSingkatanUnit.setId(tag_position_kode);


        //find id untuk teks nomor urut
        TextView tvNomor = (TextView) convertView.findViewById(R.id.tv_nomor);

        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //tampilkana nomor urut
        tvNomor.setText(String.valueOf(position+1) + ".");
        tvNomor.setTypeface(fonts);


        //saat edittext berubah, kirim juga perubahan ke model ybs
        //nama kategori
        viewHolder.etNamaUnit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etNamaUnit.getId();

                //init id
                final EditText et_Nama_Unit = (EditText) viewHolder.etNamaUnit;

                if(et_Nama_Unit.getText().toString().length()>0){

                   UnitData unitData = unitDataList.get(position2);
                   unitData.setName(et_Nama_Unit.getText().toString());
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
        viewHolder.etSingkatanUnit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etSingkatanUnit.getId();

                //init id
                final EditText et_Singkatan_Unit = (EditText) viewHolder.etSingkatanUnit;

                if(et_Singkatan_Unit.getText().toString().length()>0){

                    UnitData unitData = unitDataList.get(position2);
                    unitData.setShort_name(et_Singkatan_Unit.getText().toString());
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

    //fungsi ini untuk membuat array model
    public List<UnitData> getList(){

        //buat array list baru
        List<UnitData> hasil = new ArrayList<>();
        UnitData unitData;

        //looping model
        for (UnitData u : unitDataList){

            //jika nama kosong
            if (u.getName()==null || u.getName().matches("")){

            }else{
                //masukkan ke dalam array
                unitData = new UnitData();
                unitData.setName(u.getName());
                unitData.setShort_name(u.getShort_name()==null?"":u.getShort_name());
                unitData.setAdditional(u.getAdditional());
                unitData.setBusiness_id(u.getBusiness_id());
                unitData.setId(u.getId());
                unitData.setMobile_id(StringUtil.getRandomString(20));

                hasil.add(unitData);
            }

        }
        return  hasil;
    }


}

class ViewHolder {
    EditText etNamaUnit;
    EditText etSingkatanUnit;
}