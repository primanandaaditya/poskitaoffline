package com.kitadigi.poskita.printer;

import android.bluetooth.BluetoothDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BTDeviceModel {

    @SerializedName("bluetoothDevice")
    @Expose
    private BluetoothDevice bluetoothDevice;



    @SerializedName("Tipe")
    @Expose
    private String Tipe;

    @SerializedName("UUID")

    @Expose

    private String UUID;



    @SerializedName("Name")

    @Expose

    private String Name;

    @SerializedName("Address")

    @Expose

    private String Address;

    @SerializedName("EXTRA_NAME")

    @Expose

    private String eXTRANAME;

    @SerializedName("EXTRA_DEVICE")

    @Expose

    private String eXTRADEVICE;

    @SerializedName("EXTRA_PAIRING_KEY")

    @Expose

    private String eXTRAPAIRINGKEY;

    @SerializedName("EXTRA_RSSI")

    @Expose

    private String eXTRARSSI;

    @SerializedName("EXTRA_UUID")

    @Expose

    private String eXTRAUUID;

    @SerializedName("BOND_BONDED")

    @Expose

    private Integer bONDBONDED;

    @SerializedName("BOND_BONDING")

    @Expose

    private Integer bONDBONDING;

    @SerializedName("BOND_NONE")

    @Expose

    private Integer bONDNONE;

    @SerializedName("DEVICE_TYPE_CLASSIC")

    @Expose

    private Integer dEVICETYPECLASSIC;

    @SerializedName("DEVICE_TYPE_LE")

    @Expose

    private Integer dEVICETYPELE;

    @SerializedName("DEVICE_TYPE_DUAL")

    @Expose

    private Integer dEVICETYPEDUAL;





    public BluetoothDevice getBluetoothDevice() {

        return bluetoothDevice;

    }



    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {

        this.bluetoothDevice = bluetoothDevice;

    }





    public String getTipe() {

        return Tipe;

    }



    public void setTipe(String Tipe) {

        this.Tipe = Tipe;

    }



    public String getUUID() {

        return UUID;

    }



    public void setUUID(String UUID) {

        this.UUID = UUID;

    }











    public String getName() {

        return  Name;

    }



    public void setName(String Name) {

        this.Name = Name;

    }











    public String getAddress() {

        return  Address;

    }



    public void setAddress(String Address) {

        this.Address = Address;

    }







    public String getEXTRANAME() {

        return eXTRANAME;

    }



    public void setEXTRANAME(String eXTRANAME) {

        this.eXTRANAME = eXTRANAME;

    }



    public String getEXTRADEVICE() {

        return eXTRADEVICE;

    }



    public void setEXTRADEVICE(String eXTRADEVICE) {

        this.eXTRADEVICE = eXTRADEVICE;

    }



    public String getEXTRAPAIRINGKEY() {

        return eXTRAPAIRINGKEY;

    }



    public void setEXTRAPAIRINGKEY(String eXTRAPAIRINGKEY) {

        this.eXTRAPAIRINGKEY = eXTRAPAIRINGKEY;

    }



    public String getEXTRARSSI() {

        return eXTRARSSI;

    }



    public void setEXTRARSSI(String eXTRARSSI) {

        this.eXTRARSSI = eXTRARSSI;

    }



    public String getEXTRAUUID() {

        return eXTRAUUID;

    }



    public void setEXTRAUUID(String eXTRAUUID) {

        this.eXTRAUUID = eXTRAUUID;

    }



    public Integer getBONDBONDED() {

        return bONDBONDED;

    }



    public void setBONDBONDED(Integer bONDBONDED) {

        this.bONDBONDED = bONDBONDED;

    }



    public Integer getBONDBONDING() {

        return bONDBONDING;

    }



    public void setBONDBONDING(Integer bONDBONDING) {

        this.bONDBONDING = bONDBONDING;

    }



    public Integer getBONDNONE() {

        return bONDNONE;

    }



    public void setBONDNONE(Integer bONDNONE) {

        this.bONDNONE = bONDNONE;

    }



    public Integer getDEVICETYPECLASSIC() {

        return dEVICETYPECLASSIC;

    }



    public void setDEVICETYPECLASSIC(Integer dEVICETYPECLASSIC) {

        this.dEVICETYPECLASSIC = dEVICETYPECLASSIC;

    }



    public Integer getDEVICETYPELE() {

        return dEVICETYPELE;

    }



    public void setDEVICETYPELE(Integer dEVICETYPELE) {

        this.dEVICETYPELE = dEVICETYPELE;

    }



    public Integer getDEVICETYPEDUAL() {

        return dEVICETYPEDUAL;

    }



    public void setDEVICETYPEDUAL(Integer dEVICETYPEDUAL) {

        this.dEVICETYPEDUAL = dEVICETYPEDUAL;

    }



}
