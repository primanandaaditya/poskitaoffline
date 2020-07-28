package com.kitadigi.poskita.activities.propinsi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;

public class PropinsiAdapter extends BaseAdapter {

    Context context;
    PropinsiModel propinsiModel;
    private LayoutInflater inflater;

    public PropinsiAdapter(Context context, PropinsiModel propinsiModel) {
        this.context = context;
        this.propinsiModel = propinsiModel;
    }

    @Override
    public int getCount() {
        return propinsiModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return propinsiModel.getData().get(position);
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
        Datum datum = propinsiModel.getData().get(position);
        tv_nama.setText(datum.getProvince());
        tv_label.setText(datum.getProvince_id());

        return convertView;
    }
}
