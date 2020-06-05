package com.kitadigi.poskita.activities.reportoffline.terlaris;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.reportoffline.revenue.RevenueModel;
import com.kitadigi.poskita.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ROTerlarisAdapter extends BaseAdapter {

    Context context;
    List<ROTerlarisModel> roTerlarisModels;
    LayoutInflater inflater;

    File file;
    boolean adaGambar;


    public ROTerlarisAdapter(Context context, List<ROTerlarisModel> roTerlarisModels) {
        this.context = context;
        this.roTerlarisModels=roTerlarisModels;
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
        return roTerlarisModels.size();
    }

    @Override
    public Object getItem(int position) {
        return roTerlarisModels.get(position);
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
            convertView = inflater.inflate(R.layout.ro_terlaris, null);

        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView tv_nama_produk = (TextView) convertView.findViewById(R.id.tv_nama_produk);
        TextView tv_jumlah_terjual = (TextView) convertView.findViewById(R.id.tv_jumlah_terjual);
        TextView tv_persentase = (TextView) convertView.findViewById(R.id.tv_persentase);
        RatingBar rating = (RatingBar) convertView.findViewById(R.id.rating);


        // getting movie data for the row
        final ROTerlarisModel roTerlarisModel = roTerlarisModels.get(position);

        //aply font
        tv_nama_produk.setTypeface(fontsBold);
        tv_jumlah_terjual.setTypeface(fonts);
        tv_persentase.setTypeface(fonts);

        //set teks
        tv_nama_produk.setText(roTerlarisModel.getName_product());
        tv_jumlah_terjual.setText("Total terjual : " + roTerlarisModel.getJumlah_terjual().toString());
        tv_persentase.setText("Persentase : " + roTerlarisModel.getPersentase().toString() + "%");
        rating.setRating(roTerlarisModel.getBintang());

        //set gambar
        if (roTerlarisModel.getImage()==null || roTerlarisModel.getImage().matches("")){
            //jika gambar tidak ada
            //tidak usah load
            Log.d("gambar item","String kosong");
        }else{
            //jika gambar ada
            //gambar sudah disimpan di hp user
            //tampilkan di imageview

            file = new File(roTerlarisModel.getImage());
            adaGambar=file.exists();

            if (adaGambar){
                //jika file gambar ada
                //load di iv_icon
//                file = new  File(directory.getAbsolutePath() + "/" + item.getImage());
//                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(context)
                        .load(new File(roTerlarisModel.getImage()))
                        .into(iv_icon);
                Log.d("picasso ada", file.getAbsolutePath());

            }else{
                //tidak ada gambar

            }


        }

        return convertView;

    }
}
