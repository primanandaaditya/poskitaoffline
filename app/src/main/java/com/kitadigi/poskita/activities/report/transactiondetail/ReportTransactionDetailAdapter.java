package com.kitadigi.poskita.activities.report.transactiondetail;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

public class ReportTransactionDetailAdapter extends BaseAdapter {

    private ReportTransactionDetailActivity reportTransactionDetailActivity;
    private LayoutInflater inflater;
    private TransactionDetailModel transactionDetailModel;
    Context context;

    public ReportTransactionDetailAdapter(ReportTransactionDetailActivity reportTransactionDetailActivity, TransactionDetailModel transactionDetailModel, Context context) {
        this.reportTransactionDetailActivity = reportTransactionDetailActivity;
        this.transactionDetailModel = transactionDetailModel;
        this.context = context;
    }


    @Override
    public int getCount() {
        return transactionDetailModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return transactionDetailModel.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) reportTransactionDetailActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.report_transaction_detail, null);


        //init font
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");



        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);

        TextView tv_name_product = (TextView) convertView.findViewById(R.id.tv_name_product);
        TextView tv_label_quantity = (TextView) convertView.findViewById(R.id.tv_label_quantity);
        TextView tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
        TextView tv_label_unit_price = (TextView) convertView.findViewById(R.id.tv_label_unit_price);
        TextView tv_unit_price = (TextView) convertView.findViewById(R.id.tv_unit_price);

        TextView tv_label_subtotal = (TextView) convertView.findViewById(R.id.tv_label_subtotal);
        TextView tv_subtotal = (TextView) convertView.findViewById(R.id.tv_subtotal);

        // getting movie data for the row
        final TransactionDetailData transactionDetailData = transactionDetailModel.getData().get(position);

        //aply font
        tv_name_product.setTypeface(fonts);
        tv_label_quantity.setTypeface(fonts);
        tv_quantity.setTypeface(fonts);
        tv_label_unit_price.setTypeface(fonts);
        tv_unit_price.setTypeface(fonts);
        tv_label_subtotal.setTypeface(fonts);
        tv_subtotal.setTypeface(fontsBold);

        tv_name_product.setText(transactionDetailData.getName_product());
        tv_quantity.setText(transactionDetailData.getQuantity().toString());
        tv_unit_price.setText(transactionDetailData.getUnit_price());
        tv_subtotal.setText(transactionDetailData.getSub_total());

        //pasang gambar
        Picasso.with(context)
                .load(Url.DIKI_IMAGE_URL + transactionDetailData.getImage())
                .into(iv_icon);

        return convertView;
    }
}
