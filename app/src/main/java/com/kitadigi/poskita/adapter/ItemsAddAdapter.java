package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
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

public class ItemsAddAdapter extends RecyclerView.Adapter implements Filterable {

    private final int VIEW_DATA = 1;
    private final int VIEW_ADD = 0;

    private List<Items> dataList;
    private List<Items> dataListFiltered;
    private Context mContext;
    private POSFragment posFragment;

    int currentPosition;


    public ItemsAddAdapter(Context context, List<Items> dataList, POSFragment posFragment) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.dataListFiltered   = dataList;
        this.posFragment        = posFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_DATA) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_items, parent, false);
            vh = new DataViewHolder(itemView);
        }else {
            View loadView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_add_items, parent, false);
            vh = new AddViewHolder(loadView);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DataViewHolder){
            final Items items       = dataListFiltered.get(position);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String price            = formatter.format(Integer.parseInt(items.getItemsPrice()));
            Log.d("Items", "Items " + items.getItemsName());
            /* Custom fonts */
            Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
            Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
            Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

            ((DataViewHolder) holder).tv_items.setTypeface(fontsBold);
            ((DataViewHolder) holder).tv_price.setTypeface(fonts);


            ((DataViewHolder) holder).tv_items.setText(items.getItemsName());
            ((DataViewHolder) holder).tv_price.setText(price);
            Glide.with(mContext)
                    .load(items.getItemsImage())
                    .into(((DataViewHolder) holder).iv_icon);

            ((DataViewHolder) holder).rl_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posFragment.setLoadingDialog("Loading ...");
                    posFragment.showAlertDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            posFragment.hideAlertDialog();
                            posFragment.addItems(items.getIdItems(), items.getItemsName(), items.getItemsPrice(), items.getItemsImage(), items.getTipe());
                        }
                    }, 1000);
                }
            });
        }else{
            currentPosition = position;
            Log.d("Items, ", "current position " + currentPosition);
            /* Custom fonts */
            Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
            Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
            Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

            ((AddViewHolder) holder).tv_items.setTypeface(fontsBold);
            ((AddViewHolder) holder).iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posFragment.addNewItems();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        Log.d("Items, ", "Ori " + String.valueOf(dataListFiltered.size()));
        return dataListFiltered.size();
    }
    @Override
    public int getItemViewType(int position) {

        Log.d("Items, ", "Position " + position);
//        Log.d("Items, ", "Ori " + String.valueOf(dataListFiltered.size()));
//        Log.d("Items, ", "Minus " + String.valueOf(dataListFiltered.size() - 1));
        if(position == 0){
            return VIEW_ADD;
        } else {
            return VIEW_DATA;
        }
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

    public static class AddViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_items;

        public AddViewHolder(View view) {
            super(view);
            iv_icon                         = view.findViewById(R.id.iv_icon);
            tv_items                        = view.findViewById(R.id.tv_items);
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
                    for(Items row : dataList){
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
