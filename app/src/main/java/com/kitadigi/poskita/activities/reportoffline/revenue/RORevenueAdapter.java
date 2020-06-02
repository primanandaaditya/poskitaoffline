package com.kitadigi.poskita.activities.reportoffline.revenue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.reportoffline.detailjual.ROJualDetailActivity;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class RORevenueAdapter extends BaseAdapter {

    Context context;
    List<RevenueModel> revenueModels;
    LayoutInflater inflater;



    public RORevenueAdapter(Context context,  List<RevenueModel> revenueModels) {
        this.context = context;
        this.revenueModels=revenueModels;
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
        return revenueModels.size();
    }

    @Override
    public Object getItem(int position) {
        return revenueModels.get(position);
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
            convertView = inflater.inflate(R.layout.ro_revenue, null);

        TextView tv_tanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
        TextView tv_nomor = (TextView) convertView.findViewById(R.id.tv_nomor);
        TextView tv_total_qty = (TextView) convertView.findViewById(R.id.tv_total_qty);
        TextView tv_grand_total = (TextView) convertView.findViewById(R.id.tv_grand_total);

        TextView tv_label_nomor = (TextView)convertView.findViewById(R.id.tv_label_nomor);
        TextView tv_label_tanggal = (TextView) convertView.findViewById(R.id.tv_label_tanggal);
        TextView tv_label_total_qty = (TextView)convertView.findViewById(R.id.tv_label_total_qty);
        TextView tv_label_grand_total = (TextView)convertView.findViewById(R.id.tv_label_grand_total);


        // getting movie data for the row
        final RevenueModel revenueModel = revenueModels.get(position);

        //aply font
        tv_grand_total.setTypeface(fonts);
        tv_label_nomor.setTypeface(fonts);
        tv_nomor.setTypeface(fonts);
        tv_total_qty.setTypeface(fonts);
        tv_tanggal.setTypeface(fonts);
        tv_label_tanggal.setTypeface(fonts);
        tv_label_grand_total.setTypeface(fonts);
        tv_label_total_qty.setTypeface(fonts);


        tv_tanggal.setText( ": " + revenueModel.getTanggal());
        tv_nomor.setText(": " + revenueModel.getNomor());
        tv_total_qty.setText( ": " + revenueModel.getQty().toString());
        tv_grand_total.setText(": " + StringUtil.formatRupiah(revenueModel.getGrandtotal()));


        return convertView;

    }
}
