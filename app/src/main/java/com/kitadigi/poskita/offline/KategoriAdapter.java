package com.kitadigi.poskita.offline;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.kategori.Kategori;

import java.util.List;

public class KategoriAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Kategori> kategoris;

    public KategoriAdapter(Context context, List<Kategori> kategoris) {
        this.context = context;
        this.kategoris = kategoris;
    }

    @Override
    public int getCount() {
        return kategoris.size();
    }

    @Override
    public Object getItem(int location) {
        return kategoris.get(location);
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
            convertView = inflater.inflate(R.layout.list_kategori, null);



        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_code_category = (TextView) convertView.findViewById(R.id.tv_code_category);
        ImageView iv_edit=(ImageView)convertView.findViewById(R.id.iv_edit);
        ImageView iv_delete=(ImageView)convertView.findViewById(R.id.iv_delete);


        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        tv_name.setTypeface(fontsBold);
        tv_code_category.setTypeface(fonts);

        // getting movie data for the row
        final Kategori kategori = kategoris.get(position);


        //nama kategori
        tv_name.setText(kategori.getName_category());

        //code category
        tv_code_category.setText("Kode: " + kategori.getCode_category());

        return convertView;
    }


}
