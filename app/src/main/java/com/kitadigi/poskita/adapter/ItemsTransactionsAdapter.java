package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.ItemsTransactionsActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.model.TransactionsDetail;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.DecimalFormat;
import java.util.List;

public class ItemsTransactionsAdapter extends RecyclerView.Adapter<ItemsTransactionsAdapter.DataViewHolder> {
    private List<TransactionsDetail> dataList;
    private Context mContext;
    private ItemsTransactionsActivity itemsTransactionsActivity;

    public ItemsTransactionsAdapter(Context context, List<TransactionsDetail> dataList, ItemsTransactionsActivity itemsTransactionsActivity) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.itemsTransactionsActivity = itemsTransactionsActivity;
    }

    @Override
    public ItemsTransactionsAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transactions_data, parent, false);
        return new ItemsTransactionsAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemsTransactionsAdapter.DataViewHolder holder, int position) {
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

        holder.np_qty.setMax(100);
        holder.np_qty.setMin(1);
        holder.np_qty.setUnit(1);
        holder.np_qty.setValue(Integer.parseInt(items.getQty()));

        holder.np_qty.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                itemsTransactionsActivity.updateData(items.getIdItems(), items.getItemsName(), String.valueOf(holder.np_qty.getValue()), items.getItemsPrice());
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsTransactionsActivity.deleteData(items.getIdItems(), items.getItemsName());
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price;
        NumberPicker np_qty;
        ImageView iv_delete;
        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            np_qty                      = view.findViewById(R.id.np_qty);
            iv_delete                   = view.findViewById(R.id.iv_delete);
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