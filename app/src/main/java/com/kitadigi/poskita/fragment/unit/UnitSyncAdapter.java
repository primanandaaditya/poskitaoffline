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
import com.kitadigi.poskita.dao.unit.Unit;
import com.kitadigi.poskita.fragment.addunit.AddUnitActivity;
import com.kitadigi.poskita.fragment.deleteunit.DeleteUnitController;
import com.kitadigi.poskita.fragment.deleteunit.IDeleteUnitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UnitSyncAdapter extends BaseAdapter implements IDeleteUnitResult {


    private UnitFragment activity;
    private LayoutInflater inflater;
    private List<Unit> units;
    private DeleteUnitController deleteUnitController;

    private ArrayList<Unit> arraylist;

    public UnitSyncAdapter(UnitFragment activity, List<Unit> units) {
        this.activity = activity;
        this.units=units;

        deleteUnitController=new DeleteUnitController(activity.getActivity(),this, true);

        this.arraylist = new ArrayList<Unit>();
        this.arraylist.addAll(units);
    }

    @Override
    public int getCount() {
        return units.size();
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
//        final UnitData unitData = unitModel.getData().get(position);
        final Unit unit = units.get(position);

        //nama kategori
        tv_name.setText(unit.getName());

        //code category
        tv_code_category.setText("(" + unit.getSingkatan() + ")");


//        untuk edit
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity.getActivity(), AddUnitActivity.class);
                intent.putExtra("name", unit.getName());
                intent.putExtra("short_name", unit.getSingkatan());
                intent.putExtra("id", unit.getId().toString());
//                intent.putExtra("additional", unitData.getAdditional());
                activity.startActivity(intent);
            }
        });

//        untuk delete
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
                                deleteUnitController.deleteKategori(unit.getId().toString());
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
    public void onDeleteUnitSuccess(BaseResponse baseResponse) {
        Toast.makeText(activity.getActivity(),activity.getActivity().getResources().getString(R.string.delete_ok),Toast.LENGTH_SHORT).show();

        //refresh listview di fragment unit
        activity.onResume();
    }

    @Override
    public void onDeleteUnitError(String error) {
//        Toast.makeText(activity.getActivity(),error,Toast.LENGTH_SHORT).show();
        activity.onResume();
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        units.clear();

        if (charText.length() == 0) {
            units.addAll(arraylist);
        }
        else
        {
            for (Unit unit : arraylist)
            {
                if (unit.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    units.add(unit);
                }
            }
        }
        notifyDataSetChanged();
    }
}
