package com.mark.cyberpunkplayer.bean.local;

import android.bluetooth.BluetoothDevice;

public class BTBean {

    public static final int NEARBY_DEVICE = 0;
    public static final int BONDED_DEVICE = 1;
    public static final int BONDED_BONDING = 2;
    public static final int BONDED_BONDED = 3;

    public BTBean(int status, BluetoothDevice device) {
        this.status = status;
        this.device = device;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "BTBean{" +
                "status=" + status +
                ", device=" + device +
                '}';
    }

    private int status;
    private BluetoothDevice device;

}
