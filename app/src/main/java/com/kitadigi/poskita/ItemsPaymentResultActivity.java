package com.kitadigi.poskita;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitadigi.poskita.activities.pos.PrintPreviewActivity;
import com.kitadigi.poskita.activities.pos.addtransaction.AddTransactionController;
import com.kitadigi.poskita.activities.pos.addtransaction.IAddTransactionResult;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.base.BaseResponse;
import com.kitadigi.poskita.dao.printer.PrinterHelper;
import com.kitadigi.poskita.dao.struk.StrukHelper;
import com.kitadigi.poskita.model.JualModel;
import com.kitadigi.poskita.model.ListJualModel;
import com.kitadigi.poskita.printer.PairedBTModel;
import com.kitadigi.poskita.printer.ScanActivity;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.PairedBluetooth;
import com.kitadigi.poskita.util.SessionManager;
import com.kitadigi.poskita.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemsPaymentResultActivity extends BaseActivity implements IAddTransactionResult {


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

    //hash untuk mendapatkan user,email toko
    HashMap<String,String> hashToko;


    //init controller untuk simpan transaksi
    private AddTransactionController addTransactionController;

    //init session
    private SessionManager sessionManager;

    private static final String TAG = ItemsPaymentResultActivity.class.getName();
    Context context;

    /* init ui */
    TextView tv_nav_header, tv_remark_transaksi, tv_remark_change, tv_change;
    ImageView iv_back;
    Button btn_finish;

    /* typeface fonts */
    Typeface fonts, fontsBold, fontsItalic;

    String change;

    //variabel untuk menerima intent totalpenjualan dan uang yang dibayar pembeli
    Integer total_penjualan,dibayar;

    //untuk cek printer
    PrinterHelper printerHelper;

    //untuk simpan konten struk ke SQlite
    StrukHelper strukHelper;

    //var ini untuk mengetahui berapa jumlah bt yang sudah di-pair di hp user
    int pairedBT;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_results);
        context                     = this;



        //init utk ngecek apakah sudah ada printer yang
        //tersimpan
        printerHelper=new PrinterHelper(context);

        //persiapan bluetooth printer
        // take an instance of BluetoothAdapter - Bluetooth radio
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            this.showToast(getResources().getString(R.string.perangkat_tidak_mendukung_bluetooth));

        } else {


        }

        //minta user supaya menghidupkan bluetooth
        bluetoothOn();

        //init session
        sessionManager=new SessionManager(ItemsPaymentResultActivity.this);

        //get nama printer
        nama_printer=printerHelper.namaPrinter();

        //get user,email toko
        hashToko=sessionManager.getUserDetails();


        //persiapan untuk mencetak
        try {
            findBluetoothDevice();
            openBluetoothPrinter();
        }catch (Exception e){
            e.printStackTrace();
        }

        /* init textview */
        tv_nav_header               = findViewById(R.id.tv_nav_header);
        tv_remark_transaksi         = findViewById(R.id.tv_remark_transaksi);
        tv_remark_change            = findViewById(R.id.tv_remark_change);
        tv_change                   = findViewById(R.id.tv_change);

        /* init imageview */
        iv_back                     = findViewById(R.id.iv_back);

        /* Button */
        btn_finish                  = findViewById(R.id.btn_finish);

        /* set fonts */
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToTextView(tv_remark_transaksi);
        this.applyFontRegularToTextView(tv_remark_change);
        this.applyFontBoldToTextView(tv_change);
        this.applyFontBoldToButton(btn_finish);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent homeActivity = new Intent(ItemsPaymentResultActivity.this, MainActivity.class);
