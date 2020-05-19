package com.kitadigi.poskita.fragment.kategori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriEditDAO;
import com.kitadigi.poskita.fragment.addkategori.AddKategoriActivity;
import com.kitadigi.poskita.fragment.deletekategori.DeleteKategoriController;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KategoriSyncAdapter extends BaseAdapter implements IDeleteResult {

//    private Context context;
    private LayoutInflater inflater;
    private List<Kategori> kategoris;
    private ArrayList<Kategori> arraylist;



    //init untuk mengedit status hapus kategori
    KategoriEditDAO kategoriEditDAO;

    //init controller
    DeleteKategoriController deleteKategoriController;

    //fragment asal
    PrimaKategoriFragment primaKategoriFragment;

    public KategoriSyncAdapter(PrimaKategoriFragment primaKategoriFragment, List<Kategori> kategoris) {
        this.primaKategoriFragment = primaKategoriFragment;
        this.kategoris = kategoris;

        //untuk filter
        arraylist=new ArrayList<>();
        arraylist.addAll(kategoris);

        //untuk delete
        deleteKategoriController=new DeleteKategoriController(primaKategoriFragment.getActivity(),this, true);
//        kategoriEditDAO = new KategoriEditDAO(primaKategoriFragment.getActivity(),this);
//        kategoriDeleteDAO=new KategoriDeleteDAO(primaKategoriFragment.getActivity(),this);
    }

    @Override
    public int getCount() {
        return kategoris.size();
    }

    @Override
    public Object getItem(int location) {
        return kategoris.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) primaKategoriFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_kategori, null);



        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_code_category = (TextView) convertView.findViewById(R.id.tv_code_category);
        ImageView iv_edit=(ImageView)convertView.findViewById(R.id.iv_edit);
        ImageView iv_delete=(ImageView)convertView.findViewById(R.id.iv_delete);


        //setting font
        Typeface fonts              = Typeface.createFromAsset( primaKategoriFragment.getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset( primaKategoriFragment.getActivity().getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset( primaKategoriFragment.getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

        tv_name.setTypeface(fontsBold);
        tv_code_category.setTypeface(fonts);

        // getting movie data for the row
        final Kategori kategori = kategoris.get(position);


        //nama kategori
        tv_name.setText(kategori.getName_category());

        //code category
        tv_code_category.setText("Kode: " + kategori.getCode_category());


        //untuk edit
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( primaKategoriFragment.getActivity(), AddKategoriActivity.class);
                intent.putExtra("nama_kategori", kategori.getName_category());
                intent.putExtra("kode_kategori", kategori.getCode_category());
                intent.putExtra("id_kategori", kategori.getId().toString());
                intent.putExtra("additional", "");
                primaKategoriFragment.getActivity().startActivity(intent);
            }
        });

        //untuk delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog( primaKategoriFragment.getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText( primaKategoriFragment.getActivity().getResources().getString(R.string.hapus))
                        .setContentText( primaKategoriFragment.getActivity().getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText( primaKategoriFragment.getActivity().getResources().getString(R.string.ya))
                        .setCancelText( primaKategoriFragment.getActivity().getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

//                                kategori.setSync_delete(Constants.STATUS_BELUM_SYNC);
//                                kategoriEditDAO.editKategori(kategori);
//                                kategoriDeleteDAO.deleteKategori(kategori);

                                //sebelum hapus data, yang menjadi param URL
                                //bukanlah id yang sudah dienkrip, tapi id yang masih berupa Long/integer
                                //nanti enkrip-nya dilakukan di DeleteKategoriController-nya
                                deleteKategoriController.deleteKategori(kategori.getId().toString());

                                //refresh list
                                primaKategoriFragment.onResume();

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
        
        return convertView;
    }


    @Override
    public void onDeleteSuccess(BaseResponse baseResponse) {
        //refresh listview di fragment asal
        primaKategoriFragment.tampil();
    }

    @Override
    public void onDeleteError(String error) {
        //refresh listview di fragment asal
        primaKategoriFragment.tampil();
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        kategoris.clear();

        if (charText.length() == 0) {
            kategoris.addAll(arraylist);
        }
        else
        {
            for (Kategori kategori : arraylist)
            {
                if (kategori.getName_category().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    kategoris.add(kategori);
                }
            }
        }
        notifyDataSetChanged();

    }

}
