package com.kitadigi.poskita.activities.printer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.pos.PrintPreviewActivity;
import com.kitadigi.poskita.base.BaseFragment;
import com.kitadigi.poskita.dao.struk.Struk;
import com.kitadigi.poskita.util.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

//Fragment ini untuk cetak struk2 lama
public class CetakStrukFragment extends BaseFragment implements IStrukResult,IDeleteSemuaResult {


    //init widget
    Button btnHapus;

    //init controller
    //untuk get semua struk
    StrukController strukController;

    //init listview
    ListView listView;

    //init adapter
    StrukAdapter strukAdapter;

    public CetakStrukFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cetak_struk, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //findid
        listView=(ListView)getActivity().findViewById(R.id.lv);
        btnHapus=(Button)getActivity().findViewById(R.id.btnHapus);
        this.applyFontBoldToButton(btnHapus);

        //init helper sqlite
        //data struk lama disimpan di tabel struk
        strukController=new StrukController(getActivity(),this,this);
        strukController.getStruk();


        //tombol untuk hapus semua struk di sqlite
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getActivity().getResources().getString(R.string.hapus))
                        .setContentText(getActivity().getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText(getActivity().getResources().getString(R.string.ya))
                        .setCancelText(getActivity().getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                //hapus struk di sqlite
                                strukController.deleteSemuaStruk();
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

            }
        });
    }

    public void refreshData(){
        strukController.getStruk();
    }

    @Override
    public void onStrukSuccess(List<Struk> struks) {

        //tampilkan list struk ke listview
        strukAdapter = new StrukAdapter(this,struks);
        listView.setAdapter(strukAdapter);

        //kalau diklik, langsung ke ScanActivity.java
        //juga lemparkan intent konten struk
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //tampung var struk dari listview
                Struk struk = (Struk)parent.getAdapter().getItem(position);

                //intent, lemparkan konten struk
                Intent intent = new Intent(getActivity(), PrintPreviewActivity.class);
                intent.putExtra(Constants.STRUK,struk.getDeskripsi());

                //mulai intent
                startActivity(intent);

            }
        });
    }

    @Override
    public void onStrukError(String error) {

        //tampilkan error
        this.showToast(error);
    }

    @Override
    public void onDeleteSemuaSuccess(String message) {
        this.showToast(message);
        //refresh listview
        refreshData();
    }

    @Override
    public void onDeleteSemuaError(String error) {
        this.showToast(error);
    }
}
