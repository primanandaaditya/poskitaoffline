package com.kitadigi.poskita.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.pembelian.KonfirmasiPembelianActivity;

public class DateUtil {

    public void dateDialog(Activity activity, final TextView textView){

        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        TextView tv_title;
        final DatePicker et_periode;


        dialog = new AlertDialog.Builder(activity);
        inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);



        dialog.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //dapetin tanggal dari date picker
                String tanggal = StringUtil.tanggalDariDatePicker(et_periode);

                //tampilkan repport
                textView.setText(tanggal);
            }
        });

        dialog.setNegativeButton(activity.getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}
