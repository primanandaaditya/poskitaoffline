package com.kitadigi.poskita.activities.kota;

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


public class KotaAdapter extends BaseAdapter {

    Context context;
    KotaModel kotaModel;
    private LayoutInflater inflater;
    private ArrayList<Datum> arraylist;


    public KotaAdapter(Context context, KotaModel kotaModel) {
        this.context = context;
        this.kotaModel = kotaModel;

        this.arraylist = new ArrayList<Datum>();
        this.arraylist.addAll(kotaModel.getData());
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

        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");



        TextView tv_nama = (TextView) convertView.findViewById(R.id.tv_nama);
        TextView tv_label = (TextView) convertView.findViewById(R.id.tv_label);
        tv_nama.setTypeface(fontsBold);
        tv_label.setTypeface(fonts);


        // getting movie data for the row
        Datum datum = kotaModel.getData().get(position);
        tv_nama.setText(datum.getCity_name());
        tv_label.setText(datum.getType());

        return convertView;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        kotaModel.getData().clear();
        if (charText.length() == 0) {
            kotaModel.getData().addAll(arraylist);
        }
        else
        {
            for (Datum datum : arraylist)
            {
                if (datum.getCity_name().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    kotaModel.getData().add(datum);
                }
            }
        }
        notifyDataSetChanged();

    }
}
