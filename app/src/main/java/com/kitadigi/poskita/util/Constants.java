package com.kitadigi.poskita.util;

public class Constants {

    public static final String BUNDLE_BLUETOOTH_STATE = "com.printer.prima";

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

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String ADDRESS = "address";
    public static String STRUK = "struk";

    public static Integer STATUS_SUDAH_SYNC=1;
    public static Integer STATUS_BELUM_SYNC=0;

    public static String poskita = "poskita";
    public final static String auth_token = "auth_token";
    public final static String token_key = "085B7120909C0DB4FBC3C9C568975E6F";
    public final static String KODE_200 = "200";
    public final static String ICON_STOCK = "icon_stock.png";
    public final static String ICON_STOCK_PATH = "image/icon_stock.png";

    public static Integer maxInput = 100;
    public static Integer maxInput50 = 50;
    public static Integer randomString = 20;
/*********************************************************************************/

}
