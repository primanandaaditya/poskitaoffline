package com.kitadigi.poskita.fragment.inputmassal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.massal.brand.InputMassalBrandActivity;
import com.kitadigi.poskita.activities.massal.kategori.InputMassalKategoriActivity;
import com.kitadigi.poskita.activities.massal.unit.InputMassalUnitActivity;
import com.kitadigi.poskita.fragment.report.list.DaftarReportAdapter;

import java.util.ArrayList;
import java.util.List;


public class InputMassalFragment extends Fragment {



    ListView lv;
    List<String> daftarReport;
    DaftarReportAdapter daftarReportAdapter;

    public InputMassalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_input_massal, container, false);

        View view = inflater.inflate(R.layout.fragment_input_massal, container, false);
        initMain(view);
        return view;
    }

    private void initMain(View view) {

        //init listview
        lv=(ListView)view.findViewById(R.id.lv);

        //init judul report
        daftarReport=new ArrayList<>();
        daftarReport.add("Kategori");
        daftarReport.add("Brand");
        daftarReport.add("Unit");
        daftarReport.add("Produk");

        //init adapter untuk listview
        daftarReportAdapter=new DaftarReportAdapter(getActivity(),daftarReport);

        lv.setAdapter(daftarReportAdapter);

        //jika listview diklik
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch(position) {
                    case 0:

                        //tampilkan input massal kategori
                        Intent imKategori = new Intent(getActivity(), InputMassalKategoriActivity.class);
                        startActivity(imKategori);

                        break;
                    case 1:

                        Intent imBrand = new Intent(getActivity(), InputMassalBrandActivity.class);
                        startActivity(imBrand);

                        break;
                    case 2:

                        Intent imUnit = new Intent(getActivity(), InputMassalUnitActivity.class);
                        startActivity(imUnit);

                        break;
                    case 3:

                        break;

                    default:
                        // code block
                }
            }
        });

    }

}
