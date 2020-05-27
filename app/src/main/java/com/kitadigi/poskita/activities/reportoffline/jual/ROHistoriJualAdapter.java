package com.kitadigi.poskita.activities.reportoffline.jual;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class ROHistoriJualAdapter extends BaseAdapter {

    Context context;
    List<JualMaster> jualMasters;
    LayoutInflater inflater;



    public ROHistoriJualAdapter(Context context, List<JualMaster> jualMasters) {
        this.context = context;
        this.jualMasters = jualMasters;
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
        return jualMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return jualMasters.get(position);
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
            convertView = inflater.inflate(R.layout.ro_histori_jual, null);

        TextView tv_tanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
        TextView tv_no_invoice = (TextView) convertView.findViewById(R.id.tv_no_invoice);
        TextView tv_payment_refno = (TextView) convertView.findViewById(R.id.tv_payment_refno);
        TextView tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);

        TextView tv_label_tanggal = (TextView) convertView.findViewById(R.id.tv_label_tanggal);
        TextView tv_label_invoice = (TextView) convertView.findViewById(R.id.tv_label_invoice);
        TextView tv_label_payment = (TextView) convertView.findViewById(R.id.tv_label_payment_ref);
        TextView tv_label_total_harga = (TextView)convertView.findViewById(R.id.tv_label_total_harga);
        TextView tv_total_harga = (TextView)convertView.findViewById(R.id.tv_total_harga);


        // getting movie data for the row
        final JualMaster jualMaster= jualMasters.get(position);

        //aply font
        tv_label_invoice.setTypeface(fonts);
        tv_label_tanggal.setTypeface(fonts);
        tv_label_payment.setTypeface(fonts);
        tv_tanggal.setTypeface(fonts);
        tv_no_invoice.setTypeface(fonts);
        tv_payment_refno.setTypeface(fonts);
        tv_amount.setTypeface(fontsBold);
        tv_qty.setTypeface(fontsBold);
        tv_label_total_harga.setTypeface(fonts);
        tv_total_harga.setTypeface(fontsBold);

        tv_tanggal.setText( ": " + jualMaster.getTanggal());
        tv_no_invoice.setText( ": " + jualMaster.getNomor());
        tv_payment_refno.setText(": " + StringUtil.formatRupiah(jualMaster.getTotal_pay()));
        tv_total_harga.setText(": " + StringUtil.formatRupiah(jualMaster.getTotal_price()));

        return convertView;
    }
}
