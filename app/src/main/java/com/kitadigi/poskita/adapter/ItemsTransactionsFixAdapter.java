package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.model.TransactionsDetail;

import java.text.DecimalFormat;
import java.util.List;

public class ItemsTransactionsFixAdapter extends RecyclerView.Adapter<ItemsTransactionsFixAdapter.DataViewHolder> {
    private List<TransactionsDetail> dataList;
    private Context mContext;

    public ItemsTransactionsFixAdapter(Context context, List<TransactionsDetail> dataList) {
        this.mContext           = context;
        this.dataList           = dataList;
    }

    @Override
    public ItemsTransactionsFixAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transactions_data_fix, parent, false);
        return new ItemsTransactionsFixAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsTransactionsFixAdapter.DataViewHolder holder, int position) {
        final TransactionsDetail items     = dataList.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price            = formatter.format(Integer.parseInt(items.getItemsPrice()));
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_price.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_title.setText(items.getItemsName());
        holder.tv_price.setText(price);
        holder.tv_qty.setText("Qty : " + items.getQty());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price, tv_qty;

        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            tv_qty                      = view.findViewById(R.id.tv_qty);
        }
    }

    public void clear() {
        final int size = dataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                dataList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}