//                startActivity(homeActivity);
//                finish();
                save();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //simpan online
                save();

            }
        });

        //init untuk simpan konten struk ke sqlite
        strukHelper=new StrukHelper(ItemsPaymentResultActivity.this);


    }

    @Override
    public void onStart() {
        super.onStart();

        //dapatkan intent, untuk mendapatkan jumlah uang yang dibayar pembeli dan grand total
        Bundle extras                   = getIntent().getExtras();
        change                          = extras.getString("change");
        total_penjualan                 = extras.getInt("total_penjualan");
        dibayar                         = extras.getInt("dibayar");

        if(change.isEmpty()){
            tv_remark_change.setVisibility(View.GONE);
            tv_change.setVisibility(View.GONE);
        }else{
            DecimalFormat formatter         = new DecimalFormat("#,###,###");
            String price                    = formatter.format(Integer.parseInt(change));

            tv_change.setText(price);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    void save(){

        //init controller dulu
        addTransactionController=new AddTransactionController(ItemsPaymentResultActivity.this, this, true);

        //simpan online
        addTransactionController.addTransaction("",dibayar,total_penjualan);

    }

    @Override
    public void onAddTransactionSuccess(BaseResponse baseResponse) {

        //notifikasi kalau sudah disimpan
//        Toast.makeText(ItemsPaymentResultActivity.this,baseResponse.getMessage(),Toast.LENGTH_SHORT).show();



        //siapkan konten struk
        //untuk intent dan disimpan di sqlite
        final String struk = kontenStruk();

        //simpan struk di sqlite
        strukHelper.newStruk(struk);

        //munculkan dialog apakah akan mencetak struk
        new SweetAlertDialog(ItemsPaymentResultActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(ItemsPaymentResultActivity.this.getResources().getString(R.string.cetak_struk))
                .setContentText(ItemsPaymentResultActivity.this.getResources().getString(R.string.pastikan_printer_sudah_terhubung))
                .setConfirmText(ItemsPaymentResultActivity.this.getResources().getString(R.string.ya))
                .setCancelText(ItemsPaymentResultActivity.this.getResources().getString(R.string.tidak))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                        //jika user pilih untuk cetak struk
//                        try {
//                            //cek dulu apakah sudah ada nama printer di sqlite
//                            boolean sudahAda = printerHelper.adaPrinter();
//
//                            if (sudahAda){
//                                //jika sudah ada nama printer yg
//                                //tersimpan di sqlite
//                                printData();
//
//                                //sesudah penyimpanan online berhasil dilakukan
//                                //hapus penjualan offline
//                                sessionManager.clearPenjualanOffline();
//
//                                //pindah ke home
//                                pindahKeHome();
//
//                            }else{
//                                //jika belum ada nama printer
//                                //arahkan ke ScanActivity
//                                //sekalian bawa intent yaitu String berisi struk
//                                Intent intent = new Intent(ItemsPaymentResultActivity.this, ScanActivity.class);
//
//                                //sisipkan intent struk
//                                String struk = kontenStruk();
//                                intent.putExtra("struk",struk);
//                                Log.d("struk", struk);
//
//                                startActivity(intent);
//                                finish();
//                            }
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//


//                            cek dulu apakah sudah ada nama printer di sqlite

                            //cek apakah ada nama printer yg tersimpan di
                            //sqlite di tabel printer
                            boolean sudahAda = printerHelper.adaPrinter();

                            //cek apakah sudah ada device yg sudah di-pair di hp user
                        //dikasih try, jaga2 kalau user belum nyalain bluetooth
                        try {
                            PairedBluetooth pairedBluetooth = new PairedBluetooth(ItemsPaymentResultActivity.this);
                            pairedBT = pairedBluetooth.jumlahPairedBT();
                        }catch (Exception e){
                            pairedBT=0;
                        }


                            if (sudahAda){
                                //jika sudah ada nama printer yg
                                //tersimpan di sqlite
                                //dapatkan dulu mac address printer
                                String address = printerHelper.addressPrinter();

                                Intent intent = new Intent(ItemsPaymentResultActivity.this, PrintPreviewActivity.class);

                                //sisipkan intent struk

                                intent.putExtra(Constants.STRUK,struk);
                                intent.putExtra(Constants.EXTRA_DEVICE_ADDRESS,address);


                                startActivity(intent);

                                //sesudah penyimpanan online berhasil dilakukan
                                //hapus penjualan offline
                                sessionManager.clearPenjualanOffline();

                                finish();
                                //pindah ke home
//                                pindahKeHome();

                            }else{
                                //jika belum ada nama printer
                                //arahkan ke ScanActivity
                                //sekalian bawa intent yaitu String berisi struk
                                Intent intent = new Intent(ItemsPaymentResultActivity.this, ScanActivity.class);

                                //sisipkan intent struk
                                intent.putExtra(Constants.STRUK,struk);
                                Log.d("struk", struk);

                                //sesudah penyimpanan online berhasil dilakukan
                                //hapus penjualan offline
                                sessionManager.clearPenjualanOffline();

                                startActivity(intent);
                                finish();
                            }


                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        //sesudah penyimpanan online berhasil dilakukan
                        //hapus penjualan offline
                        sessionManager.clearPenjualanOffline();

                        //pindah ke home
                        pindahKeHome();
                    }
                })
                .show();

    }

    @Override
    public void onAddTransactionError(String error) {

        //siapkan konten struk
        //untuk intent dan disimpan di sqlite
        final String struk = kontenStruk();

        //simpan struk di sqlite
        strukHelper.newStruk(struk);

        //munculkan dialog apakah akan mencetak struk
        new SweetAlertDialog(ItemsPaymentResultActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(ItemsPaymentResultActivity.this.getResources().getString(R.string.cetak_struk))
                .setContentText(ItemsPaymentResultActivity.this.getResources().getString(R.string.pastikan_printer_sudah_terhubung))
                .setConfirmText(ItemsPaymentResultActivity.this.getResources().getString(R.string.ya))
                .setCancelText(ItemsPaymentResultActivity.this.getResources().getString(R.string.tidak))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                        //jika user pilih untuk cetak struk
//                        try {
//                            //cek dulu apakah sudah ada nama printer di sqlite
//                            boolean sudahAda = printerHelper.adaPrinter();
//
//                            if (sudahAda){
//                                //jika sudah ada nama printer yg
//                                //tersimpan di sqlite
//                                printData();
//
//                                //sesudah penyimpanan online berhasil dilakukan
//                                //hapus penjualan offline
//                                sessionManager.clearPenjualanOffline();
//
//                                //pindah ke home
//                                pindahKeHome();
//
//                            }else{
//                                //jika belum ada nama printer
//                                //arahkan ke ScanActivity
//                                //sekalian bawa intent yaitu String berisi struk
//                                Intent intent = new Intent(ItemsPaymentResultActivity.this, ScanActivity.class);
//
//                                //sisipkan intent struk
//                                String struk = kontenStruk();
//                                intent.putExtra("struk",struk);
//                                Log.d("struk", struk);
//
//                                startActivity(intent);
//                                finish();
//                            }
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//


//                            cek dulu apakah sudah ada nama printer di sqlite

                        //cek apakah ada nama printer yg tersimpan di
                        //sqlite di tabel printer
                        boolean sudahAda = printerHelper.adaPrinter();

                        //cek apakah sudah ada device yg sudah di-pair di hp user
                        //dikasih try, jaga2 kalau user belum nyalain bluetooth
                        try {
                            PairedBluetooth pairedBluetooth = new PairedBluetooth(ItemsPaymentResultActivity.this);
                            pairedBT = pairedBluetooth.jumlahPairedBT();
                        }catch (Exception e){
                            pairedBT=0;
                        }


                        if (sudahAda){
                            //jika sudah ada nama printer yg
                            //tersimpan di sqlite
                            //dapatkan dulu mac address printer
                            String address = printerHelper.addressPrinter();

                            Intent intent = new Intent(ItemsPaymentResultActivity.this, PrintPreviewActivity.class);

                            //sisipkan intent struk

                            intent.putExtra(Constants.STRUK,struk);
                            intent.putExtra(Constants.EXTRA_DEVICE_ADDRESS,address);


                            startActivity(intent);

                            //sesudah penyimpanan online berhasil dilakukan
                            //hapus penjualan offline
                            sessionManager.clearPenjualanOffline();

                            finish();

                        }else{
                            //jika belum ada nama printer
                            //arahkan ke ScanActivity
                            //sekalian bawa intent yaitu String berisi struk
                            Intent intent = new Intent(ItemsPaymentResultActivity.this, ScanActivity.class);

                            //sisipkan intent struk
                            intent.putExtra(Constants.STRUK,struk);
                            Log.d("struk", struk);

                            //sesudah penyimpanan online berhasil dilakukan
                            //hapus penjualan offline
                            sessionManager.clearPenjualanOffline();

                            startActivity(intent);
                            finish();
                        }


                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        //sesudah penyimpanan online berhasil dilakukan
                        //hapus penjualan offline
                        sessionManager.clearPenjualanOffline();

                        //pindah ke home
                        pindahKeHome();
                    }
                })
                .show();
    }

    void pindahKeHome(){

        //pindah ke home
        Intent homeActivity = new Intent(ItemsPaymentResultActivity.this, MainActivity.class);
        homeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeActivity);
        this.finish();

    }

    private void bluetoothOn(){

        try {
            if (!myBluetoothAdapter.isEnabled()) {
                Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
//            Toast.makeText(getApplicationContext(),getResources().getString(R.string.bluetooth_dihidupkan) ,
//                    Toast.LENGTH_LONG).show();
            }

            else{
//            Toast.makeText(getApplicationContext(),getResources().getString(R.string.bluetooth_sudah_dihidupkan),
//                    Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    private void cariNamaPrinter(){

        pairedDevices = myBluetoothAdapter.getBondedDevices();

        //tampilkan di listview
        //siapkan variabel
        List<PairedBTModel> pairedBTModels = new ArrayList<>();
        PairedBTModel pairedBTModel;

        if (pairedDevices.size()==0){

            Toast.makeText(ItemsPaymentResultActivity.this,getResources().getString(R.string.belum_tersambung_dengan_printer_bluetooth),Toast.LENGTH_LONG).show();
        }else{
            for(BluetoothDevice device : pairedDevices){
                pairedBTModel =new PairedBTModel(device.getName(),device.getAddress());
                pairedBTModels.add(pairedBTModel);
            }

            //ambil nilai yang pertama lalu simpan ke session
            pairedBTModel = pairedBTModels.get(0);

            //simpan nama printer ke session
            sessionManager.setPrinterName(pairedBTModel.getNama_bluetooth());
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

    private String kontenStruk(){

        String divider = "--------------------------------";

        //untuk tanggal sekarang
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String tanggal = dateFormat.format(date);

        //variabel untuk nampung looping item penjualan
        String nama_produk;
        Integer qty,harga,total;

        //variabel ini untuk menghitung total jumlah barang
        Integer counter_qty = 0;

        //ini adalah string khusus untuk struk
        String msg;
        msg = "";


        //cetak nama toko dan alamat
        msg += StringUtil.center(hashToko.get(sessionManager.KEY_NAME),32);
        msg+="\n";
        msg += StringUtil.center(hashToko.get(sessionManager.KEY_EMAIL),32);
        msg+="\n";
        msg += StringUtil.center(tanggal,32);
        msg+="\n";


        //cetak header struk
        msg += divider + "\n";
        msg += String.format(
                "%-8s %4s %8s %8s",
                getResources().getString(R.string.item),
                getResources().getString(R.string.qty),
                getResources().getString(R.string.harga),
                getResources().getString(R.string.total));
        msg+="\n";
        msg += divider + "\n";



        //cetak item
        //looping dari session
        ListJualModel listJualModel = sessionManager.getPenjualanOffline();
        for (JualModel jualModel: listJualModel.getJualModels()){

            //cek apakah nama produk, jumlah hurufnya lebih dari 8
            //untuk membatasi saja, karena lebar kertas struk terbatas
            if (jualModel.getName_product().toString().length()>8){
                nama_produk = jualModel.getName_product().toString().substring(0,7);
            }else{
                nama_produk = jualModel.getName_product();
            }


            qty = jualModel.getQty();
            harga = jualModel.getSell_price();
            total = qty * harga;

            msg += String.format("%-8s %4s %8s %8s", nama_produk, qty.toString(), harga.toString(),total.toString());
            msg+="\n";


            //looping qty
            counter_qty = counter_qty + qty;

        }

        //cetak divider di bawah list item
        msg += divider + "\n";

        //cetak grandtotal
        msg += String.format("%-8s %4s %8s %8s", getResources().getString(R.string.total),counter_qty.toString(),"        ",total_penjualan.toString());
        msg+="\n";

        //cetak dibayar
        msg += String.format("%-8s %4s %8s %8s", "Tunai ","    ","        ",dibayar.toString());
        msg+="\n";

        //cetak kembalian
        msg += String.format("%-8s %4s %8s %8s", "Kembali ","    ","        ",change);
        msg+="\n";

        //cetak divider sebelum ucapan terima kasih
        msg += divider + "\n";

        //cetak terima kasih ucapan
        msg+=StringUtil.center(getResources().getString(R.string.terima_kasih),32);
        msg+="\n";

        //cetak 'selamat belanja kembali'
        msg+=StringUtil.center(getResources().getString(R.string.selamat_belanja_kembali),32);

        //cetak 'POWERED BY POSKITA'
        msg+=StringUtil.center(getResources().getString(R.string.powered_by_poskita),32);

        //diberi escapa, spy kasih mudah nyobek struk
        msg+="\n";
        msg+="\n";
        msg+="\n";
        return msg;
    }

    void printData() throws IOException{

        try {

            String divider = "--------------------------------";

            //untuk tanggal sekarang
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            String tanggal = dateFormat.format(date);

            //variabel untuk nampung looping item penjualan
            String nama_produk;
            Integer qty,harga,total;

            //variabel ini untuk menghitung total jumlah barang
            Integer counter_qty = 0;

            //ini adalah string khusus untuk struk
            String msg;
            msg = "";


            //cetak nama toko dan alamat
            msg += StringUtil.center(hashToko.get(sessionManager.KEY_NAME),32);
            msg+="\n";
            msg += StringUtil.center(hashToko.get(sessionManager.KEY_EMAIL),32);
            msg+="\n";
            msg += StringUtil.center("Tgl. " + tanggal,32);
            msg+="\n";


            //cetak header struk
            msg += divider + "\n";
            msg += String.format("%-8s %4s %8s %8s", "Item", "Qty", "Harga","Total");
            msg+="\n";
            msg += divider + "\n";



            //cetak item
            //looping dari session
            ListJualModel listJualModel = sessionManager.getPenjualanOffline();
            for (JualModel jualModel: listJualModel.getJualModels()){

                //cek apakah nama produk, jumlah hurufnya lebih dari 8
                //untuk membatasi saja, karena lebar kertas struk terbatas
                if (jualModel.getName_product().toString().length()>8){
                    nama_produk = jualModel.getName_product().toString().substring(0,7);
                }else{
                    nama_produk = jualModel.getName_product();
                }


                qty = jualModel.getQty();
                harga = jualModel.getSell_price();
                total = qty * harga;

                msg += String.format("%-8s %4s %8s %8s", nama_produk, qty.toString(), harga.toString(),total.toString());
                msg+="\n";


                //looping qty
                counter_qty = counter_qty + qty;

            }

            //cetak divider di bawah list item
            msg += divider + "\n";

            //cetak grandtotal
            msg += String.format("%-8s %4s %8s %8s", "Total",counter_qty.toString(),"        ",total_penjualan.toString());
            msg+="\n";

            //cetak dibayar
            msg += String.format("%-8s %4s %8s %8s", "Dibayar ","    ","        ",dibayar.toString());
            msg+="\n";

            //cetak kembalian
            msg += String.format("%-8s %4s %8s %8s", "Kembali ","    ","        ",change);
            msg+="\n";

            //cetak msg ke printer bluetooth
            if (outputStream==null){
//                Toast.makeText(ItemsPaymentResultActivity.this,"Struk null",Toast.LENGTH_SHORT).show();
            }else {

                outputStream.write(msg.getBytes());
                Log.d("bill",msg);
                outputStream.flush();
                outputStream.close();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}


