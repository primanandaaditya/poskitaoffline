package com.kitadigi.poskita.fragment.dashboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kitadigi.poskita.R;


@SuppressLint("ValidFragment")
public class DashboardFragment extends Fragment {


    ImageView iv_pos,iv_pembelian,iv_produk, iv_kategori,iv_brand,iv_unit;
    IDashboard iDashboard;


    @SuppressLint("ValidFragment")
    public DashboardFragment(IDashboard iDashboard) {
        this.iDashboard = iDashboard;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        iv_pos = (ImageView)getActivity().findViewById(R.id.iv_pos);
        iv_pembelian = (ImageView)getActivity().findViewById(R.id.iv_pembelian);
        iv_produk = (ImageView)getActivity().findViewById(R.id.iv_produk);
        iv_kategori = (ImageView)getActivity().findViewById(R.id.iv_kategori);
        iv_brand = (ImageView)getActivity().findViewById(R.id.iv_brand);
        iv_unit = (ImageView)getActivity().findViewById(R.id.iv_unit);


        //tampilkan layar penjualan
        iv_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarPOS();
            }
        });

        //tampilkan layar pembelian
        iv_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarPembelian();
            }
        });

        //tampilkan layar produk
        iv_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarProduk();
            }
        });

        //tampilkan layar kategori
        iv_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarKategori();
            }
        });

        //tampilkan layar brand
        iv_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarBrand();
            }
        });

        //tampilkan layar unit
        iv_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDashboard.layarUnit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}
