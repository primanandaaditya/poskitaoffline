package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.AddressFragment;
import com.kitadigi.poskita.model.Address;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.DataViewHolder> {
    private List<Address> dataList;
    private Context mContext;
    private AddressFragment addressFragment;

    public AddressListAdapter(Context context, List<Address> dataList, AddressFragment addressFragment) {
        this.mContext               = context;
        this.dataList               = dataList;
        this.addressFragment        = addressFragment;
    }

    @Override
    public AddressListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_address_data, parent, false);
        return new AddressListAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddressListAdapter.DataViewHolder holder, int position) {
        final Address address     = dataList.get(position);
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_price.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_title.setText(address.getUserAddress());
        holder.tv_price.setText(address.getNameAddress());

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressFragment.editData(address.getIdAddress(), address.getUserAddress(), address.getNameAddress(), address.getUsersPhone());
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressFragment.setCancelConfirm("Hapus Alamat ?", address.getIdAddress());
                addressFragment.showAlertDialog();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price;
        ImageView iv_delete, iv_edit;

        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            iv_delete                   = view.findViewById(R.id.iv_delete);
            iv_edit                     = view.findViewById(R.id.iv_edit);
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