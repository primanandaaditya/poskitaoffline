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
import com.kitadigi.poskita.dao.beli.BeliHelper;
import com.kitadigi.poskita.dao.jual.JualHelper;
import com.kitadigi.poskita.dao.stok.Stok;
import com.kitadigi.poskita.database.Database;
import com.kitadigi.poskita.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StokSyncAdapter extends RecyclerView.Adapter<StokSyncAdapter.DataViewHolder> implements Filterable{

    private List<Stok> stoks;
    private Context mContext;
    private JualFragment jualFragment;
    Database db;
    int check;

    //init sqlite, untuk mengetahui jumlah stok yang sudah terjual/dibeli
    JualHelper jualHelper;
    BeliHelper beliHelper;

    //untuk nampilin gambar
    File imgFile;
    String pathFoto;

    //variabel list untuk keperluan filter
    private List<Stok> realStokDatum;

    //variabel session, untuk menghitung berapa jumlah item yang sudah dimasukkan ke penjualan offline
    SessionManager sessionManager;


    public StokSyncAdapter(Context context, List<Stok> stoks, JualFragment jualFragment) {
        this.mContext           = context;
        this.jualFragment       = jualFragment;
        this.stoks              = stoks;

        //siapkan isi variabel untuk filter
        realStokDatum=new ArrayList<>();
        realStokDatum.addAll(stoks);

        //init session
        sessionManager=new SessionManager(context);

        //init sqlite
        jualHelper=new JualHelper(context);
        beliHelper=new BeliHelper(context);
    }

    @Override
    public StokSyncAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stok_card, parent, false);
        return new StokSyncAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StokSyncAdapter.DataViewHolder holder, int position) {

        final Stok stok      = stoks.get(position);

        db                      = new Database(mContext);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        //untuk menentukan harga jual
        //jika null maka diisi dengan '0'
        String price;
        if (stok.getSell_price().toString().matches("") || stok.getSell_price()==null){
            price="0";
        }else {
            price            = formatter.format(Integer.parseInt(stok.getSell_price()));
        }


        Typeface fonts              = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Bold.ttf");


        //aplikasikan font
        holder.tv_items.setTypeface(fontsBold);
        holder.tv_price.setTypeface(fonts);

        //tampikan nama produk dan harga jual di recycle view
        holder.tv_items.setText(stok.getName_product());
        holder.tv_price.setText(price);
        holder.iv_icon.setVisibility(View.VISIBLE);

        //tampilkan gambar produk
        //cek dulu apakah ada gambar pada produk ybs
        if (stok.getImage().matches("") || stok.getImage()==null){

            Log.d("gbr stok",stok.getImage());
        }else{
            //jika ada gambar
            //load gambar di imageview

            //tampung foto produk di var pathFoto
            pathFoto = stok.getImage();

            //buat file baru dari pathFoto
            imgFile = new File(pathFoto);
//
            //cek apakah ada file tersebut, buat jaga-jaga
            if(imgFile.exists()){
                Picasso.with(mContext)
                        .load(imgFile)
                        .into(holder.iv_icon);
            }


            Log.d("gbr stok adpt",stok.getImage());
        }


        //fungsi ini untuk menampilkan popup
        //berisi gambar,harga jual, dan stok
        //juga tombol
        //lihat JualFragment.java di fungsi addItems()
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
                int qty_yang_sudah_terjual;
                int qty_yang_sudah_terbeli;
                int qty_total;
                int qty_available;
                int qty_stok_sekarang;


                qty_stok_sekarang = stok.getQty_available();

                //dapatkan id dari produk
                //lalu hitung jumlah yang sudah diinput melalui session
                id=stok.getId().toString();
                mobile_id = stok.getKode_id();

                //cari jumlah barang yang sudah di-add ke keranjang
                qty_yang_sudah_diinput = sessionManager.jumlahItemYangDiinputKePenjualanOffline(id);

                //cari jumlah barang terjual yang ada disqlite, tapi belum di-sync
                qty_yang_sudah_terjual = jualFragment.getJumlahJual(mobile_id);

                //cari jumlah barang terbeli yang ada disqlite, tapi belum di-sync
                qty_yang_sudah_terbeli = jualFragment.getJumlahBeli(mobile_id);
                Log.d("terbeli", String.valueOf(qty_yang_sudah_terbeli));

                //jumlahkan kedua variabel diatas, nantinya akan dijadikan pengurang
                qty_total = qty_stok_sekarang + qty_yang_sudah_terbeli - (qty_yang_sudah_diinput + qty_yang_sudah_terjual);

                //jika stok =0 atau jumlah stok sama dengan yang diinputkan
                //munculkan pesan, kalau overstok
                if ( (stok.getQty_available() + qty_yang_sudah_terbeli )<=0 || ( (stok.getQty_available() + qty_yang_sudah_terbeli ) - qty_yang_sudah_diinput == 0)  ){
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.stok_kosong),Toast.LENGTH_SHORT).show();
                }else{
                    //jika lebih dari 0
                    //tampilkan dialog

                    //karena sekarang ada mode OFFLINE,
                    //maka fungsi addItems dibawah menggunakan kode_id,
                    //bukan id item lagi

                    //kurangkan qty_available pada stok dengan qty_total
                    //tambahkan dengan qty_terbeli, karena pembelian sifatnya menambah stok
                    qty_available = qty_total;
                    Log.d("qty sdh diinput", String.valueOf(qty_yang_sudah_diinput));
                    Log.d("qty sdh terjual", String.valueOf(qty_yang_sudah_terjual));
                    Log.d("qty sdh terbeli", String.valueOf(qty_yang_sudah_terbeli));
                    Log.d("qty total",String.valueOf(qty_total));

                    //tampilkan popup dari jualfragment.java
                    //lihat fungsi addItems()
                    //untuk parameter id pada fungsi jualFragment.addItems(), diganti dengan kode_id
                    //sehubungan dengan sinkronisasi tabel penjualan
                    jualFragment.addItems(stok.getKode_id(),stok.getName_product(),stok.getSell_price(), bitmap,"", qty_available);

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return stoks.size();
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
                    stoks.clear();
                    stoks.addAll(realStokDatum);

                }else{
                    stoks.clear();
                    for (Stok stok : realStokDatum){
                        if(stok.getName_product().toLowerCase().contains(charString.toLowerCase())){
                            stoks.add(stok);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = stoks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }






}


