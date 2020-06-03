package com.kitadigi.poskita.activities.reportoffline.kartustok;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.fragment.addbrand.AddBrandActivity;
import com.kitadigi.poskita.fragment.brand.BrandFragment;
import com.kitadigi.poskita.fragment.deletebrand.DeleteBrandController;
import com.kitadigi.poskita.fragment.deletebrand.IDeleteResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KartuStokAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private Context context;
    private List<Item> items;
    File file;
    boolean adaGambar;

    //untuk filter
    private ArrayList<Item> arraylist;

    public KartuStokAdapter(Context context, List<Item> items) {
        this.context=context;
        this.items=items;


        //untuk filter
        this.arraylist = new ArrayList<Item>();
        this.arraylist.addAll(items);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
            convertView = inflater.inflate(R.layout.ro_kartu_stok, null);

        TextView tv_title, tv_price, tv_price_sell;
        ImageView iv_icon, iv_detail;

        tv_title                    = (TextView)convertView.findViewById(R.id.tv_title);
        tv_price                    = convertView.findViewById(R.id.tv_price);
        tv_price_sell               = convertView.findViewById(R.id.tv_price_sell);
        iv_detail                   = convertView.findViewById(R.id.iv_detail);
        iv_icon                     = convertView.findViewById(R.id.iv_icon);

        //setting font
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        final Item item = items.get(position);

        tv_title.setTypeface(fontsBold);
        tv_price.setTypeface(fonts);
        tv_price_sell.setTypeface(fonts);

        tv_title.setText(item.getName_product());
        tv_price.setText("Harga beli " + item.getPurchase_price().toString());
        tv_price_sell.setText("Harga jual " + item.getSell_price().toString());

        //pasang gambar di iv_icon
        iv_icon.setVisibility(View.VISIBLE);

        //cek dulu apakah gambar benar2 ada di server
        if (item.getImage()==null || item.getImage().matches("")){
            //jika gambar tidak ada
            //tidak usah load
            Log.d("gambar item","String kosong");
        }else{
            //jika gambar ada
            //gambar sudah disimpan di hp user
            //tampilkan di imageview

            file = new File(item.getImage());
            adaGambar=file.exists();

            if (adaGambar){
                //jika file gambar ada
                //load di iv_icon
//                file = new  File(directory.getAbsolutePath() + "/" + item.getImage());
//                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(context)
                        .load(new File(item.getImage()))
                        .into(iv_icon);
                Log.d("picasso ada", file.getAbsolutePath());

            }else{
                //tidak ada gambar

            }


        }

        return convertView;
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();

        if (charText.length() == 0) {
            items.addAll(arraylist);
        }
        else
        {
            for (Item item : arraylist)
            {
                if (item.getName_product().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
