package com.kitadigi.poskita.activities.coba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kitadigi.poskita.R;

public class CobaActivity extends AppCompatActivity implements ICobaResult {

    CobaController cobaController;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              coba();
            }
        });
    }

    void coba(){
        cobaController=new CobaController(this,CobaActivity.this);
        cobaController.getUser();


    }
    @Override
    public void onSuccess(CobaModel cobaModel) {
      Toast.makeText(CobaActivity.this, cobaModel.getPage().toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(CobaActivity.this, cobaModel.getData().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(CobaActivity.this, "Error : " + error.toString(),Toast.LENGTH_SHORT).show();
    }
}
