package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.ItemsFragment;
import com.kitadigi.poskita.model.Items;

import java.text.DecimalFormat;
import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.DataViewHolder> {
    private List<Items> dataList;
    private Context mContext;
    private ItemsFragment itemsFragment;
    public ItemsListAdapter(Context context, List<Items> dataList, ItemsFragment itemsFragment) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.itemsFragment      = itemsFragment;
    }

    @Override
    public ItemsListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items_data, parent, false);
        return new ItemsListAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsListAdapter.DataViewHolder holder, int position) {
        final Items items     = dataList.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price            = formatter.format(Integer.parseInt(items.getItemsPrice()));
        String pricesell        = formatter.format(Integer.parseInt(items.getItemsPriceSell()));
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_title.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);
        holder.tv_price_sell.setTypeface(fonts);

        holder.tv_title.setText(items.getItemsName());
        holder.tv_price.setText("Harga Beli " + price);
        holder.tv_price_sell.setText("Harga Jual " + pricesell);

        Glide.with(mContext)
                .load(items.getItemsImage())
                .into(holder.iv_icon);

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemsFragment.editData(category.getId(), category.getCategory().toUpperCase());
                itemsFragment.editData(items.getIdItems(), items.getItemsName(), items.getItemsPrice(), items.getItemsPriceSell(), items.getItemsImage());
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsFragment.setCancelConfirm("Hapus " + items.getItemsName() + " ?", items.getIdItems());
                itemsFragment.showAlertDialog();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price, tv_price_sell;
        ImageView iv_icon, iv_delete, iv_edit;

        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            tv_price_sell               = view.findViewById(R.id.tv_price_sell);
            iv_delete                   = view.findViewById(R.id.iv_delete);
            iv_edit                     = view.findViewById(R.id.iv_edit);
            iv_icon                     = view.findViewById(R.id.iv_icon);
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