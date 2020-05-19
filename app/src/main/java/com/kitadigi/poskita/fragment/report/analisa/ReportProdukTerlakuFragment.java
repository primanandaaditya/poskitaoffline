package com.kitadigi.poskita.fragment.report.analisa;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.analisa.ReportAnalisaAdapter;
import com.kitadigi.poskita.activities.report.analisa.ReportAnalisaTabActivity;
import com.kitadigi.poskita.activities.report.analisa.ReportRingkasanAnalisaModel;
import com.kitadigi.poskita.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportProdukTerlakuFragment extends BaseFragment implements ReportAnalisaTabActivity.OnReportProdukTerlakuFragmentListener {



    ListView lv_produk_terlaku;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ReportAnalisaTabActivity)getActivity()).setOnReportProdukTerlakuListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_produk_terlaku, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        lv_produk_terlaku=(ListView)getActivity().findViewById(R.id.lv_produk_terlakua);

    }



    @Override
    public void onReportProdukTerlaku_TransferModel(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel) {
        ReportAnalisaAdapter reportAnalisaAdapter=new ReportAnalisaAdapter(reportRingkasanAnalisaModel,getActivity());
        lv_produk_terlaku.setAdapter(reportAnalisaAdapter);
    }
}
