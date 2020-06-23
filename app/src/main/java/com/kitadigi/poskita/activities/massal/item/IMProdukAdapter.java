package com.kitadigi.poskita.activities.massal.item;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.item.Datum;
import com.kitadigi.poskita.fragment.unit.UnitData;
import com.kitadigi.poskita.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class IMProdukAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Datum> datumList;

    public IMProdukAdapter(Context context, List<Datum> datumList) {
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
            convertView = inflater.inflate(R.layout.im_produk, null);


            //find id dari control
            viewHolder.et_items = (EditText) convertView.findViewById(R.id.et_items);
            viewHolder.sp_brand_id = (Spinner) convertView.findViewById(R.id.sp_brand_id);
            viewHolder.sp_kategori = (Spinner) convertView.findViewById(R.id.sp_kategori);
            viewHolder.sp_unit_id = (Spinner) convertView.findViewById(R.id.sp_unit_id);
//            viewHolder.etNamaUnit = (EditText) convertView
//                    .findViewById(R.id.et_nama_unit);
//            viewHolder.etSingkatanUnit = (EditText) convertView
//                    .findViewById(R.id.et_singkatan_unit);
//

            //pasang tag dengan index list dari model
            viewHolder.et_items.setTag(position);
            viewHolder.sp_unit_id.setTag(position);
            viewHolder.sp_kategori.setTag(position);
            viewHolder.sp_brand_id.setTag(position);
//            viewHolder.etNamaUnit.setTag(position);
//            viewHolder.etSingkatanUnit.setTag(position);

            //set teks dari tiap model
            viewHolder.et_items.setText(datumList.get(position).getName_product());

            if (datumList.get(position).getBrands_id()!=null){
                viewHolder.sp_brand_id.setSelection(Integer.parseInt(datumList.get(position).getBrands_id()));
            }

//            viewHolder.sp_kategori.setSelection(Integer.parseInt(datumList.get(position).getCategory_id()));
//            viewHolder.sp_unit_id.setSelection(Integer.parseInt(datumList.get(position).getUnits_id()));

//            viewHolder.etNamaUnit.setText(unitDataList.get(position).getName());
//            viewHolder.etSingkatanUnit.setText(unitDataList.get(position).getShort_name());

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //buat var integer
        int tag_position_nama = (Integer) viewHolder.et_items.getTag();
        int tag_position_unit_id = (Integer)viewHolder.sp_unit_id.getTag();
        int tag_position_kategori_id = (Integer)viewHolder.sp_kategori.getTag();
        int tag_position_brand_id = (Integer)viewHolder.sp_brand_id.getTag();

//        int tag_position_nama=(Integer) viewHolder.etNamaUnit.getTag();
//        int tag_position_kode = (Integer) viewHolder.etSingkatanUnit.getTag();

        viewHolder.et_items.setId(tag_position_nama);
        viewHolder.sp_brand_id.setId(tag_position_brand_id);
        viewHolder.sp_kategori.setId(tag_position_kategori_id);
        viewHolder.sp_unit_id.setId(tag_position_unit_id);
//        viewHolder.etNamaUnit.setId(tag_position_nama);
//        viewHolder.etSingkatanUnit.setId(tag_position_kode);


        //find id untuk teks nomor urut
        TextView tvNomor = (TextView) convertView.findViewById(R.id.tv_nomor);

        //setting font
//        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //tampilkana nomor urut
        tvNomor.setText(String.valueOf(position+1) + ".");


        //saat edittext berubah, kirim juga perubahan ke model ybs
        //nama
        viewHolder.et_items.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.et_items.getId();

                //init id
                final EditText et_Nama = (EditText) viewHolder.et_items;

                if(et_Nama.getText().toString().length()>0){

                    Datum datum = datumList.get(position2);
                    datum.setName_product(et_Nama.getText().toString());
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

        viewHolder.sp_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //var integer
                final int position2 = viewHolder.sp_kategori.getId();

                //init id
                final Spinner sp_kategori_id = (Spinner) viewHolder.sp_kategori;

                Datum datum = datumList.get(position2);
                datum.setCategory_id(String.valueOf(sp_kategori_id.getSelectedItemId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return convertView;
    }

    //fungsi ini untuk membuat array model


}

class ViewHolder {
    TextView tv_nav_header, tv_remark_items, tv_remark_unit, tv_remark_price, tv_remark_price_sell;
    TextView tv_remark_brand, tv_remark_kategori, tv_remark_barkode, tv_stok, tv_stok_minimum;
    EditText et_items, et_price, et_price_sell, et_barkode, et_stok, et_stok_minimum;
    ImageView iv_back, iv_preview_photo;
    Button btn_photo;
    Spinner sp_unit_id, sp_brand_id, sp_kategori;
    ImageButton ib_barkode;
}