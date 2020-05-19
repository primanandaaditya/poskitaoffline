package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.POSFragment;
import com.kitadigi.poskita.model.Items;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemsAddNewAdapter extends RecyclerView.Adapter<ItemsAddNewAdapter.DataViewHolder> implements Filterable {
    private List<Items> dataList;
    private List<Items> dataListFiltered;
    private List<Items> dataAll;
    private Context mContext;
    private POSFragment posFragment;

    public ItemsAddNewAdapter(Context context, List<Items> dataList, List<Items> dataAll, POSFragment posFragment) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.dataListFiltered   = dataList;
        this.dataAll            = dataAll;
        this.posFragment        = posFragment;
    }

    @Override
    public ItemsAddNewAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        final Items items       = dataListFiltered.get(position);
        Log.d("Items", "Items " + items.getTipe());
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_items.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_items.setText(items.getItemsName());

        if(items.getTipe().equals("0")){
            holder.tv_price.setVisibility(View.GONE);
            int id = mContext.getResources().getIdentifier(items.getItemsImage(), "drawable", mContext.getPackageName());
//            holder.iv_icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_plus));
            holder.iv_icon.setImageResource(id);
            holder.iv_icon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlack));
            holder.rl_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posFragment.addNewItems();
                }
            });
        }else if(items.getTipe().equals("3")){
            holder.tv_price.setVisibility(View.GONE);
            int id = mContext.getResources().getIdentifier(items.getItemsImage(), "drawable", mContext.getPackageName());
//            holder.iv_icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_plus));
            holder.iv_icon.setImageResource(id);
            holder.iv_icon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlack));
            holder.rl_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posFragment.addCustomItems();
                }
            });
        }else{
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String price            = formatter.format(Integer.parseInt(items.getItemsPrice()));
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_price.setText(price);
            holder.iv_icon.clearColorFilter();
            Glide.with(mContext)
                    .load(items.getItemsImage())
                    .into(holder.iv_icon);
            holder.rl_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posFragment.setLoadingDialog("Loading ...");
                    posFragment.showAlertDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            posFragment.hideAlertDialog();
                            posFragment.addItems(items.getIdItems(), items.getItemsName(), items.getItemsPrice(), items.getItemsImage(), items.getShortcut());
                        }
                    }, 1000);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_items, tv_price;
        RelativeLayout rl_items;

        public DataViewHolder(View view) {
            super(view);
            iv_icon                         = view.findViewById(R.id.iv_icon);
            tv_items                        = view.findViewById(R.id.tv_items);
            tv_price                        = view.findViewById(R.id.tv_price);
            rl_items                        = view.findViewById(R.id.rl_items);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                Log.d("charstring ", charString);
                if(charString.isEmpty()){
                    dataListFiltered = dataList;
                }else{
                    List<Items> filteredList = new ArrayList<>();
                    for(Items row : dataAll){
                        if(row.getItemsName().toLowerCase().contains(charString.toLowerCase())){
                            Log.d("charstring ", row.getItemsName().toLowerCase());
                            filteredList.add(row);
                        }
                    }
                    dataListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataListFiltered = (ArrayList<Items>) results.values;
                notifyDataSetChanged();
            }
        };
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
