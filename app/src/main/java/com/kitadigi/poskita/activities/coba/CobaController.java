package com.kitadigi.poskita.activities.coba;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CobaController implements ICobaRequest {

    private ICobaResult iCobaResult;
    private CobaInterface cobaInterface;
    Context context;
    ProgressDialog progressDialog;

    public CobaController(ICobaResult iCobaResult, Context context){
        this.iCobaResult = iCobaResult;
        this.context=context;
    }

    @Override
    public void getUser() {
        progressDialog=new ProgressDialog(context);
        progressDialog.show();

        cobaInterface=CobaUtil.getInterface();

        HashMap<String, String> params = new HashMap<>();
        params.put("param", "2");


        cobaInterface.getUser(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CobaModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                iCobaResult.onError(e.getMessage());
            }

            @Override
            public void onNext(CobaModel cobaModel) {
                progressDialog.dismiss();
                iCobaResult.onSuccess(cobaModel);
            }
        }) ;

    }
}
