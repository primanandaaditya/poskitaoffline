package com.kitadigi.poskita.fragment.pembelian;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.fragment.item.BarangResult;
import com.kitadigi.poskita.fragment.item.Datum;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PembelianAdapter extends RecyclerView.Adapter<com.kitadigi.poskita.fragment.pembelian.PembelianAdapter.DataViewHolder> implements Filterable {

    private BarangResult barangResult;
    private Context mContext;
    private PembelianFragment pembelianFragment;
    private List<Datum> realStokDatum;

    //variabel session, untuk menghitung berapa jumlah item yang sudah dimasukkan ke penjualan offline
    SessionManager sessionManager;


    public PembelianAdapter(Context context, BarangResult barangResult, PembelianFragment pembelianFragment) {
        this.mContext                = context;
        this.pembelianFragment       = pembelianFragment;
        this.barangResult            = barangResult;

        realStokDatum=new ArrayList<>();
        realStokDatum.addAll(barangResult.getData());

        sessionManager=new SessionManager(context);

    }

    @Override
    public com.kitadigi.poskita.fragment.pembelian.PembelianAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stok_card, parent, false);
        return new com.kitadigi.poskita.fragment.pembelian.PembelianAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final com.kitadigi.poskita.fragment.pembelian.PembelianAdapter.DataViewHolder holder, int position) {
        final Datum datum     = barangResult.getData().get(position);

        DecimalFormat formatter = new DecimalFormat("#,###,###");


        //akan menampilkan hargabeli
        String price;
        if (datum.getPurchase_price().toString().matches("") || datum.getPurchase_price()==null){
            price="0";
        }else {
            price            = formatter.format(Integer.parseInt(datum.getPurchase_price()));
        }


        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_items.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_items.setText(datum.getName_product());
        holder.tv_price.setText(price);

        holder.iv_icon.setVisibility(View.VISIBLE);


        Picasso.with(mContext)
                .load(Url.DIKI_IMAGE_URL + datum.getImage())
                .into(holder.iv_icon);


        holder.rl_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variabel bitmap untuk di-intent ke fragment jual
                Bitmap bitmap;

                if (holder.iv_icon.getDrawable()==null){
                    bitmap=null;
                }else{
                    bitmap = ((BitmapDrawable)holder.iv_icon.getDrawable()).getBitmap();

                }

                //tampilkan dialog

                pembelianFragment.addItems(datum.getId(),datum.getName_product(),datum.getPurchase_price(),bitmap,"",500);


            }
        });


    }


    @Override
    public int getItemCount() {
        return barangResult.getData().size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_items, tv_price;
        LinearLayout rl_items;

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
                    barangResult.getData().clear();
                    barangResult.getData().addAll(realStokDatum);

                }else{
                    barangResult.getData().clear();
                    for (Datum datum : realStokDatum){
                        if(datum.getName_product().toLowerCase().contains(charString.toLowerCase())){
                            barangResult.getData().add(datum);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = barangResult.getData();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }






}



