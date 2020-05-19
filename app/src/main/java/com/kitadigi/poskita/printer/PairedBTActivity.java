package com.kitadigi.poskita.printer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class PairedBTActivity extends BaseActivity {


    private static final int REQUEST_ENABLE_BT = 1;
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;
    private Button findBtn;
    private TextView text,tv_nav_header;
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private SessionManager sessionManager;


    ImageView iv_back;
    Switch sw_bluetooth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_bt);

        //session untuk nyimpan nama printer
        sessionManager=new SessionManager(PairedBTActivity.this);


        //teks keterangan
        text = (TextView) findViewById(R.id.text);
        text.setText(getResources().getString(R.string.nama_printer_tersimpan) + sessionManager.getPrinterName());

        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);

        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontBoldToTextView(text);

        //untuk nutup activity
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // take an instance of BluetoothAdapter - Bluetooth radio
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);
            findBtn.setEnabled(false);
            text.setText("Status: not supported");


            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();

        } else {

            text = (TextView) findViewById(R.id.text);

            listBtn = (Button)findViewById(R.id.paired);
            listBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    list(v);
                }
            });







            //switch untuk hidup mati Bluetooth
            sw_bluetooth=(Switch)findViewById(R.id.sw_bluetooth);
            sw_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        bluetoothOn();
                    }else{
                        bluetoothOff();
                    }
                }
            });

        }


    }




    private void bluetoothOn(){


        if (!myBluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.bluetooth_dihidupkan) ,
                    Toast.LENGTH_LONG).show();
        }

        else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.bluetooth_sudah_dihidupkan),
                    Toast.LENGTH_LONG).show();
        }

    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT){
            if(myBluetoothAdapter.isEnabled()) {
                text.setText(getResources().getString(R.string.bluetooth_dihidupkan));
            } else {
                text.setText(getResources().getString(R.string.bluetooth_dimatikan));
            }
        }

    }


    public void list(View view){

        pairedDevices = myBluetoothAdapter.getBondedDevices();

        //tampilkan di listview
        //siapkan variabel
        List<PairedBTModel> pairedBTModels = new ArrayList<>();
        PairedBTModel pairedBTModel;


        for(BluetoothDevice device : pairedDevices){
            pairedBTModel =new PairedBTModel(device.getName(),device.getAddress());
            pairedBTModels.add(pairedBTModel);
        }

        //siapkan adapter untuk listview
        PairedBTAdapter pairedBTAdapter=new PairedBTAdapter(pairedBTModels,PairedBTActivity.this);

        //set adapter di listview
        myListView = (ListView)findViewById(R.id.listView1);
        myListView.setAdapter(pairedBTAdapter);

        //jika diklik
        //akan disimpan di session manager
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PairedBTModel model=(PairedBTModel) parent.getAdapter().getItem(position);

                sessionManager.setPrinterName(model.getNama_bluetooth());
                Toast.makeText(PairedBTActivity.this, getResources().getString(R.string.nama_printer_tersimpan) + model.getNama_bluetooth(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            }
        }
    };


    private void bluetoothOff(){
        myBluetoothAdapter.disable();
        text.setText(getResources().getString(R.string.bluetooth_dimatikan));
        this.showToast(getResources().getString(R.string.bluetooth_dimatikan));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(bReceiver);
    }
}
