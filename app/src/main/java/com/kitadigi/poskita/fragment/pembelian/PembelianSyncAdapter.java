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
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PembelianSyncAdapter extends RecyclerView.Adapter<PembelianSyncAdapter.DataViewHolder> implements Filterable {

    private List<Item> items;
    private Context mContext;
    private PembelianFragment pembelianFragment;

    //untuk nampilin gambar
    File imgFile;
    String pathFoto;

    //untuk keperluan filter
    private List<Item> itemList;

    //variabel session, untuk menghitung berapa jumlah item yang sudah dimasukkan ke penjualan offline
    SessionManager sessionManager;


    public PembelianSyncAdapter(Context context, List<Item> items, PembelianFragment pembelianFragment) {
        this.mContext                = context;
        this.pembelianFragment       = pembelianFragment;
        this.items                   = items;

        itemList=new ArrayList<>();
        itemList.addAll(items);

        sessionManager=new SessionManager(context);

    }

    @Override
    public PembelianSyncAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stok_card, parent, false);
        return new PembelianSyncAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PembelianSyncAdapter.DataViewHolder holder, int position) {
        final Item item     = items.get(position);

        DecimalFormat formatter = new DecimalFormat("#,###,###");


        //akan menampilkan hargabeli
        String price;
        if (item.getPurchase_price().toString().matches("") || item.getPurchase_price()==null){
            price               ="0";
        }else {
            price               = formatter.format(item.getPurchase_price());
        }


        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_items.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        holder.tv_items.setText(item.getName_product());
        holder.tv_price.setText(price);

        holder.iv_icon.setVisibility(View.VISIBLE);


        //tampilkan gambar produk
        //cek dulu apakah ada gambar pada produk ybs
        if (item.getImage().matches("") || item.getImage()==null){

            Log.d("gbr stok",item.getImage());
        }else{
            //jika ada gambar
            //load gambar di imageview

            //tampung foto produk di var pathFoto
            pathFoto = item.getImage();

            //buat file baru dari pathFoto
            imgFile = new File(pathFoto);
//
            //cek apakah ada file tersebut, buat jaga-jaga
            if(imgFile.exists()){
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(mContext)
                        .load(imgFile)
                        .into(holder.iv_icon);
            }


            Log.d("gbr stok adpt",item.getImage());
        }


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

                pembelianFragment.addItems(item.getKode_id(),item.getName_product(),item.getPurchase_price().toString(),bitmap,"",500);


            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
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
                    items.clear();
                    items.addAll(itemList);

                }else{
                    items.clear();
                    for (Item item : itemList){
                        if(item.getName_product().toLowerCase().contains(charString.toLowerCase())){
                            items.add(item);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }






}



