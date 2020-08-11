package com.kitadigi.poskita.fragment.pos;

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
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.dao.belimaster.BeliMasterHelper;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.Url;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StokAdapter extends RecyclerView.Adapter<com.kitadigi.poskita.fragment.pos.StokAdapter.DataViewHolder> implements Filterable{

    private StokModel stokModel;
    private Context mContext;
    private JualFragment jualFragment;
    Database db;
    int check;
    private List<StokDatum> realStokDatum;

    //variabel session, untuk menghitung berapa jumlah item yang sudah dimasukkan ke penjualan offline
    SessionManager sessionManager;


    public StokAdapter(Context context, StokModel stokModel, JualFragment jualFragment) {
        this.mContext           = context;
        this.jualFragment       = jualFragment;
        this.stokModel          = stokModel;

        realStokDatum=new ArrayList<>();
        realStokDatum.addAll(stokModel.getData());

        sessionManager=new SessionManager(context);

    }



    @Override
    public com.kitadigi.poskita.fragment.pos.StokAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stok_card, parent, false);
        return new com.kitadigi.poskita.fragment.pos.StokAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final com.kitadigi.poskita.fragment.pos.StokAdapter.DataViewHolder holder, int position) {
        final StokDatum stokDatum      = stokModel.getData().get(position);

        db                      = new Database(mContext);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        String price;
        if (stokDatum.getSell_price().toString().matches("") || stokDatum.getSell_price()==null){
            price="0";
        }else {
            price            = formatter.format(Integer.parseInt(stokDatum.getSell_price()));
        }


        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");

        holder.tv_items.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);


        holder.tv_items.setText(stokDatum.getName_product());
        holder.tv_price.setText(price);

        holder.iv_icon.setVisibility(View.VISIBLE);


        Picasso.with(mContext)
                .load(Url.DIKI_IMAGE_URL + stokDatum.getImage())
                .into(holder.iv_icon);
        Log.d("diki image",Url.DIKI_IMAGE_URL + stokDatum.getImage());




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


                //jika stok 0, atau hasil inputan sudah sama dengan 0
                //tidak tampil dialog
                String id, mobile_id;
                int qty_yang_sudah_diinput;
                int qty_yang_sudah_dibeli;


                //dapatkan id dari produk
                //lalu hitung jumlah yang sudah diinput melalui session
                id=stokDatum.getId();
                mobile_id = stokDatum.getKode_id();

                qty_yang_sudah_diinput = sessionManager.jumlahItemYangDiinputKePenjualanOffline(id);
//                qty_yang_sudah_dibeli = jualFragment.getJumlahBeli(mobile_id);
//                Log.d("qty sdh beli", String.valueOf(qty_yang_sudah_dibeli));

                //jika stok =0 atau jumlah stok sama dengan yang diinputkan
                //munculkan pesan, kalau overstok
//                if ( (stokDatum.getQty_available() + qty_yang_sudah_dibeli )<=0 || ( (stokDatum.getQty_available() - qty_yang_sudah_diinput) + qty_yang_sudah_dibeli ==0 )  ){
//                    Toast.makeText(mContext,mContext.getResources().getString(R.string.stok_kosong),Toast.LENGTH_SHORT).show();
//                }else{
//                    //jika lebih dari 0
//                    //tampilkan dialog
//
//                    jualFragment.addItems(stokDatum.getId(),stokDatum.getName_product(),stokDatum.getSell_price(), bitmap,"", stokDatum.getQty_available());
//                }

            }
        });




    }


    @Override
    public int getItemCount() {
        return stokModel.getData().size();
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
                    stokModel.getData().clear();
                    stokModel.getData().addAll(realStokDatum);

                }else{
                    stokModel.getData().clear();
                    for (StokDatum stokDatum : realStokDatum){
                        if(stokDatum.getName_product().toLowerCase().contains(charString.toLowerCase())){
                            stokModel.getData().add(stokDatum);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = stokModel.getData();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }





}


