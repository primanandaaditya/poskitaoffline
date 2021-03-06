package com.kitadigi.poskita.activities.reportoffline.detailjual;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.util.StringUtil;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ROHistoriJualDetailAdapter extends BaseAdapter {

    Context context;
    List<DetailJualModel> detailJualModels;
    LayoutInflater inflater;

    //var untuk nampilin gambar
    File file;
    boolean adaGambar;


    public ROHistoriJualDetailAdapter(Context context, List<DetailJualModel> detailJualModels) {
        this.context = context;
        this.detailJualModels = detailJualModels;
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
        return detailJualModels.size();
    }

    @Override
    public Object getItem(int position) {
        return detailJualModels.get(position);
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
            convertView = inflater.inflate(R.layout.report_transaction_detail, null);

        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);

        TextView tv_name_product = (TextView) convertView.findViewById(R.id.tv_name_product);
        TextView tv_label_quantity = (TextView) convertView.findViewById(R.id.tv_label_quantity);
        TextView tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
        TextView tv_label_unit_price = (TextView) convertView.findViewById(R.id.tv_label_unit_price);
        TextView tv_unit_price = (TextView) convertView.findViewById(R.id.tv_unit_price);

        TextView tv_label_subtotal = (TextView) convertView.findViewById(R.id.tv_label_subtotal);
        TextView tv_subtotal = (TextView) convertView.findViewById(R.id.tv_subtotal);

        // getting movie data for the row
        final DetailJualModel detailJualModel = detailJualModels.get(position);

        //aply font
        tv_name_product.setTypeface(fonts);
        tv_label_quantity.setTypeface(fonts);
        tv_quantity.setTypeface(fonts);
        tv_label_unit_price.setTypeface(fonts);
        tv_unit_price.setTypeface(fonts);
        tv_label_subtotal.setTypeface(fonts);
        tv_subtotal.setTypeface(fontsBold);

        tv_name_product.setText(detailJualModel.getNama_produk());
        tv_quantity.setText(detailJualModel.getQty().toString());
        tv_unit_price.setText(StringUtil.formatRupiah(detailJualModel.getPrice()));
        tv_subtotal.setText( StringUtil.formatRupiah(detailJualModel.getSubtotal()));

        //pasang gambar

        //cek dulu apakah gambar benar2 ada di server
        if (detailJualModel.getGambar()==null || detailJualModel.getGambar().matches("")){
            //jika gambar tidak ada
            //tidak usah load
            Log.d("gambar item","String kosong");
        }else{
            //jika gambar ada
            //gambar sudah disimpan di hp user
            //tampilkan di imageview

            file = new File(detailJualModel.getGambar());
            adaGambar=file.exists();

            if (adaGambar){
                //jika file gambar ada
                //load di iv_icon
//                file = new  File(directory.getAbsolutePath() + "/" + item.getImage());
//                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(context)
                        .load(new File(detailJualModel.getGambar()))
                        .into(iv_icon);
                Log.d("picasso ada", file.getAbsolutePath());

            }else{
                //tidak ada gambar
            }
        }

        return convertView;
    }
}
