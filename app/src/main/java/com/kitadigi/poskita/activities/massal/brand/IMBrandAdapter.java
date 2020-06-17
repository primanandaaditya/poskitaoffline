package com.kitadigi.poskita.activities.massal.brand;

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
import com.kitadigi.poskita.fragment.brand.BrandData;
import com.kitadigi.poskita.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IMBrandAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BrandData> brandDataList;

    public IMBrandAdapter(Context context, List<BrandData> brandDataList) {
        this.context = context;
        this.brandDataList = brandDataList;
    }

    @Override
    public int getCount() {
        return brandDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return brandDataList.get(position);
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
            convertView = inflater.inflate(R.layout.im_brand, null);


            //find id dari control
            viewHolder.etNamaBrand = (EditText) convertView
                    .findViewById(R.id.et_nama_brand);
            viewHolder.etDeskripsiBrand = (EditText) convertView
                    .findViewById(R.id.et_deskripsi_brand);


            //pasang tag dengan index list dari model
            viewHolder.etNamaBrand.setTag(position);
            viewHolder.etDeskripsiBrand.setTag(position);

            //set teks dari tiap model
            viewHolder.etNamaBrand.setText(brandDataList.get(position).getName());
            viewHolder.etDeskripsiBrand.setText(brandDataList.get(position).getDescription());

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //buat var integer
        int tag_position_nama=(Integer) viewHolder.etNamaBrand.getTag();
        int tag_position_deskripsi = (Integer) viewHolder.etDeskripsiBrand.getTag();

        viewHolder.etNamaBrand.setId(tag_position_nama);
        viewHolder.etDeskripsiBrand.setId(tag_position_deskripsi);


        //find id untuk teks nomor urut
        TextView tvNomor = (TextView) convertView.findViewById(R.id.tv_nomor);

        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        //tampilkana nomor urut
        tvNomor.setText(String.valueOf(position+1) + ".");
        tvNomor.setTypeface(fonts);


        //saat edittext berubah, kirim juga perubahan ke model ybs
        //nama brand
        viewHolder.etNamaBrand.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etNamaBrand.getId();

                //init id
                final EditText et_Nama_Brand = (EditText) viewHolder.etNamaBrand;

                if(et_Nama_Brand.getText().toString().length()>0){

                   BrandData brandData = brandDataList.get(position2);
                   brandData.setName(et_Nama_Brand.getText().toString());
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
        //deskripsi brand
        viewHolder.etDeskripsiBrand.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                //var integer
                final int position2 = viewHolder.etDeskripsiBrand.getId();

                //init id
                final EditText et_Deskripsi_Brand = (EditText) viewHolder.etDeskripsiBrand;

                if(et_Deskripsi_Brand.getText().toString().length()>0){

                    BrandData brandData = brandDataList.get(position2);
                    brandData.setDescription(et_Deskripsi_Brand.getText().toString());
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
    public List<BrandData> getList(){

        //buat array list baru
        List<BrandData> hasil = new ArrayList<>();
        BrandData brandData;

        //looping model
        for (BrandData b: brandDataList){

            //jika nama kosong
            if (b.getName()==null || b.getName().matches("")){

            }else{
                //masukkan ke dalam array
                brandData = new BrandData();
                brandData.setName(b.getName());
                brandData.setDescription(b.getDescription()==null?"":b.getDescription());
                brandData.setAdditional(b.getAdditional());
                brandData.setBusiness_id(b.getBusiness_id());
                brandData.setId(b.getId());
                brandData.setMobile_id(StringUtil.getRandomString(20));

                hasil.add(brandData);
            }

        }
        return  hasil;
    }


}

class ViewHolder {
    EditText etNamaBrand;
    EditText etDeskripsiBrand;
}