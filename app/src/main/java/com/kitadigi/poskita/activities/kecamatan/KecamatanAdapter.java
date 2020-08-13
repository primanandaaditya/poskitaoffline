package com.kitadigi.poskita.activities.kecamatan;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;

import java.util.ArrayList;
import java.util.Locale;


public class KecamatanAdapter extends BaseAdapter {

    Context context;
    KecamatanModel kecamatanModel;
    private LayoutInflater inflater;
    private ArrayList<Datum> arraylist;

    public KecamatanAdapter(Context context, KecamatanModel kecamatanModel) {
        this.context = context;
        this.kecamatanModel = kecamatanModel;

        this.arraylist = new ArrayList<Datum>();
        this.arraylist.addAll(kecamatanModel.getData());
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


        //format font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");


        // getting movie data for the row
        Datum datum = kecamatanModel.getData().get(position);
        tv_nama.setText(datum.getSubdistrict_name());
        tv_label.setText(datum.getCity_name());

        //set font
        tv_nama.setTypeface(fontsBold);
        tv_label.setTypeface(fonts);

        return convertView;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        kecamatanModel.getData().clear();
        if (charText.length() == 0) {
            kecamatanModel.getData().addAll(arraylist);
        }
        else
        {
            for (Datum datum : arraylist)
            {
                if (datum.getSubdistrict_name().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    kecamatanModel.getData().add(datum);
                }
            }
        }
        notifyDataSetChanged();

    }
}
