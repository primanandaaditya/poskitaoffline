package com.kitadigi.poskita.fragment.brand;

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
import com.kitadigi.poskita.dao.brand.Brand;
import com.kitadigi.poskita.fragment.addbrand.AddBrandActivity;
import com.kitadigi.poskita.fragment.deletebrand.DeleteBrandController;
import com.kitadigi.poskita.fragment.deletebrand.IDeleteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BrandAdapter extends BaseAdapter implements IDeleteResult {

    private BrandFragment activity;
    private LayoutInflater inflater;
//    private BrandModel brandModel;
    private List<Brand> brands;

    //untuk filter
    private ArrayList<Brand> arraylist;

    //init controller
    DeleteBrandController deleteBrandController;

    public BrandAdapter(BrandFragment activity, List<Brand> brands) {
        this.activity = activity;
        this.brands=brands;

        deleteBrandController=new DeleteBrandController(activity.getActivity(),this,true);

        //untuk filter
        this.arraylist = new ArrayList<Brand>();
        this.arraylist.addAll(brands);

    }

    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        return brands.get(position);
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

        final Brand brand = brands.get(position);



        //nama kategori
        tv_name.setText(brand.getName());

        //code category
        tv_code_category.setText("Deskripsi: " + brand.getDescription());


        //untuk edit
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity.getActivity(), AddBrandActivity.class);
                intent.putExtra("name", brand.getName());
                intent.putExtra("description", brand.getDescription());
                intent.putExtra("id", brand.getId().toString());
//                intent.putExtra("additional", brandData.getAdditional());
                activity.startActivity(intent);

            }
        });

        //untuk delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tampilkan dialog yes-no untuk hapus data
                new SweetAlertDialog(activity.getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(activity.getResources().getString(R.string.hapus))
                        .setContentText(activity.getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText(activity.getResources().getString(R.string.ya))
                        .setCancelText(activity.getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                                //sebelum hapus data, yang menjadi param URL
                                //bukanlah id yang sudah dienkrip, tapi id yang masih berupa Long/integer
                                //nanti enkrip-nya dilakukan di DeleteBrandController-nya
                                deleteBrandController.deleteBrand(brand.getId().toString());

                                //list refreesh
                                activity.onResume();
//                                deleteBrandController.deleteBrand(brandData.getAdditional());
//                                deleteKategoriController.deleteKategori(datum.getAdditional());

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
        activity.onResume();
    }

    @Override
    public void onDeleteError(String error) {
        Toast.makeText(activity.getActivity(), error,Toast.LENGTH_SHORT).show();
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        brands.clear();

        if (charText.length() == 0) {
            brands.addAll(arraylist);
        }
        else
        {
            for (Brand brand : arraylist)
            {
                if (brand.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    brands.add(brand);
                }
            }
        }
        notifyDataSetChanged();
    }
}
