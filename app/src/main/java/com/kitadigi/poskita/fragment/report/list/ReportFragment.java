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
import com.kitadigi.poskita.activities.reportoffline.jual.ROJualActivity;
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
        daftarReport.add("Report transaksi");
        daftarReport.add("Report pembelian");
        daftarReport.add("Report revenue");
        daftarReport.add("Report stok");
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
                        Intent rpa = new Intent(getActivity(), ReportPembelianActivity.class);
                        startActivity(rpa);
                        break;
                    case 2:
                        Intent rre = new Intent(getActivity(), ReportRevenueActivity.class);
                        startActivity(rre);
                        break;
                    case 3:
                        Intent rstok = new Intent(getActivity(), ReportStokActivity.class);
                        startActivity(rstok);
                        break;
                    case 4:
                        Intent ranalisa = new Intent(getActivity(), ReportAnalisaTabActivity.class);
                        startActivity(ranalisa);
                        break;
                    default:
                        // code block
                }
            }
        });

    }

}
