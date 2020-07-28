package com.kitadigi.poskita.activities.kecamatan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;


public class KecamatanAdapter extends BaseAdapter {

    Context context;
    KecamatanModel kecamatanModel;
    private LayoutInflater inflater;

    public KecamatanAdapter(Context context, KecamatanModel kecamatanModel) {
        this.context = context;
        this.kecamatanModel = kecamatanModel;
    }

    @Override
    public int getCount() {
        return kecamatanModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return kecamatanModel.getData().get(position);
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
        Datum datum = kecamatanModel.getData().get(position);
        tv_nama.setText(datum.getSubdistrict_name());
        tv_label.setText(datum.getCity_id());

        return convertView;
    }
}
