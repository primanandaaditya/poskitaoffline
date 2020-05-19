package com.kitadigi.poskita.activities.printer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.struk.Struk;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StrukAdapter extends BaseAdapter implements IDeleteStrukResult {

    private CetakStrukFragment cetakStrukFragment;
    private LayoutInflater inflater;
    private List<Struk> struks;

    //var untuk mengukur panjang string dari tanggal
    int panjangTanggal;

    //init controller untuk hapus struk di sqlite
    StrukController strukController;

    public StrukAdapter(CetakStrukFragment cetakStrukFragment, List<Struk> struks) {
        this.cetakStrukFragment = cetakStrukFragment;
        this.struks = struks;

        //init controller untuk hapus struk di sqlite
        strukController=new StrukController(cetakStrukFragment.getActivity(),this);

    }

    @Override
    public int getCount() {
        return struks.size();
    }

    @Override
    public Object getItem(int position) {
        return struks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) cetakStrukFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_struk, null);



        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_code= (TextView) convertView.findViewById(R.id.tv_code);
        ImageView iv_delete=(ImageView)convertView.findViewById(R.id.iv_delete);


        //setting font
        Typeface fonts              = Typeface.createFromAsset(cetakStrukFragment.getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface fontsItalic        = Typeface.createFromAsset(cetakStrukFragment.getActivity().getAssets(), "fonts/OpenSans-Italic.ttf");
        Typeface fontsBold          = Typeface.createFromAsset(cetakStrukFragment.getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

        tv_name.setTypeface(fonts);
        tv_code.setTypeface(fonts);

        final Struk struk = struks.get(position);

        //hitung panjang string dari tanggal di var struk
        panjangTanggal = struk.getTanggal().length();

        //jika formatnya 'yyyy/MM/dd HH:mm:ss'
        if (panjangTanggal==19){
            //ambil tanggal-nya saja
            tv_name.setText(struk.getTanggal().substring(0,10));

            //ambil jam-nya saja
            tv_code.setText(struk.getTanggal().substring(11,19));
        }else{
            //tanggal, tapi set ke empty string
            tv_name.setText("");

            //ambil id-nya saja
            tv_code.setText(struk.getId().toString());
        }




        //untuk delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(cetakStrukFragment.getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(cetakStrukFragment.getActivity().getResources().getString(R.string.hapus))
                        .setContentText(cetakStrukFragment.getActivity().getResources().getString(R.string.data_tidak_dapat_dikembalikan))
                        .setConfirmText(cetakStrukFragment.getActivity().getResources().getString(R.string.ya))
                        .setCancelText(cetakStrukFragment.getActivity().getResources().getString(R.string.tidak))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                //hapus struk di sqlite
                                strukController.deleteStrukById(struk.getId());

                                sweetAlertDialog.dismissWithAnimation();
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
    public void onDeleteStrukSuccess(String message) {
        Toast.makeText(cetakStrukFragment.getActivity(),message,Toast.LENGTH_SHORT).show();

        //refresh listview di cetakStrukFragment
        cetakStrukFragment.refreshData();

    }

    @Override
    public void onDeleteStrukError(String error) {
        Toast.makeText(cetakStrukFragment.getActivity(),error,Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onDeleteSuccess(BaseResponse baseResponse) {
//        Toast.makeText(activity.getActivity(),baseResponse.getMessage(),Toast.LENGTH_SHORT).show();
//        activity.onResume();
//    }
//
//    @Override
//    public void onDeleteError(String error) {
//        Toast.makeText(activity.getActivity(), error,Toast.LENGTH_SHORT).show();
//    }
}
