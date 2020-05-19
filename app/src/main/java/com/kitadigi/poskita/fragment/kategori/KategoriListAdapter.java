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
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.fragment.addkategori.AddKategoriActivity;
import com.kitadigi.poskita.fragment.deletekategori.DeleteKategoriController;
import com.kitadigi.poskita.fragment.deletekategori.IDeleteResult;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KategoriListAdapter extends BaseAdapter implements IDeleteResult {
    private PrimaKategoriFragment activity;
    private LayoutInflater inflater;
    private KategoriModel kategoriModel;
    Datum datum;

    //init controller
    DeleteKategoriController deleteKategoriController;

    public KategoriListAdapter(PrimaKategoriFragment activity, KategoriModel kategoriModel) {
        this.activity = activity;
        this.kategoriModel = kategoriModel;

        deleteKategoriController=new DeleteKategoriController(activity.getActivity(), this, true);
    }



    @Override
    public int getCount() {
        return kategoriModel.getData().size();
    }

    @Override
    public Object getItem(int location) {
        return kategoriModel.getData().get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_kategori, null);



        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_code_category = (TextView) convertView.findViewById(R.id.tv_code_category);
        ImageView iv_edit=(ImageView)convertView.findViewById(R.id.iv_edit);
        ImageView iv_delete=(ImageView)convertView.findViewById(R.id.iv_delete);


        //setting font
        Typeface fonts              = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

        tv_name.setTypeface(fontsBold);
        tv_code_category.setTypeface(fonts);

        // getting movie data for the row
        final Datum datum = kategoriModel.getData().get(position);


        //nama kategori
        tv_name.setText(datum.getName());

       //code category
        tv_code_category.setText("Kode: " + datum.getCode_category());


        //untuk edit
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity.getActivity(), AddKategoriActivity.class);
                intent.putExtra("nama_kategori", datum.getName());
                intent.putExtra("kode_kategori", datum.getCode_category());
                intent.putExtra("id_kategori", datum.getId());
                intent.putExtra("additional", datum.getAdditional());
                activity.startActivity(intent);
            }
        });

        //untuk delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(activity.getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(activity.getResources().getString(R.string.hapus))
                        .setContentText(activity.getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText(activity.getResources().getString(R.string.ya))
                        .setCancelText(activity.getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                deleteKategoriController.deleteKategori(datum.getAdditional());

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
        Toast.makeText(activity.getActivity(),baseResponse.getMessage(),Toast.LENGTH_SHORT).show();

        //untuk refresh
        activity.onResume();
    }

    @Override
    public void onDeleteError(String error) {
        Toast.makeText(activity.getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
