package com.kitadigi.poskita.fragment.item;


import android.content.Context;
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
import com.kitadigi.poskita.fragment.additem.AddBarangResult;
import com.kitadigi.poskita.fragment.deleteitem.DeleteBarangController;
import com.kitadigi.poskita.fragment.edititem.IEditResult;
import com.kitadigi.poskita.util.FileUtil;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrimaItemListAdapter extends RecyclerView.Adapter<com.kitadigi.poskita.fragment.item.PrimaItemListAdapter.DataViewHolder> implements IEditResult {
    private BarangResult dataList;
    private Context mContext;
    private PrimaItemFragment primaItemFragment;
    private DeleteBarangController deleteBarangController;

    //var ini untuk menyimpan gambar ke hp user
    Bitmap bitmap;

    public PrimaItemListAdapter(Context context, BarangResult dataList, PrimaItemFragment primaItemFragment) {
        this.mContext           = context;
        this.dataList           = dataList;
        this.primaItemFragment      = primaItemFragment;
        deleteBarangController=new DeleteBarangController(context, this, true);
    }

    @Override
    public com.kitadigi.poskita.fragment.item.PrimaItemListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items_data, parent, false);

        return new com.kitadigi.poskita.fragment.item.PrimaItemListAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(com.kitadigi.poskita.fragment.item.PrimaItemListAdapter.DataViewHolder holder, int position) {
        final Datum barang     = dataList.getData().get(position);


//        String harga_beli = StringUtil.formatRupiah(barang.getHarga_beli());
//        String harga_jual = StringUtil.formatRupiah(barang.getHarga_jual());

        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_title.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);
        holder.tv_price_sell.setTypeface(fonts);

        holder.tv_title.setText(barang.getName_product());
        holder.tv_price.setText("Harga beli " + barang.getPurchase_price());
        holder.tv_price_sell.setText("Harga jual " + barang.getSell_price());


        holder.iv_icon.setVisibility(View.VISIBLE);

        //cek dulu apakah gambar benar2 ada di server
        if (barang.getImage()==null || barang.getImage().matches("")){
            //jika gambar tidak ada
            //tidak usah load
        }else{
            //jika gambar ada
            //simpan gambar di hp user
            Picasso.with(mContext)
                    .load(Url.DIKI_IMAGE_URL + barang.getImage())
                    .into(FileUtil.picassoImageTarget(mContext,barang.getImage()));

            //tampilkan di imageview
            Picasso.with(mContext)
                    .load(Url.DIKI_IMAGE_URL + barang.getImage())
                    .into(holder.iv_icon);


        }



        Log.d("url gambar", Url.DIKI_IMAGE_URL + barang.getImage());
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                primaItemFragment.editBarang(
                        barang.getId().toString(),
                        barang.getCategory_id().toString(),
                        barang.getUnits_id().toString(),
                        barang.getBrands_id().toString(),
                        barang.getTypes().toString(),
                        barang.getCode_product().toString(),
                        barang.getName_product().toString(),
                        barang.getBrands_name().toString(),
                        barang.getName_category().toString(),
                        barang.getUnits_name().toString(),
                        barang.getImage().toString(),
                        barang.getPurchase_price().toString(),
                        barang.getSell_price().toString(),
                        barang.getQty_stock().toString(),
                        barang.getQty_minimum().toString(),
                        barang.getAdditional().toString()
                );
            }
        });

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
                                deleteBarangController.deleteBarang(barang.getAdditional());
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();


//                itemsFragment.setCancelConfirm("Hapus " + items.getItemsName() + " ?", items.getIdItems());
//                itemsFragment.showAlertDialog();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.getData().size();
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

    public void clear() {
        final int size = dataList.getData().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                dataList.getData().remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}