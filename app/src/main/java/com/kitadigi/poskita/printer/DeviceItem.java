package com.kitadigi.poskita.printer;

public class DeviceItem {
    String Name;
    String Address;
    String flag;

    public DeviceItem(String name, String address, String flag) {
        Name = name;
        Address = address;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
