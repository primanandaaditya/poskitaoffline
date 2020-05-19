package com.kitadigi.poskita.printer;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.util.HashMap;
import java.util.Set;

import java.util.UUID;

public class PrinterActivity extends BaseActivity {

    TextView lblAttachPrinter,lblPrinterName, tvCetak, tv_nav_header;
    ImageView iv_back;
    EditText etInput;
    Button btnConnectPrinter,btnDisconnectPrint,btnPrintDicument;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    SessionManager sessionManager;
    String nama_printer;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    //hash untuk mendapatkan user,email toko
    HashMap<String,String> hashToko;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        //untuk mendapatkan nama printer yang sudah tersimpan
        sessionManager=new SessionManager(PrinterActivity.this);

        //get nama printer
        nama_printer=sessionManager.getPrinterName();

        //get user,email toko
        hashToko=sessionManager.getUserDetails();



        tvCetak=(TextView)findViewById(R.id.tvcetak);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);
        lblAttachPrinter=(TextView)findViewById(R.id.lblAttachPrinter);
        lblAttachPrinter.setText(getResources().getString(R.string.nama_printer_tersimpan) + " " + nama_printer);

        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lblPrinterName=(TextView)findViewById(R.id.lblPrinterName);
        etInput=(EditText)findViewById(R.id.etInput);
        btnConnectPrinter=(Button)findViewById(R.id.btnConnectPrinter);
        btnDisconnectPrint=(Button)findViewById(R.id.btnDisconnectPrinter);
        btnPrintDicument=(Button)findViewById(R.id.btnPrintDocument);

        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(lblAttachPrinter);
        this.applyFontBoldToButton(btnPrintDicument);

        try {
            findBluetoothDevice();
            openBluetoothPrinter();
        }catch (Exception e){
            e.printStackTrace();
        }

        btnConnectPrinter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    findBluetoothDevice();
                    openBluetoothPrinter();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });



        btnDisconnectPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    disconnectPrinter();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        });



        btnPrintDicument.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    printData();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        });

        String BILL = "";

        BILL = "                   XXXX MART    \n"
                + "                   XX.AA.BB.CC.     \n " +
                "                 NO 25 ABC ABCDE    \n" +
                "                  XXXXX YYYYYY      \n" +
                "                   MMM 590019091      \n";
        BILL = BILL
                + "-----------------------------------------------\n";


        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Total");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n ";

        BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
        BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";

        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";


        tvCetak.setText(BILL);
        btnPrintDicument.setEnabled(true);
//        etInput.setText(styledResultText);
    }

    void printData() throws IOException{

        try {
            String msg = "--------------------------------" + "\n";
            msg += StringUtil.center(hashToko.get(sessionManager.KEY_NAME),32);
            msg+="\n";
            msg += StringUtil.center(hashToko.get(sessionManager.KEY_EMAIL),32);
            msg+="\n";




            outputStream.write(msg.getBytes());
            Log.d("bill",msg);

            lblPrinterName.setText("Sedang mencetak...");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    void findBluetoothDevice(){

        try {
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter==null){
                lblPrinterName.setText("Tidak ada bluetooth");
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
                        lblPrinterName.setText("Printer sudah terpasang");
                        break;
                    }
                }

            }else {
            }
            lblPrinterName.setText("Belum ada printer");
        }catch (Exception e){
            Log.d("Printer bluetooth", e.getMessage().toString());
        }
    }

    void openBluetoothPrinter() throws IOException{
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
                                                lblPrinterName.setText(data);
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




    void disconnectPrinter() throws  IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblPrinterName.setText("Printer terputus...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
