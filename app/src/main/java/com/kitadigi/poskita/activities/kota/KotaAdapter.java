package com.kitadigi.poskita.activities.kota;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;


public class KotaAdapter extends BaseAdapter {

    Context context;
    KotaModel kotaModel;
    private LayoutInflater inflater;

    public KotaAdapter(Context context, KotaModel kotaModel) {
        this.context = context;
        this.kotaModel = kotaModel;
    }

    @Override
    public int getCount() {
        return kotaModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return kotaModel.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.wilayah, null);


        TextView tv_nama = (TextView) convertView.findViewById(R.id.tv_nama);
        TextView tv_label = (TextView) convertView.findViewById(R.id.tv_label);


        // getting movie data for the row
        Datum datum = kotaModel.getData().get(position);
        tv_nama.setText(datum.getCity_name());
        tv_label.setText(datum.getProvince());

        return convertView;
    }
}
