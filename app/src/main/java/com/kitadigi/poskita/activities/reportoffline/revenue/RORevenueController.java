package com.kitadigi.poskita.activities.reportoffline.revenue;

import android.content.Context;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.activities.reportoffline.revenue.IRORevenueRequest;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.util.StringUtil;

import java.util.List;

public class RORevenueController implements IRORevenueRequest {

    Context context;
    IRORevenueResult result;
    JualMasterHelper jualMasterHelper;
    List<RevenueModel> revenueModels;

    public RORevenueController(Context context, IRORevenueResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getRevenue(String tanggalDari, String tanggalHingga) {

        jualMasterHelper=new JualMasterHelper(context);

//        revenueModels = jualMasterHelper.reportRevenue(tanggalDari, tanggalHingga);
//        result.onRORevenueSuccess(revenueModels);

        try{
            revenueModels = jualMasterHelper.reportRevenue(tanggalDari, tanggalHingga);
            result.onRORevenueSuccess(revenueModels);
        }catch (Exception e){
            result.onRORevenueError(e.getMessage());
        }

    }


    //hitung total qty penjualan
    public void getTotalQty(List<RevenueModel> revenueModels, TextView textView){
        Integer hasil;

        if (revenueModels.size()==0){
            hasil =0;
        }else{

            Integer counter;

            hasil = 0;
            for (RevenueModel revenueModel: revenueModels){
                counter = revenueModel.getQty();
                hasil = hasil + counter;
            }
        }

        textView.setText(hasil.toString() + " item");
    }


    public void getGrandTotal(List<RevenueModel> revenueModels, TextView textView){
        Integer hasil;

        if (revenueModels.size()==0){
            hasil =0;
        }else{

            Integer counter;

            hasil = 0;
            for (RevenueModel revenueModel: revenueModels){
                counter = revenueModel.getGrandtotal();
                hasil = hasil + counter;
            }
        }

        String s = StringUtil.formatRupiah(hasil);
        textView.setText("Grand total : " + s);
    }
}
