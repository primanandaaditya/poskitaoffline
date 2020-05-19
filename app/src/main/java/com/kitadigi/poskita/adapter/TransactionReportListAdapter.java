package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.ReportDetailsActivity;
import com.kitadigi.poskita.model.Transactions;
import com.kitadigi.poskita.model.TransactionsList;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransactionReportListAdapter extends RecyclerView.Adapter<TransactionReportListAdapter.MyViewHolder> {

    //private List<Movie> moviesList;

    private TransactionsList eventInformation;
    private ArrayList<Transactions> transactionsDetails;
    private Context mContext;

    public TransactionReportListAdapter(Context mContext, ArrayList<Transactions> transactionsDetails) {
        this.transactionsDetails    = transactionsDetails;
        this.mContext               = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_reports_data, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Transactions items = transactionsDetails.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price            = formatter.format(Integer.parseInt(items.getTotal()));
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_price.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        if(items.getTipe().equals("penjualan") ||items.getTipe().equals("wholesale")){
            holder.tv_title.setText("No Transaksi : " + items.getNo() + " (Wholesale)");
        }else if(items.getTipe().equals("penjualan")){
            holder.tv_title.setText("No Transaksi : " + items.getNo());
        }

        holder.tv_price.setText("Total " + price);
        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_detail = new Intent(mContext, ReportDetailsActivity.class);
                view_detail.putExtra("kodeTransaction", items.getNo());
                view_detail.putExtra("tipe", items.getTipe());
                mContext.startActivity(view_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price;
        ImageView iv_detail;
        RelativeLayout rl_content;

        public MyViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            iv_detail                   = view.findViewById(R.id.iv_detail);
            rl_content                  = view.findViewById(R.id.rl_content);
        }
    }
}
