package com.kitadigi.poskita.fragment.report.list;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.analisa.ReportAnalisaTabActivity;
import com.kitadigi.poskita.activities.report.pembelian.ReportPembelianActivity;
import com.kitadigi.poskita.activities.report.revenue.ReportRevenueActivity;
import com.kitadigi.poskita.activities.report.stok.ReportStokActivity;
import com.kitadigi.poskita.activities.report.transaction.ReportTransactionActivity;
import com.kitadigi.poskita.activities.reportoffline.beli.ROBeliActivity;
import com.kitadigi.poskita.activities.reportoffline.grafik.harian.GrafikJualHarianActivity;
import com.kitadigi.poskita.activities.reportoffline.jual.ROJualActivity;
import com.kitadigi.poskita.activities.reportoffline.kartustok.PilihBarangActivity;
import com.kitadigi.poskita.activities.reportoffline.revenue.RORevenueActivity;
import com.kitadigi.poskita.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends BaseFragment {


    ListView lv;
    List<String> daftarReport;
    DaftarReportAdapter daftarReportAdapter;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initMain(view);
        return view;
    }

    private void initMain(View view) {

        //init listview
        lv=(ListView)view.findViewById(R.id.lv_report);

        //init judul report
        daftarReport=new ArrayList<>();
        daftarReport.add("Histori penjualan");
        daftarReport.add("Histori pembelian");
        daftarReport.add("Grafik penjualan harian");
        daftarReport.add("Report revenue");
        daftarReport.add("Kartu stok");
        daftarReport.add("Report ringkasan analisa");

        //init adapter untuk listview
        daftarReportAdapter=new DaftarReportAdapter(getActivity(),ReportFragment.this,daftarReport);

        lv.setAdapter(daftarReportAdapter);

        //jika listview diklik
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch(position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), ROJualActivity.class);
                        startActivity(intent);
//                        Intent intent = new Intent(getActivity(), ReportTransactionActivity.class);
//                        startActivity(intent);
                        break;
                    case 1:
                        Intent roHistoriBeli = new Intent(getActivity(), ROBeliActivity.class);
                        startActivity(roHistoriBeli);
//                        Intent rpa = new Intent(getActivity(), ReportPembelianActivity.class);
//                        startActivity(rpa);
                        break;
                    case 2:
                        Intent grafikJualHarian = new Intent(getActivity(), GrafikJualHarianActivity.class);
                        startActivity(grafikJualHarian);
//                        Intent rre = new Intent(getActivity(), ReportRevenueActivity.class);
//                        startActivity(rre);
                        break;
                    case 3:
                        Intent roRevenue = new Intent(getActivity(), RORevenueActivity.class);
                        startActivity(roRevenue);
                        break;
                    case 4:
//                        Intent ranalisa = new Intent(getActivity(), ReportAnalisaTabActivity.class);
//                        startActivity(ranalisa);
                        Intent ikartustok = new Intent(getActivity(), PilihBarangActivity.class);
                        startActivity(ikartustok);
                        break;
                    default:
                        // code block
                }
            }
        });

    }

}
