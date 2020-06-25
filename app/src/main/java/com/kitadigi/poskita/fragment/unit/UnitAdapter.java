package com.kitadigi.poskita.fragment.unit;

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
import com.kitadigi.poskita.dao.produk.ItemHelper;
import com.kitadigi.poskita.fragment.addunit.AddUnitActivity;
import com.kitadigi.poskita.fragment.deleteunit.DeleteUnitController;
import com.kitadigi.poskita.fragment.deleteunit.IDeleteUnitResult;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UnitAdapter extends BaseAdapter implements IDeleteUnitResult {

    //init sqlite
    ItemHelper itemHelper;

    private UnitFragment activity;
    private LayoutInflater inflater;
    private UnitModel unitModel;
    private DeleteUnitController deleteUnitController;


    public UnitAdapter(UnitFragment activity, UnitModel unitModel) {
        this.activity = activity;
        this.unitModel = unitModel;

        deleteUnitController=new DeleteUnitController(activity.getActivity(),this);
    }

    @Override
    public int getCount() {
        return unitModel.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
        final UnitData unitData = unitModel.getData().get(position);


        //nama kategori
        tv_name.setText(unitData.getName());

        //code category
        tv_code_category.setText("(" + unitData.getShort_name() + ")");


//        untuk edit
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity.getActivity(), AddUnitActivity.class);
                intent.putExtra("name", unitData.getName());
                intent.putExtra("short_name", unitData.getShort_name());
                intent.putExtra("id", unitData.getId());
                intent.putExtra("additional", unitData.getAdditional());
                activity.startActivity(intent);
            }
        });

//        untuk delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sebelum dihapus, cek dulu apakah data ini dipakai di tabel item
                itemHelper = new ItemHelper(activity.getActivity());

                boolean ada = itemHelper.adaItemByUnitMobileId(unitData.getMobile_id());

                //jika ada, jangan dihapus
                if (ada){

                    Toast.makeText(activity.getActivity(),activity.getActivity().getResources().getString(R.string.data_tidak_dapat_dihapus),Toast.LENGTH_SHORT).show();
                }else{


                    //kalau tidak ada yang pakai
                    //boleh dihapus

                    new SweetAlertDialog(activity.getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(activity.getResources().getString(R.string.hapus))
                            .setContentText(activity.getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                            .setConfirmText(activity.getResources().getString(R.string.ya))
                            .setCancelText(activity.getResources().getString(R.string.tidak))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    deleteUnitController.deleteKategori(unitData.getAdditional());
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


            }
        });
        return convertView;
    }

    @Override
    public void onDeleteUnitSuccess(BaseResponse baseResponse) {
        Toast.makeText(activity.getActivity(),activity.getActivity().getResources().getString(R.string.delete_ok),Toast.LENGTH_SHORT).show();

        //refresh listview di fragment unit
        activity.onResume();
    }

    @Override
    public void onDeleteUnitError(String error) {
        Toast.makeText(activity.getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
