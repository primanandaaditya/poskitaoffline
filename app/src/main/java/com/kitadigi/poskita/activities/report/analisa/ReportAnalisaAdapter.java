package com.kitadigi.poskita.activities.report.analisa;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;

public class ReportAnalisaAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private ReportRingkasanAnalisaModel reportRingkasanAnalisaModel;
    Context context;


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    public ReportAnalisaAdapter(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel, Context context) {
        this.reportRingkasanAnalisaModel = reportRingkasanAnalisaModel;
        this.context = context;
        initFont();
    }

    @Override
    public int getCount() {
        return reportRingkasanAnalisaModel.getProduk_terlaku().size();
    }

    @Override
    public Object getItem(int position) {
        return reportRingkasanAnalisaModel.getProduk_terlaku().get(position);
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
            convertView = inflater.inflate(R.layout.listview_produk_terlaku, null);



        TextView tv_label_nama_produk = (TextView) convertView.findViewById(R.id.tv_label_nama_produk);
        TextView tv_nama_produk = (TextView) convertView.findViewById(R.id.tv_nama_produk);
        TextView tv_label_total_sale = (TextView) convertView.findViewById(R.id.tv_label_total_sale);
        TextView tv_total_sale = (TextView) convertView.findViewById(R.id.tv_total_sale);
        TextView tv_label_presentase = (TextView) convertView.findViewById(R.id.tv_label_presentase);
        TextView tv_presentase = (TextView) convertView.findViewById(R.id.tv_presentase);


        // getting movie data for the row
        final ProdukTerlakuModel produkTerlakuModel= reportRingkasanAnalisaModel.getProduk_terlaku().get(position);

        //aply font
        tv_label_nama_produk.setTypeface(fonts);
        tv_nama_produk.setTypeface(fonts);
        tv_label_total_sale.setTypeface(fonts);
        tv_total_sale.setTypeface(fonts);
        tv_label_presentase.setTypeface(fonts);
        tv_presentase.setTypeface(fonts);


        tv_nama_produk.setText(": " + produkTerlakuModel.getName_product());
        tv_total_sale.setText(": " +produkTerlakuModel.getTotal_sale());
        tv_presentase.setText(": " +produkTerlakuModel.getProsentase());


        return convertView;
    }
}
