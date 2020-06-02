package com.kitadigi.poskita.fragment.item;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.fragment.deleteitem.DeleteBarangController;
import com.kitadigi.poskita.fragment.edititem.IEditResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.DataViewHolder> implements IEditResult {
    private List<Item> items;
    private Context mContext;
    private PrimaItemFragment primaItemFragment;
    private DeleteBarangController deleteBarangController;

    //var ini untuk filter
    private List<Item> itemList;

    //var ini untuk menyimpan gambar ke hp user
    Bitmap bitmap;

    //var ini untuk cek apakah gambar sudah disimpan di hp user
    File file;
    boolean adaGambar, readOnly;
    ContextWrapper contextWrapper;
    Bitmap myBitmap;


    public BarangAdapter(Context context,  List<Item> items, boolean readOnly) {
        this.mContext           = context;
        this.items              = items;
        this.readOnly           = readOnly;
        contextWrapper=new ContextWrapper(context);

        //untuk filter
        itemList=new ArrayList<>();
        itemList.addAll(items);

    }

    public BarangAdapter(Context context,  List<Item> items, PrimaItemFragment primaItemFragment) {
        this.mContext           = context;
        this.items              = items;
        this.primaItemFragment      = primaItemFragment;
        deleteBarangController=new DeleteBarangController(context, this, true);

        contextWrapper=new ContextWrapper(context);

        //untuk filter
        itemList=new ArrayList<>();
        itemList.addAll(items);

    }

    @Override
    public BarangAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items_data, parent, false);

        return new BarangAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BarangAdapter.DataViewHolder holder, int position) {


        //jika read only
        //hide untuk tombol edit dan delete
        if (readOnly){
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_edit.setVisibility(View.GONE);
        }

        final Item item     = items.get(position);

        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");



        holder.tv_title.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);
        holder.tv_price_sell.setTypeface(fonts);

        holder.tv_title.setText(item.getName_product());
        holder.tv_price.setText("Harga beli " + item.getPurchase_price().toString());
        holder.tv_price_sell.setText("Harga jual " + item.getSell_price().toString());


        holder.iv_icon.setVisibility(View.VISIBLE);

        //cek dulu apakah gambar benar2 ada di server
        if (item.getImage()==null || item.getImage().matches("")){
            //jika gambar tidak ada
            //tidak usah load
            Log.d("gambar item","String kosong");
        }else{
            //jika gambar ada
            //gambar sudah disimpan di hp user
            //tampilkan di imageview

            file = new File(item.getImage());
            adaGambar=file.exists();

            if (adaGambar){
                //jika file gambar ada
                //load di iv_icon
//                file = new  File(directory.getAbsolutePath() + "/" + item.getImage());
//                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                holder.iv_icon.setImageBitmap(myBitmap);
                Picasso.with(mContext)
                        .load(new File(item.getImage()))
                        .into(holder.iv_icon);
                Log.d("picasso ada", file.getAbsolutePath());

            }else{
                //tidak ada gambar

            }


        }


        //untuk edit
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("adapter", item.getUnit_id().toString());

                primaItemFragment.editBarang(
                        item.getId().toString(),
                        item.getCategory_id().toString(),
                        item.getUnit_id().toString(),
                        item.getBrand_id().toString(),
                        item.getTypes().toString(),
                        item.getCode_product() == null ? "": item.getCode_product(),
                        item.getName_product() == null ? "" : item.getName_product(),
                        item.getBrand_name() == null ? "" : item.getBrand_name(),
                        item.getCategory_name() == null ? "" : item.getCategory_name(),
                        item.getUnit_name() == null ? "" : item.getUnit_name(),
                        item.getImage() == null ? "" : item.getImage(),
                        item.getPurchase_price().toString(),
                        item.getSell_price().toString(),
                        item.getQty_stock().toString(),
                        item.getQty_minimum().toString(),
                        item.getAdditional() == null ? "" : item.getAdditional()
                );


            }
        });

        //untuk hapus
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.hapus))
                        .setContentText(mContext.getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText(mContext.getResources().getString(R.string.ya))
                        .setCancelText(mContext.getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteBarangController.deleteBarang(item.getId().toString());
                                sweetAlertDialog.dismissWithAnimation();

                                //refresh listview dari PrimaItemFragment.java
                                primaItemFragment.onResume();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();


//
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onSuccess(AddBarangResult addBarangResult) {
        Toast.makeText(mContext,addBarangResult.getMessage(),Toast.LENGTH_SHORT).show();
        //refresh listview sesudah delete
        primaItemFragment.onResume();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();
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


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();

        if (charText.length() == 0) {
            items.addAll(itemList);
        }
        else
        {
            for (Item item : itemList)
            {
                if (item.getName_product().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}