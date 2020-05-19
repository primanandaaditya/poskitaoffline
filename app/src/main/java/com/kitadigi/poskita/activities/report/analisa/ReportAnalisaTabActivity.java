package com.kitadigi.poskita.activities.report.analisa;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.fragment.report.analisa.ReportAnalisaFragment;
import com.kitadigi.poskita.fragment.report.analisa.ReportProdukTerlakuFragment;
import com.kitadigi.poskita.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportAnalisaTabActivity extends BaseActivity implements IReportAnalisaActivityContract.IReportAnalisaMainView {


    //untuk keperluan tab
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    //untuk fragment2 nya
    IReportAnalisaActivityContract.IReportAnalisaPresenter iReportAnalisaPresenter;

    //session untuk get enkrip id user
    SessionManager sessionManager;

    //hashmapp untuk retrofit
    HashMap<String,String> hashMap;

    //untuk menampung id user
    String enkripIdUser;


    //init tv
    TextView tv_nav_header,tv_tanggal;
    TextView tv_label_produk_terjual,tv_produk_terjual;
    TextView tv_label_produk_terlaku,tv_produk_terlaku;
    TextView tv_label_tanggal;
    TextView tv_label_total_keuntungan,tv_total_keuntungan;
    TextView tv_label_total_pembelian,tv_total_pembelian;
    TextView tv_label_total_penjualan,tv_total_penjualan;
    TextView tv_label_total_purchase_order,tv_total_purchase_order;
    TextView tv_label_total_transaksi,tv_total_transaksi;

    //init imageview
    ImageView iv_back,iv_filter;

    //init listview
    ListView lv_produk_terjual;


    //di bawah ini untuk dialog pilih tanggal
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    TextView tv_title;
    DatePicker et_periode_awal;



    OnHeadlineSelectedListener callback;

    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(String pesan);
        public void onTransferModel(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel);
    }





    OnReportProdukTerlakuFragmentListener callbackReportProdukTerlaku;

    public void setOnReportProdukTerlakuListener(OnReportProdukTerlakuFragmentListener listener){
        this.callbackReportProdukTerlaku = listener;
    }

    public interface OnReportProdukTerlakuFragmentListener{
        public void onReportProdukTerlaku_TransferModel(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_analisa_tab);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




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
        sessionManager=new SessionManager(ReportAnalisaTabActivity.this);
        enkripIdUser=sessionManager.getEncryptedIdUsers();


        //findid
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
//


      tv_nav_header.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              callback.onArticleSelected("Halo, dunia");
          }
      });

      DialogForm();

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ReportAnalisaFragment reportAnalisaFragment = new ReportAnalisaFragment();
        adapter.addFragment(reportAnalisaFragment, getResources().getString(R.string.hasil_analisa));


        ReportProdukTerlakuFragment reportProdukTerlakuFragment = new ReportProdukTerlakuFragment();
        adapter.addFragment(reportProdukTerlakuFragment, getResources().getString(R.string.produk_terlaku));

        viewPager.setAdapter(adapter);


    }




    @Override
    public void requestReport(String enkripIdUser, String tanggal) {

        hashMap=new HashMap<>();
        hashMap.put(getResources().getString(R.string.report_date),tanggal);

        iReportAnalisaPresenter=new ReportAnalisaPresenter(this, new ReportAnalisaActivityImpl(enkripIdUser,hashMap,ReportAnalisaTabActivity.this));
        iReportAnalisaPresenter.requestDataFromServer();
    }

    @Override
    public void setDataToView(ReportRingkasanAnalisaModel reportRingkasanAnalisaModel) {

//        jika tidak expired
        if (this.sessionExpired(reportRingkasanAnalisaModel.getMessage())==0){
            callback.onTransferModel(reportRingkasanAnalisaModel);
            callbackReportProdukTerlaku.onReportProdukTerlaku_TransferModel(reportRingkasanAnalisaModel);
        }


    }

    @Override
    public void onError(Throwable throwable) {

        this.showToast(throwable.getMessage());
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    private void DialogForm() {
        dialog = new AlertDialog.Builder(ReportAnalisaTabActivity.this);
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

                String tahun = String.valueOf(et_periode_awal.getYear());
                String bulan = String.valueOf(et_periode_awal.getMonth()+1);
                String tanggal = String.valueOf(et_periode_awal.getDayOfMonth());
                String filterTanggal = tahun + "-" + bulan + "-" +tanggal;

                requestReport(enkripIdUser,filterTanggal);
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

}
