package com.kitadigi.poskita.fragment.report.analisa;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.analisa.ReportAnalisaTabActivity;
import com.kitadigi.poskita.activities.report.analisa.ReportRingkasanAnalisaModel;
import com.kitadigi.poskita.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportAnalisaFragment extends BaseFragment implements ReportAnalisaTabActivity.OnHeadlineSelectedListener {


    //init tv
    TextView tv_nav_header,tv_tanggal;
    TextView tv_label_produk_terjual,tv_produk_terjual;
    TextView tv_label_produk_terlaku,tv_produk_terlaku;
    TextView tv_label_tanggal;
    TextView tv_label_total_keuntungan,tv_total_keuntungan;
    TextView tv_label_total_pembelian,tv_total_pembelian;
    TextView tv_label_total_penjualan,tv_total_penjualan;
    TextView tv_label_total_purchase_order,tv_total_purchase_order;
    TextView tv_label_total_transaksi,tv_total_transaksi;

    //init imageview
    ImageView iv_back,iv_filter;

    //init listview
    ListView lv_produk_terjual;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_analisa, container, false);



    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //findid
        tv_nav_header=(TextView)getActivity().findViewById(R.id.tv_nav_header);
//        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
//        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
        tv_label_produk_terjual=(TextView)getActivity().findViewById(R.id.tv_label_produk_terjual);
        tv_produk_terjual=(TextView)getActivity().findViewById(R.id.tv_produk_terjual);
        tv_label_produk_terlaku=(TextView)getActivity().findViewById(R.id.tv_label_produk_terlaku);
        tv_produk_terlaku=(TextView)getActivity().findViewById(R.id.tv_produk_terlaku);
        tv_label_tanggal=(TextView)getActivity().findViewById(R.id.tv_label_tanggal);
        tv_tanggal=(TextView)getActivity().findViewById(R.id.tv_tanggal);
        tv_total_keuntungan=(TextView)getActivity().findViewById(R.id.tv_total_keuntungan);
        tv_label_total_keuntungan=(TextView)getActivity().findViewById(R.id.tv_label_total_keuntungan);
        tv_total_pembelian=(TextView)getActivity().findViewById(R.id.tv_total_pembelian);
        tv_label_total_pembelian=(TextView)getActivity().findViewById(R.id.tv_label_total_pembelian);
        tv_total_penjualan=(TextView)getActivity().findViewById(R.id.tv_total_penjualan);
        tv_label_total_penjualan=(TextView)getActivity().findViewById(R.id.tv_label_total_penjualan);
        tv_total_purchase_order=(TextView)getActivity().findViewById(R.id.tv_total_purchase_order);
        tv_label_total_purchase_order=(TextView)getActivity().findViewById(R.id.tv_label_total_penjualan);
        tv_total_transaksi=(TextView)getActivity().findViewById(R.id.tv_total_transaksi);
        tv_label_total_transaksi=(TextView)getActivity().findViewById(R.id.tv_label_total_transaksi);
        lv_produk_terjual=(ListView)getActivity().findViewById(R.id.lv_produk_terlaku);


        this.applyFontRegularToTextView(tv_label_produk_terjual);
        this.applyFontBoldToTextView(tv_produk_terjual);
        this.applyFontRegularToTextView(tv_label_produk_terlaku);
        this.applyFontBoldToTextView(tv_produk_terlaku);
        this.applyFontRegularToTextView(tv_label_tanggal);
        this.applyFontBoldToTextView(tv_tanggal);
        this.applyFontRegularToTextView(tv_label_total_keuntungan);
        this.applyFontBoldToTextView(tv_total_keuntungan);
        this.applyFontRegularToTextView(tv_label_total_pembelian);
        this.applyFontBoldToTextView(tv_total_pembelian);
        this.applyFontRegularToTextView(tv_label_total_penjualan);
        this.applyFontBoldToTextView(tv_total_penjualan);
        this.applyFontRegularToTextView(tv_label_total_purchase_order);
        this.applyFontBoldToTextView(tv_total_purchase_order);
        this.applyFontRegularToTextView(tv_label_total_transaksi);
        this.applyFontBoldToTextView(tv_total_transaksi);



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ReportAnalisaTabActivity)getActivity()).setOnHeadlineSelectedListener(this);


    }


    @Override
    public void onArticleSelected(String pesan) {

    }

    @Override
    public void onTransferModel(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel) {
//

        tv_produk_terjual.setText(reportRingkasanAnalisaModel.getProduk_terjual().toString());
        tv_tanggal.setText(reportRingkasanAnalisaModel.getReport_date());
        tv_total_keuntungan.setText(reportRingkasanAnalisaModel.getTotal_keuntungan());
        tv_total_pembelian.setText(reportRingkasanAnalisaModel.getTotal_pembelian());
        tv_total_penjualan.setText(reportRingkasanAnalisaModel.getTotal_penjualan());
        tv_total_purchase_order.setText(reportRingkasanAnalisaModel.getTotal_purchase_order());
        tv_total_transaksi.setText(reportRingkasanAnalisaModel.getTotal_transaksi());

    }
}
