package com.kitadigi.poskita.activities.pos;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.pembelian.SubPembelianActivity;
import com.kitadigi.poskita.model.ListTransactionDetail;
import com.kitadigi.poskita.model.TransactionsDetail;

import java.text.DecimalFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubTransaksiAdapter extends RecyclerView.Adapter<com.kitadigi.poskita.activities.pos.SubTransaksiAdapter.DataViewHolder> {
    private ListTransactionDetail listTransactionDetail;
    private Context mContext;

    private SubTransaksiActivity subTransaksiActivity; //jika layar sekarang SubPembelian, maka properti ini harus null, ini untuk menghitung Total di Activity
    private SubPembelianActivity subPembelianActivity; //jika layar sekarang SubTransaksi, maka properti ini harus null, ini untuk menghitung Total di Activity
    boolean modePenjualan; //boolean ini untuk menentukan apakah dipakai sebagai pembelian / penjualan

    public SubTransaksiAdapter(Context mContext,ListTransactionDetail listTransactionDetail, SubTransaksiActivity subTransaksiActivity, SubPembelianActivity subPembelianActivity, boolean modePenjualan) {
        this.listTransactionDetail = listTransactionDetail;
        this.mContext = mContext;
        this.subTransaksiActivity = subTransaksiActivity;
        this.subPembelianActivity = subPembelianActivity;
        this.modePenjualan = modePenjualan;
    }

    public ListTransactionDetail getListTransactionDetail() {
        return listTransactionDetail;
    }

    public void setListTransactionDetail(ListTransactionDetail listTransactionDetail) {
        this.listTransactionDetail = listTransactionDetail;
    }



    @Override
    public com.kitadigi.poskita.activities.pos.SubTransaksiAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_transactions_data, parent, false);
        return new com.kitadigi.poskita.activities.pos.SubTransaksiAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final com.kitadigi.poskita.activities.pos.SubTransaksiAdapter.DataViewHolder holder, int position) {
        final TransactionsDetail transactionsDetail     = listTransactionDetail.getTransactionsDetails().get(position);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price            = formatter.format(Integer.parseInt(transactionsDetail.getItemsPrice()));
        /* Custom fonts */
        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_price.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);
        holder.tv_qty.setTypeface(fontsBold);
        holder.tv_title.setTypeface(fonts);

        holder.tv_title.setText(transactionsDetail.getItemsName());
        holder.tv_price.setText(price);
        holder.tv_qty.setText(transactionsDetail.getQty());

//        holder.np_qty.setMax(1000);
//        holder.np_qty.setMin(1);
//        holder.np_qty.setUnit(1);
//        holder.np_qty.setValue(Integer.parseInt(transactionsDetail.getQty()));
//
//        holder.np_qty.setValueChangedListener(new ValueChangedListener() {
//            @Override
//            public void valueChanged(int value, ActionEnum action) {
//                transactionsDetail.setQty(String.valueOf(value));
//                notifyDataSetChanged();
//
//                //tampilkan total
//                subTransaksiActivity.hitungTotalPenjualan();
////                itemsTransactionsActivity.updateData(items.getIdItems(), items.getItemsName(), String.valueOf(holder.np_qty.getValue()), items.getItemsPrice());
//            }
//        });

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
                                sweetAlertDialog.dismissWithAnimation();


                                //jika ya
                                //hapus dari recycleview
                                listTransactionDetail.getTransactionsDetails().remove(transactionsDetail);
                                notifyDataSetChanged();

                                //jika layar sekarang adalah penjualan, ataupun
                                //hitung total, langsung tampil di Activitynya
                                if (modePenjualan==true){
                                    subTransaksiActivity.hitungTotalPenjualan();
                                }else{
                                    subPembelianActivity.hitungTotalPembelian();
                                }
                                //tampilkan total


                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();



            }
        });

    }


    @Override
    public int getItemCount() {
        return listTransactionDetail.getTransactionsDetails().size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_price, tv_qty;
//        NumberPicker np_qty;
        ImageView iv_delete;
        public DataViewHolder(View view) {
            super(view);
            tv_title                    = view.findViewById(R.id.tv_title);
            tv_price                    = view.findViewById(R.id.tv_price);
            tv_qty                      = view.findViewById(R.id.tv_qty);
            iv_delete                   = view.findViewById(R.id.iv_delete);
        }
    }

    public void clear() {
        final int size = listTransactionDetail.getTransactionsDetails().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listTransactionDetail.getTransactionsDetails().remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
