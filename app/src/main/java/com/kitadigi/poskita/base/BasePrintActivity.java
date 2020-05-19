package com.kitadigi.poskita.base;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.printer.PrinterHelper;
import com.kitadigi.poskita.printer.BTDeviceModel;
import com.kitadigi.poskita.printer.PairedBTModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class BasePrintActivity extends BaseActivity {



    //init bluetooth printer
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private static final int REQUEST_ENABLE_BT = 1;

    //init untuk cetak print
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    String nama_printer;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private BluetoothAdapter btAdapter;
    public String toastText = "";
    private BluetoothDevice remoteDevice;

    private List<BTDeviceModel> btDeviceModels ;
    BTDeviceModel btDeviceModel;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};

    //var ini untuk nampung intent String struk
    String struk;

    //var printer helper
    //untuk cek apakah sudah ada printer yg tersimpan di sqlite
    PrinterHelper printerHelper;

    protected static final int DISCOVERY_REQUEST = 1;



    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra,-1);
            int previousState = intent.getIntExtra(prevStateExtra,-1);
            String toastText = "";
            switch (state){

                case (BluetoothAdapter.STATE_TURNING_ON):
                {
                    toastText = getResources().getString(R.string.bluetooth_sedang_dihidupkan);
                    Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }



                case (BluetoothAdapter.STATE_TURNING_OFF):
                {
                    toastText=getResources().getString(R.string.bluetooth_sedang_dimatikan);
                    Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }

                case (BluetoothAdapter.STATE_ON):
                {
                    toastText=getResources().getString(R.string.bluetooth_dihidupkan);
                    Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }

                case (BluetoothAdapter.STATE_OFF):
                {
                    toastText=getResources().getString(R.string.bluetooth_dimatikan);
                    Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        inisialisasi();
        mintaPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == DISCOVERY_REQUEST){
//            Toast.makeText(ScanActivity.this, "Sedang melakukan discovery", Toast.LENGTH_SHORT).show();
            setupUI();
            findDevices();
        }

        if(requestCode == REQUEST_ENABLE_BT){
            if(myBluetoothAdapter.isEnabled()) {

                //jika bluetooth di-enable
                //maka cari nama printer
                cariNamaPrinter();
            } else {

                this.showToast(getResources().getString(R.string.izin_bluetooth_diperlukan_untuk_akses_printer));
            }
        }

    }

    public void mintaPermission(){
        //meminta request permission pada user
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    public void inisialisasi(){
        //init printerhelper
        printerHelper=new PrinterHelper(BasePrintActivity.this);

        //init adapter
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //isi var struk dengan intent
        Intent intent = getIntent();
        if (intent.getStringExtra("struk")==null){
            struk="";
        }else{
            struk = intent.getStringExtra("struk");
            Log.d("struk",struk);
        }


        setupUI();

        //siapkan recyclerview untuk list bluetooth
        btDeviceModels = new ArrayList<>();

    }


    private void findDevices() {

        String lastUsedRemoteDevice = getLastUsedRemoteDevice();
        if (lastUsedRemoteDevice != null){
            toastText = "Memeriksa device, bernama : " + lastUsedRemoteDevice;
            Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();

            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            for (BluetoothDevice pairedDevice : pairedDevices){

                if (pairedDevice.getAddress().equals(lastUsedRemoteDevice)){
                    toastText = "Found device : " + pairedDevice.getName() + "@" + lastUsedRemoteDevice;
                    Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    remoteDevice = pairedDevice;

                }
            }
        }



        if (remoteDevice == null){

//            toastText = "Starting discovery for remote devices...";
//            Toast.makeText(ScanActivity.this, toastText, Toast.LENGTH_SHORT).show();
            if (btAdapter.startDiscovery()){
                toastText = getResources().getString(R.string.memulai_scan);
                Toast.makeText(BasePrintActivity.this, toastText, Toast.LENGTH_SHORT).show();
                registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }

        }
    }

    private void setupUI(){

        btAdapter=BluetoothAdapter.getDefaultAdapter();

        if (btAdapter.isEnabled()){
            String address = btAdapter.getAddress();
            String name = btAdapter.getName();
            String statusText = getResources().getString(R.string.nama_bluetooth) + " : " + name;
//            statusUpdate.setText(statusText);
        }else{
//            connect.setVisibility(View.VISIBLE);
//            statusUpdate.setText(getResources().getString(R.string.bluetooth_sedang_dimatikan));
        }


    }


    public void scanBluetooth(){


        //mulai pencarian baru
        String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
        String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
        IntentFilter filter = new IntentFilter(scanModeChanged);
        registerReceiver(bluetoothState, filter);
        startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);

    }


    public void cetakStruk(){
        //sebelum print struk, pastikan
        //ada nama printer di sqlite dan juga sudah pair dengan HP user

        //siapkan daftar bluetooth yang di-pair hp user
        List<PairedBTModel> pairedBTModels =bluetoothPairedList();
        Integer jumlahPairedBT;

        if (pairedBTModels==null){
            jumlahPairedBT=0;
        }else{
            jumlahPairedBT = pairedBTModels.size();
        }


        if (printerHelper.adaPrinter()==false && jumlahPairedBT==0 ){
            //belum ada printer yg tersimpan di sqlite dan tidak ada yg di-pair
            this.showToast(getResources().getString(R.string.printer_bluetooth_belum_dipasangkan));
//            Toast.makeText(BasePrintActivity.this,getResources().getString(R.string.printer_bluetooth_belum_dipasangkan),Toast.LENGTH_SHORT).show();

        }else{
            //print struk
            try {
                printData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getLastUsedRemoteDevice(){

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS", null);
        return  result;

    }


    BroadcastReceiver discoveryResult = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            //fungsi dibawah untuk mencegah, device yang sama
            //muncul 2 kali atau lebih

            //jika belum ada device yang terdetect
            if (btDeviceModels.size()==0){
                btDeviceModel = new BTDeviceModel();
                btDeviceModel.setName(mDevice.getName());
                btDeviceModel.setAddress(mDevice.getAddress());
                btDeviceModel.setBONDBONDED(mDevice.getBondState());
                btDeviceModel.setBluetoothDevice(mDevice);
                btDeviceModels.add(btDeviceModel);

            }else{
                //jika sudah ada 1 device ter-detect
                //cek dulu apakah sudah ada di-list

                BTDeviceModel btDeviceModel = findUsingIterator(mDevice.getAddress(),btDeviceModels);

                //jika ternyata belum ada
                if (btDeviceModel==null){
                    btDeviceModel = new BTDeviceModel();
                    btDeviceModel.setName(mDevice.getName());
                    btDeviceModel.setAddress(mDevice.getAddress());
                    btDeviceModel.setBONDBONDED(mDevice.getBondState());
                    btDeviceModel.setBluetoothDevice(mDevice);
                    btDeviceModels.add(btDeviceModel);
                }

            }


//            rv_bt.notifyDataSetChanged();
//            tvJumlah.setText(getResources().getString(R.string.jumlah_bluetooth) + " " + String.valueOf(btDeviceModels.size()));
        }

    };


    private BTDeviceModel findUsingIterator(
            String addressDevice, List<BTDeviceModel> btDeviceModels) {
        Iterator<BTDeviceModel> iterator = btDeviceModels.iterator();
        while (iterator.hasNext()) {
            BTDeviceModel btDeviceModel = iterator.next();
            if (btDeviceModel.getAddress().equals(addressDevice)) {
                return btDeviceModel;
            }
        }
        return null;
    }



    //fungsi ini untuk cek bluetooth device list
    //yang sedang di-pair sama hp user
    private List<PairedBTModel> bluetoothPairedList(){

        BluetoothAdapter myBluetoothAdapter;
        Set<BluetoothDevice> pairedDevices;

        //persiapan bluetooth printer
        // take an instance of BluetoothAdapter - Bluetooth radio
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
//            this.showToast(getResources().getString(R.string.perangkat_tidak_mendukung_bluetooth));

        } else {


        }

        pairedDevices = myBluetoothAdapter.getBondedDevices();


        //siapkan result untuk fungsi
        List<PairedBTModel> pairedBTModels = new ArrayList<>();
        PairedBTModel pairedBTModel;

        //jika tidak ada bluetooth yang di-pair
        if (pairedDevices.size()==0){
            return null;

        }else{
            //jika ada bluetooth yang di-pair
            for(BluetoothDevice device : pairedDevices){
                pairedBTModel =new PairedBTModel(device.getName(),device.getAddress());
                pairedBTModels.add(pairedBTModel);
            }
            return pairedBTModels;
//            Toast.makeText(ItemsPaymentResultActivity.this,pairedBTModel.getNama_bluetooth(),Toast.LENGTH_SHORT).show();
        }

    }




    private void cariNamaPrinter(){

        pairedDevices = myBluetoothAdapter.getBondedDevices();

        //tampilkan di listview
        //siapkan variabel
        List<PairedBTModel> pairedBTModels = new ArrayList<>();
        PairedBTModel pairedBTModel;

        if (pairedDevices.size()==0){

//            Toast.makeText(ItemsPaymentResultActivity.this,getResources().getString(R.string.belum_tersambung_dengan_printer_bluetooth),Toast.LENGTH_LONG).show();
        }else{
            for(BluetoothDevice device : pairedDevices){
                pairedBTModel =new PairedBTModel(device.getName(),device.getAddress());
                pairedBTModels.add(pairedBTModel);
            }

            //ambil nilai yang pertama lalu simpan ke session
            pairedBTModel = pairedBTModels.get(0);

            //simpan nama printer ke session
//            sessionManager.setPrinterName(pairedBTModel.getNama_bluetooth());
            Log.d("printer n", pairedBTModel.getNama_bluetooth().toString());
//            Toast.makeText(ItemsPaymentResultActivity.this,pairedBTModel.getNama_bluetooth(),Toast.LENGTH_SHORT).show();
        }

    }

    void findBluetoothDevice(){

        try {
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter==null){

            }

            if (bluetoothAdapter.isEnabled()){
                Intent enableBT=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }

            Set<BluetoothDevice> pairedDevices=bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size()>0){
                for (BluetoothDevice pairedDevice:pairedDevices){
                    //Cek nama bluetooth printer kamu, misalnya namanya BTPF109F1A

                    if (pairedDevice.getName().equals(nama_printer)){
                        bluetoothDevice=pairedDevice;
//                        lblPrinterName.setText("Printer sudah terpasang");
                        break;
                    }
                }

            }else {
            }
//            lblPrinterName.setText("Belum ada printer");
        }catch (Exception e){
            Log.d("Printer bluetooth", e.getMessage().toString());
        }
    }

    void openBluetoothPrinter() throws IOException {
        try {
            UUID uuidString = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidString);
            bluetoothSocket.connect();
            inputStream=bluetoothSocket.getInputStream();
            outputStream=bluetoothSocket.getOutputStream();
            beginListenData();
        }catch (Exception e){
        }
    }

    void beginListenData(){

        try {

            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker=false;
            readBufferPosition=0;
            readBuffer=new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable >0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);
                                for (int i=0;i<byteAvailable;i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );


                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                lblPrinterName.setText(data);
                                            }
                                        });

                                    }else {
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }

                        }catch (Exception e){
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void printData() throws IOException{

        try {

            openBluetoothPrinter();

            //cetak msg ke printer bluetooth
            if (outputStream==null){
//                Toast.makeText(ItemsPaymentResultActivity.this,"Struk null",Toast.LENGTH_SHORT).show();
            }else {

                outputStream.write(struk.getBytes());
                Log.d("bill",struk);
                outputStream.flush();
                outputStream.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getStruk(){
        return struk;
    }

}
