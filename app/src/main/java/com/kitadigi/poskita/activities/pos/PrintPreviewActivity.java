package com.kitadigi.poskita.activities.pos;

import java.io.UnsupportedEncodingException;

import com.kitadigi.poskita.MainActivity;
import com.kitadigi.poskita.base.BaseActivity;
import com.kitadigi.poskita.dao.printer.PrinterHelper;
import com.kitadigi.poskita.printer.BluetoothService;
import com.kitadigi.poskita.printer.DeviceListActivity;
import com.kitadigi.poskita.util.Constants;
import com.kitadigi.poskita.util.SessionManager;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.printer.PrinterCommand;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrintPreviewActivity extends BaseActivity {


    EditText etStruk;
    Button btnPrint,btnTutup;
    TextView tv_nav_header;
    ImageView iv_back;

    //intent untuk nerima intent struk
    Intent intent;

    // Debugging
    private static final String TAG = "Main_Activity";
    private static final boolean DEBUG = true;
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    //QRcode
    private static final int QR_WIDTH = 350;
    private static final int QR_HEIGHT = 350;
    /*******************************************************************************************************/
    private static final String CHINESE = "GBK";
    private static final String THAI = "CP874";
    private static final String KOREAN = "EUC-KR";
    private static final String BIG5 = "BIG5";


    /******************************************************************************************************/
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;

    /***************************   指                 令****************************************************************/
    final String[] items = { "复位打印机", "打印并走纸", "标准ASCII字体", "压缩ASCII字体", "正常大小",
            "二倍高倍宽", "三倍高倍宽", "四倍高倍宽", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
            "取消顺时针旋转90°", "选择顺时针旋转90°", "走纸到切刀位置并切纸", "蜂鸣指令", "标准钱箱指令",
            "实时弹钱箱指令", "进入字符模式", "进入中文模式", "打印自检页", "禁止按键", "取消禁止按键" ,
            "设置汉字字符下划线", "取消汉字字符下划线", "进入十六进制模式" };
    final String[] itemsen = { "Print Init", "Print and Paper", "Standard ASCII font", "Compressed ASCII font", "Normal size",
            "Double high power wide", "Twice as high power wide", "Three times the high-powered wide", "Off emphasized mode", "Choose bold mode", "Cancel inverted Print", "Invert selection Print", "Cancel black and white reverse display", "Choose black and white reverse display",
            "Cancel rotated clockwise 90 °", "Select the clockwise rotation of 90 °", "Feed paper Cut", "Beep", "Standard CashBox",
            "Open CashBox", "Char Mode", "Chinese Mode", "Print SelfTest", "DisEnable Button", "Enable Button" ,
            "Set Underline", "Cancel Underline", "Hex Mode" };
    final byte[][] byteCommands = {
            { 0x1b, 0x40, 0x0a },// 复位打印机
            { 0x0a }, //打印并走纸
            { 0x1b, 0x4d, 0x00 },// 标准ASCII字体
            { 0x1b, 0x4d, 0x01 },// 压缩ASCII字体
            { 0x1d, 0x21, 0x00 },// 字体不放大
            { 0x1d, 0x21, 0x11 },// 宽高加倍
            { 0x1d, 0x21, 0x22 },// 宽高加倍
            { 0x1d, 0x21, 0x33 },// 宽高加倍
            { 0x1b, 0x45, 0x00 },// 取消加粗模式
            { 0x1b, 0x45, 0x01 },// 选择加粗模式
            { 0x1b, 0x7b, 0x00 },// 取消倒置打印
            { 0x1b, 0x7b, 0x01 },// 选择倒置打印
            { 0x1d, 0x42, 0x00 },// 取消黑白反显
            { 0x1d, 0x42, 0x01 },// 选择黑白反显
            { 0x1b, 0x56, 0x00 },// 取消顺时针旋转90°
            { 0x1b, 0x56, 0x01 },// 选择顺时针旋转90°
            { 0x0a, 0x1d, 0x56, 0x42, 0x01, 0x0a },//切刀指令
            { 0x1b, 0x42, 0x03, 0x03 },//蜂鸣指令
            { 0x1b, 0x70, 0x00, 0x50, 0x50 },//钱箱指令
            { 0x10, 0x14, 0x00, 0x05, 0x05 },//实时弹钱箱指令
            { 0x1c, 0x2e },// 进入字符模式
            { 0x1c, 0x26 }, //进入中文模式
            { 0x1f, 0x11, 0x04 }, //打印自检页
            { 0x1b, 0x63, 0x35, 0x01 }, //禁止按键
            { 0x1b, 0x63, 0x35, 0x00 }, //取消禁止按键
            { 0x1b, 0x2d, 0x02, 0x1c, 0x2d, 0x02 }, //设置下划线
            { 0x1b, 0x2d, 0x00, 0x1c, 0x2d, 0x00 }, //取消下划线
            { 0x1f, 0x11, 0x03 }, //打印机进入16进制模式
    };
    /***************************条                          码***************************************************************/
    final String[] codebar = { "UPC_A", "UPC_E", "JAN13(EAN13)", "JAN8(EAN8)",
            "CODE39", "ITF", "CODABAR", "CODE93", "CODE128", "QR Code" };
    final byte[][] byteCodebar = {
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
            { 0x1b, 0x40 },// 复位打印机
    };
    /******************************************************************************************************/

    String struk;
    PrinterHelper printerHelper;
    String address,nama_printer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_preview);

        etStruk=(EditText) findViewById(R.id.etStruk);
        btnPrint=(Button)findViewById(R.id.btnPrint);
        btnTutup=(Button)findViewById(R.id.btnTutup);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_nav_header=(TextView)findViewById(R.id.tv_nav_header);




        //intent struk
        intent = getIntent();
        struk = intent.getStringExtra("struk");


        if (DEBUG)
            Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);



        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        //minta user untuk nyalakan BT
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        }

        //ambil address printer dari sqlite
        //====================================================================
        printerHelper=new PrinterHelper(PrintPreviewActivity.this);
        address = printerHelper.addressPrinter();
        nama_printer = printerHelper.namaPrinter();
        mConnectedDeviceName = nama_printer;

        konekPrinter();


        //tombol untuk scan dan langsung cetak
        btnPrint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBluetooth();
            }
        });

        //tombol untuk nutup
        btnTutup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pindahKeHome();
            }
        });

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pindahKeHome();
            }
        });

        //tamppilkan konten struk
        //di edittext
        etStruk.setText(struk);

        //apply font
        this.applyFontBoldToButton(btnPrint);
        this.applyFontBoldToTextView(tv_nav_header);
        this.applyFontRegularToEditText(etStruk);
    }

    private void SendDataString(String data) {

        data = struk;

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;

        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     *SendDataByte
     */
    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
//            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
//                    .show();
//            return;
            konekPrinter();

        }
        mService.write(data);
        mService.stop();
    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Print_Test();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            //mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);

                    break;
                case MESSAGE_TOAST:
