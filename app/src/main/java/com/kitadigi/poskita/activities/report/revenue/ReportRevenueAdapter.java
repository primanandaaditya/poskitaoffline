package com.kitadigi.poskita.activities.report.revenue;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.util.StringUtil;


public class ReportRevenueAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private ReportRevenueModel reportRevenueModel;
    Context context;


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }


    public ReportRevenueAdapter(ReportRevenueModel reportRevenueModel, Context context) {
        this.reportRevenueModel = reportRevenueModel;
        this.context = context;
        initFont();
    }

    @Override
    public int getCount() {
        return reportRevenueModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return reportRevenueModel.getData().get(position);
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
            convertView = inflater.inflate(R.layout.report_transaction, null);

        TextView tv_tanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
        TextView tv_no_invoice = (TextView) convertView.findViewById(R.id.tv_no_invoice);
        TextView tv_payment_refno = (TextView) convertView.findViewById(R.id.tv_payment_refno);
        TextView tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);

        TextView tv_label_tanggal = (TextView) convertView.findViewById(R.id.tv_label_tanggal);
        TextView tv_label_invoice = (TextView) convertView.findViewById(R.id.tv_label_invoice);
        TextView tv_label_payment = (TextView) convertView.findViewById(R.id.tv_label_payment_ref);


        // getting movie data for the row
        final ReportRevenueData reportRevenueData = reportRevenueModel.getData().get(position);

        //aply font
        tv_label_invoice.setTypeface(fonts);
        tv_label_tanggal.setTypeface(fonts);
        tv_label_payment.setTypeface(fonts);
        tv_tanggal.setTypeface(fonts);
        tv_no_invoice.setTypeface(fonts);
        tv_payment_refno.setTypeface(fonts);
        tv_amount.setTypeface(fontsBold);
        tv_qty.setTypeface(fontsBold);

        tv_tanggal.setText( ": " + StringUtil.tanggalIndonesia(reportRevenueData.getTransaction_date()));
        tv_no_invoice.setText( ": " + reportRevenueData.getInvoice_no());
        tv_payment_refno.setText(": " +reportRevenueData.getPayment_ref_no());
        tv_amount.setText( reportRevenueData.getAmount());
        tv_qty.setText(context.getResources().getString(R.string.label_qty) +reportRevenueData.getQty().toString());

        return convertView;
    }
}
