package com.kitadigi.poskita.activities.printer;

import android.content.Context;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.struk.Struk;
import com.kitadigi.poskita.dao.struk.StrukHelper;

import java.util.List;

public class StrukController implements IStrukRequest,IDeleteStrukRequest,IDeleteSemuaRequest {

    Context context;
    IStrukResult iStrukResult;
    IDeleteStrukResult iDeleteStrukResult;
    IDeleteSemuaResult iDeleteSemuaResult;

    StrukHelper strukHelper;
//    SweetAlertDialog sweetAlertDialog;
    List<Struk> struks;

    public StrukController(Context context, IStrukResult iStrukResult) {
        this.context = context;
        this.iStrukResult = iStrukResult;

        strukHelper=new StrukHelper(context);
    }

    public StrukController(Context context, IStrukResult iStrukResult, IDeleteSemuaResult iDeleteSemuaResult) {
        this.context = context;
        this.iStrukResult = iStrukResult;
        this.iDeleteSemuaResult = iDeleteSemuaResult;

        strukHelper=new StrukHelper(context);
    }

    public StrukController(Context context, IDeleteStrukResult iDeleteStrukResult) {
        this.context = context;
        this.iDeleteStrukResult = iDeleteStrukResult;

        strukHelper=new StrukHelper(context);
    }

    @Override
    public void getStruk() {

//
//        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//        sweetAlertDialog.show();

        try {
            struks = strukHelper.semuaStruk();
            iStrukResult.onStrukSuccess(struks);
//            sweetAlertDialog.dismissWithAnimation();

        }catch (Exception e){
            iStrukResult.onStrukError(e.getMessage());
//            sweetAlertDialog.dismissWithAnimation();
        }
    }

    @Override
    public void deleteStrukById(Long id) {

//        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//        sweetAlertDialog.show();

        try {
            strukHelper.deleteStrukById(id);
            iDeleteStrukResult.onDeleteStrukSuccess(context.getResources().getString(R.string.struk_berhasil_dihapus));
//            sweetAlertDialog.dismissWithAnimation();

        }catch (Exception e){
            iDeleteStrukResult.onDeleteStrukError(e.getMessage());
//            sweetAlertDialog.dismissWithAnimation();
        }
    }

    @Override
    public void deleteSemuaStruk() {
//        sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.now_loading));
//        sweetAlertDialog.show();

        try {
            strukHelper.deleteAllStruk();
            iDeleteSemuaResult.onDeleteSemuaSuccess(context.getResources().getString(R.string.struk_berhasil_dihapus));
//            sweetAlertDialog.dismissWithAnimation();

        }catch (Exception e){
            iDeleteSemuaResult.onDeleteSemuaError(e.getMessage());
//            sweetAlertDialog.dismissWithAnimation();
        }
    }
}