//                    Toast.makeText(getApplicationContext(),
//                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
//                            .show();
//                    scanBluetooth();

//				Print_Test();//
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
//                    Toast.makeText(getApplicationContext(), "Unable to connect device",
//                            Toast.LENGTH_SHORT).show();
//                 scanBluetooth();
//                    onActivityResult(REQUEST_CONNECT_DEVICE,Activity.RESULT_OK,intent);

//				Print_Test();//
                    break;
            }


        }
    };

    //fungsi ini untuk print struk
    //yang string-nya berasal dari intent
    //dan disimpan ke var 'struk'
    private void Print_Test(){
        String lang = getString(R.string.strLang);
        if((lang.compareTo("en")) == 0){

            String data = struk;
            SendDataByte(PrinterCommand.POS_Print_Text(data, CHINESE, 0, 0, 0, 0));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }else if((lang.compareTo("cn")) == 0){
            String msg = "恭喜您!\n\n";
            String data = "您已经成功的连接上了我们的便携式蓝牙打印机！\n我们公司是一家专业从事研发，生产，销售商用票据打印机和条码扫描设备于一体的高科技企业.\n\n\n\n\n\n\n";
            SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 1, 1, 0));
            SendDataByte(PrinterCommand.POS_Print_Text(data, CHINESE, 0, 0, 0, 0));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }else if((lang.compareTo("hk")) == 0 ){
            String msg = "恭喜您!\n";
            String data = "您已經成功的連接上了我們的便攜式藍牙打印機！ \n我們公司是一家專業從事研發，生產，銷售商用票據打印機和條碼掃描設備於一體的高科技企業.\n\n\n\n\n\n\n";
            SendDataByte(PrinterCommand.POS_Print_Text(msg, BIG5, 0, 1, 1, 0));
            SendDataByte(PrinterCommand.POS_Print_Text(data, BIG5, 0, 0, 0, 0));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }else if((lang.compareTo("kor")) == 0){
            String msg = "축하 해요!\n";
            String data = "성공적으로 우리의 휴대용 블루투스 프린터에 연결 한! \n우리는 하이테크 기업 중 하나에서 개발, 생산 및 상업 영수증 프린터와 바코드 스캐닝 장비 판매 전문 회사입니다.\n\n\n\n\n\n\n";
            SendDataByte(PrinterCommand.POS_Print_Text(msg, KOREAN, 0, 1, 1, 0));
            SendDataByte(PrinterCommand.POS_Print_Text(data, KOREAN, 0, 0, 0, 0));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }else if((lang.compareTo("thai")) == 0){
            String msg = "ขอแสดงความยินดี!\n";
            String data = "คุณได้เชื่อมต่อกับบลูทู ธ เครื่องพิมพ์แบบพกพาของเรา! \n เราเป็น บริษัท ที่มีความเชี่ยวชาญในการพัฒนา, การผลิตและการขายของเครื่องพิมพ์ใบเสร็จรับเงินและการสแกนบาร์โค้ดอุปกรณ์เชิงพาณิชย์ในหนึ่งในองค์กรที่มีเทคโนโลยีสูง.\n\n\n\n\n\n\n";
            SendDataByte(PrinterCommand.POS_Print_Text(msg, THAI, 255, 1, 1, 0));
            SendDataByte(PrinterCommand.POS_Print_Text(data, THAI, 255, 0, 0, 0));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:{
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            Constants.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mService.connect(device);
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BT:{
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case REQUEST_CHOSE_BMP:{

                break;
            }
            case REQUEST_CAMER:{

                break;
            }
        }
    }


    private void KeyListenerInit() {

        mService = new BluetoothService(this, mHandler);
    }

    private void scanBluetooth(){
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        }
    }

    void konekPrinter(){


        if (mService == null)
            KeyListenerInit();//监听听


        // Get the BLuetoothDevice object
        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            BluetoothDevice device = mBluetoothAdapter
                    .getRemoteDevice(address);
            // Attempt to connect to the device
            if (mService.getState()==BluetoothService.STATE_CONNECTED){

            }else{
                mService.connect(device);
            }

        }
        //====================================================================

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        pindahKeHome();
    }

    void pindahKeHome(){

        //untuk jaga-jaga
        //hapus semua item penjualan
        SessionManager sessionManager = new SessionManager(PrintPreviewActivity.this);
        sessionManager.clearPenjualanOffline();


        //pindah ke home
        Intent homeActivity = new Intent(PrintPreviewActivity.this, MainActivity.class);
        homeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeActivity);
        this.finish();

    }


}
