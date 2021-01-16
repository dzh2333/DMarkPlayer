package com.mark.cyberpunkplayer.event;

import android.bluetooth.BluetoothDevice;

public class BTEvent {

    public static final String BT_OPEN = "bt_open";
    public static final String BT_CONNECT = "bt_connect";
    public static final String BT_SCAN = "bt_scan";
    public static final String BT_SCAN_RES = "bt_scan_res";
    public static final String BT_SCAN_FINISH = "bt_scan_finish";
    public static final String BT_STATUS_CHANGED = "bt_status_changed";

    public static final String BT_SENDER = "bt_sender";
    public static final String BT_GETER = "bt_geter";
    public static final String BT_SELECT_DEVICE = "bt_select_device";

    public static final String BT_SELECT_FILE = "bt_select_file";

    public BTEvent(String type, BluetoothDevice device, String extraData) {
        this.type = type;
        this.device = device;
        this.extraData = extraData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public String toString() {
        return "BTEvent{" +
                "type='" + type + '\'' +
                ", device=" + device +
                ", extraData='" + extraData + '\'' +
                '}';
    }

    private String type;
    private BluetoothDevice device;
    private String extraData;

}
