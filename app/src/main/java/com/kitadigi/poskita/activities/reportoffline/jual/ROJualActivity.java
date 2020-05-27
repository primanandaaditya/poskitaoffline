package com.kitadigi.poskita.activities.reportoffline.jual;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.report.transaction.ReportTransactionActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ROJualActivity extends BaseActivity implements IROHistoriJualResult {

    //init controller
    IROHistoriJualController iroHistoriJualController;

    //session untuk get enkrip id user
    SessionManager sessionManager;

    //hashmapp untuk retrofit
    HashMap<String,String> hashMap;

    //untuk menampung id user
    String enkripIdUser;

    //init listview
    ListView lv;

    //init tv
    TextView tv_nav_header, tv_total_qty, tv_grand_total,tv_tanggal;

    //init imageview
    ImageView iv_back,iv_filter;


    //di bawah ini untuk dialog pilih tanggal
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView tv_title;
    DatePicker et_periode_awal;

    ROHistoriJualAdapter roHistoriJualAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rojual);


        //tombol untuk filter tanggal
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm();
            }
        });

        //iv_back
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //init session manager
        sessionManager=new SessionManager(ROJualActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();

        //init listview report
        lv=(ListView)findViewById(R.id.lv);

        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        tv_total_qty=(TextView)findViewById(R.id.tv_total_qty);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
//        tv_tanggal=(TextView)findViewById(R.id.tv_tanggal);

        //aply font
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_total_qty);
        this.applyFontRegularToTextView(tv_grand_total);
//        this.applyFontRegularToTextView(tv_tanggal);

        //waktu pertama kali, suruh user memilih tanggal
        DialogForm();
    }


    private void DialogForm() {
        dialog = new AlertDialog.Builder(ROJualActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_report_transaction, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //find id
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        et_periode_awal=(DatePicker) dialogView.findViewById(R.id.et_periode_awal);

//        this.applyFontRegularToEditText(et_periode_awal);
        this.applyFontBoldToTextView(tv_title);


        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //dapetin tanggal dari date picker
                String tanggal = StringUtil.tanggalDariDatePicker(et_periode_awal);

                //tampilkan repport
                requestReport(tanggal);
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void requestReport(String tanggal){
        Log.d("tanggal",tanggal);
        iroHistoriJualController=new IROHistoriJualController(ROJualActivity.this,this);
        iroHistoriJualController.getReport(tanggal);
    }

    @Override
    public void onHistoriJualSuccess(List<JualMaster> jualMasters) {
        roHistoriJualAdapter=new ROHistoriJualAdapter(ROJualActivity.this,jualMasters);
        lv.setAdapter(roHistoriJualAdapter);

        //tampilkan jumlah record
        Integer jml = jmlRecord(jualMasters);
        String strJml = jml.toString() + " transaksi";
        tv_total_qty.setText(strJml);

        //tampilkan sum
        Integer sum = sumRecord(jualMasters);
        String strSum = getResources().getString(R.string.grand_total) + " : " + StringUtil.formatRupiah(sum);
        tv_grand_total.setText(strSum);
    }

    @Override
    public void onHistoriJualError(String error) {
        this.showToast(error);
    }

    Integer jmlRecord(List<JualMaster> jualMasters){
        Integer hasil= jualMasters.size();
        return hasil;
    }

    Integer sumRecord(List<JualMaster> jualMasters){
        Integer hasil= 0;
        if (jualMasters.size()==0){
            hasil=0;
        }else{
            Integer counter = 0;
            for (JualMaster jualMaster:jualMasters){
                counter = counter + jualMaster.getTotal_price();
            }
            hasil = counter;
        }
        return hasil;
    }
}
