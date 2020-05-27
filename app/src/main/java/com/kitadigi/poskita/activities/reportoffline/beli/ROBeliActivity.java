package com.kitadigi.poskita.activities.reportoffline.beli;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.belimaster.BeliMaster;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.nio.ReadOnlyBufferException;
import java.util.HashMap;
import java.util.List;

public class ROBeliActivity extends BaseActivity implements IROHistoriBeliResult {


    //init controller
    IROHistoriBeliController iroHistoriBeliController;

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

    ROHistoriBeliAdapter roHistoriBeliAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robeli);


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
        sessionManager=new SessionManager(ROBeliActivity.this);
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
        dialog = new AlertDialog.Builder(ROBeliActivity.this);
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
        iroHistoriBeliController=new IROHistoriBeliController(ROBeliActivity.this,this);
        iroHistoriBeliController.getReport(tanggal);
    }

    @Override
    public void onHistoriBeliSuccess(List<BeliMaster> beliMasters) {

        //tampilkan query di listview
        roHistoriBeliAdapter=new ROHistoriBeliAdapter(ROBeliActivity.this, beliMasters);
        lv.setAdapter(roHistoriBeliAdapter);

        //tampilkan jml row
        Integer jml = iroHistoriBeliController.getCount(beliMasters);
        tv_total_qty.setText(jml.toString() + " transaksi");

        Integer sum = iroHistoriBeliController.getSum(beliMasters);
        tv_grand_total.setText(getResources().getString(R.string.grand_total) + ": " + sum.toString());

    }

    @Override
    public void onHistoriBeliError(String error) {
        this.showToast(error);
    }
}
