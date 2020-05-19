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

import com.kitadigi.poskita.ItemsPaymentMethodWholeSaleActivity;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.model.Address;

import java.util.ArrayList;
import java.util.List;

public class ItemsSelectAddressWholeSaleAdapter extends RecyclerView.Adapter<ItemsSelectAddressWholeSaleAdapter.DataViewHolder> implements Filterable {
    private List<Address> dataList;
    private List<Address> dataListFiltered;
    private Context mContext;
    private ItemsPaymentMethodWholeSaleActivity posFragment;
    Database db;
    int check;


    public ItemsSelectAddressWholeSaleAdapter(Context context, List<Address> dataList, ItemsPaymentMethodWholeSaleActivity posFragment) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.dataListFiltered   = dataList;
        this.posFragment        = posFragment;
    }

    @Override
    public ItemsSelectAddressWholeSaleAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_address_data, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        final Address address       = dataListFiltered.get(position);

        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_price.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_title.setText(address.getUserAddress());
        holder.tv_price.setText(address.getNameAddress());
        holder.iv_delete.setVisibility(View.GONE);
        holder.iv_edit.setVisibility(View.GONE);

        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    posFragment.setLoadingDialog("Loading ...");
                    posFragment.showAlertDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            posFragment.hideAlertDialog();
                            posFragment.setAddress(address.getUserAddress(), address.getNameAddress(), address.getUsersPhone());
                            //posFragment.addListItemsDashboard(items.getItemsName(), items.getItemsPrice(), items.getItemsImage(), items.getItemsDescription());

                        }
                    }, 1000);

            }
        });
    }


    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price;
        ImageView iv_delete, iv_edit;
        RelativeLayout rl_content;

        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            iv_delete                   = view.findViewById(R.id.iv_delete);
            iv_edit                     = view.findViewById(R.id.iv_edit);
            rl_content                  = view.findViewById(R.id.rl_content);
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
                    List<Address> filteredList = new ArrayList<>();
                    for(Address row : dataList){
                        if(row.getNameAddress().toLowerCase().contains(charString.toLowerCase())){
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
                dataListFiltered = (ArrayList<Address>) results.values;
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
