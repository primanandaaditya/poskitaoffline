package com.kitadigi.poskita.activities.report.stokdetail;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.util.StringUtil;

public class ReportStokDetailAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private ReportDetailModel reportDetailModel;
    Context context;

    public ReportStokDetailAdapter(ReportDetailModel reportDetailModel, Context context) {
        this.reportDetailModel = reportDetailModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reportDetailModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return reportDetailModel.getData().get(position);
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
            convertView = inflater.inflate(R.layout.report_stok_detail, null);


        //init font
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");




        TextView tv_label_tipe_transaksi = (TextView) convertView.findViewById(R.id.tv_label_tipe_transaksi);
        TextView tv_tipe_transaksi = (TextView) convertView.findViewById(R.id.tv_tipe_transaksi);
        TextView tv_label_no_transaksi = (TextView) convertView.findViewById(R.id.tv_label_no_transaksi);
        TextView tv_no_transaksi = (TextView) convertView.findViewById(R.id.tv_no_transaksi);
        TextView tv_label_status_pembayaran = (TextView) convertView.findViewById(R.id.tv_label_status_pembayaran);
        TextView tv_status_pembayaran = (TextView) convertView.findViewById(R.id.tv_status_pembayaran);
        TextView tv_label_status_transaksi = (TextView) convertView.findViewById(R.id.tv_label_status_transaksi);
        TextView tv_status_transaksi = (TextView) convertView.findViewById(R.id.tv_status_transaksi);
        TextView tv_label_tanggal_pembayaran = (TextView) convertView.findViewById(R.id.tv_label_tanggal_pembayaran);
        TextView tv_tanggal_pembayaran = (TextView)convertView.findViewById(R.id.tv_tanggal_pembayaran);
        TextView tv_label_jumlah = (TextView) convertView.findViewById(R.id.tv_label_jumlah);
        TextView tv_jumlah = (TextView) convertView.findViewById(R.id.tv_jumlah);

        // getting movie data for the row
        final ReportDetailData reportDetailData = reportDetailModel.getData().get(position);

        //aply font
        tv_tipe_transaksi.setTypeface(fonts);
        tv_label_tipe_transaksi.setTypeface(fonts);
        tv_label_no_transaksi.setTypeface(fonts);
        tv_no_transaksi.setTypeface(fonts);
        tv_label_status_pembayaran.setTypeface(fonts);
        tv_status_pembayaran.setTypeface(fonts);
        tv_label_status_transaksi.setTypeface(fonts);
        tv_status_transaksi.setTypeface(fonts);
        tv_label_tanggal_pembayaran.setTypeface(fonts);
        tv_tanggal_pembayaran.setTypeface(fonts);
        tv_label_jumlah.setTypeface(fonts);
        tv_jumlah.setTypeface(fonts);

        //isi nilai
        tv_tipe_transaksi.setText(": " + reportDetailData.getType_transaction());
        tv_no_transaksi.setText(": " + reportDetailData.getNo_transaction());
        tv_status_pembayaran.setText(": " + reportDetailData.getStatus_payment());
        tv_status_transaksi.setText(": " + reportDetailData.getStatus_transaction());
        tv_tanggal_pembayaran.setText(": " + StringUtil.tanggalIndonesia(reportDetailData.getTransaction_date()));
        tv_jumlah.setText(": " + reportDetailData.getQuantity());

        return convertView;
    }
}
