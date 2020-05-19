package com.kitadigi.poskita.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import com.kitadigi.poskita.R;

import java.util.Set;

public class PairedBluetooth {

    Context context;
    //init bluetooth printer
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    public PairedBluetooth(Context context) {
        this.context = context;
    }

    public int jumlahPairedBT(){

        int hasil = 0;

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
           Toast.makeText(context,context.getResources().getString(R.string.perangkat_tidak_mendukung_bluetooth),Toast.LENGTH_SHORT).show();
        } else {
            pairedDevices = myBluetoothAdapter.getBondedDevices();
            hasil = pairedDevices.size();
        }
        return hasil;

    }

}
