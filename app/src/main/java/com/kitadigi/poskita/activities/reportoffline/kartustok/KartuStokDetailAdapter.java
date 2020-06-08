package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.reportoffline.revenue.RevenueModel;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class KartuStokDetailAdapter extends BaseAdapter {

    Context context;
    List<KartuStokModel> kartuStokModels;
    LayoutInflater inflater;



    public KartuStokDetailAdapter(Context context, List<KartuStokModel> kartuStokModels) {
        this.context = context;
        this.kartuStokModels=kartuStokModels;
        initFont();
    }


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    @Override
    public int getCount() {
        return kartuStokModels.size();
    }

    @Override
    public Object getItem(int position) {
        return kartuStokModels.get(position);
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
            convertView = inflater.inflate(R.layout.ro_kartu_stok_detail, null);

        TextView tv_tanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
        TextView tv_keluar = (TextView) convertView.findViewById(R.id.tv_keluar);
        TextView tv_masuk = (TextView) convertView.findViewById(R.id.tv_masuk);
        TextView tv_sisa = (TextView) convertView.findViewById(R.id.tv_sisa);

        TextView tv_label_keluar = (TextView)convertView.findViewById(R.id.tv_label_keluar);
        TextView tv_label_tanggal = (TextView) convertView.findViewById(R.id.tv_label_tanggal);
        TextView tv_label_masuk = (TextView)convertView.findViewById(R.id.tv_label_masuk);
        TextView tv_label_sisa = (TextView)convertView.findViewById(R.id.tv_label_sisa);


        // getting movie data for the row
        final KartuStokModel kartuStokModel = kartuStokModels.get(position);

        //aply font
        tv_sisa.setTypeface(fonts);
        tv_masuk.setTypeface(fonts);
        tv_keluar.setTypeface(fonts);
        tv_tanggal.setTypeface(fonts);
        tv_label_tanggal.setTypeface(fonts);
        tv_label_keluar.setTypeface(fonts);
        tv_label_masuk.setTypeface(fonts);
        tv_label_sisa.setTypeface(fonts);


        tv_tanggal.setText( ": " + kartuStokModel.getTanggal());
        tv_keluar.setText(": " + kartuStokModel.getKeluar().toString());
        tv_masuk.setText( ": " + kartuStokModel.getMasuk().toString());
        tv_sisa.setText(": " + kartuStokModel.getSisa().toString());


        return convertView;

    }
}
