package com.mark.cyberpunkplayer.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mark.cyberpunkplayer.bean.local.BTSelectedFileBean;
import com.mark.cyberpunkplayer.bt.BtBase;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BTManager extends BtBase {

    private static final String TAG = BTManager.class.getSimpleName();

    public static final int ROLE_NO = 0;
    public static final int ROLE_SENDER = 1;
    public static final int ROLE_RECIPIENT = 2;

    //你是接受者还是发送者
    private int youRole = ROLE_NO;

    private static BTManager instance;

    private BluetoothAdapter mBTAdapter;
    private BTBroadcastRceciver btBroadcastRceciver;

    private BluetoothServerSocket mBTServerSocket;
    private BluetoothSocket mBTSocket;

    static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //自定义

    private BTManager(){
        init();
    }

    public static BTManager getInstance(){
        if (instance == null){
            synchronized (BTManager.class){
                if (instance == null){
                    instance = new BTManager();
                }
            }
        }
        return instance;
    }

    private void init(){
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean btIsEnabled(){
        return mBTAdapter.isEnabled();
    }

    public void startScanDevice(){
        if (mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
        }
        mBTAdapter.startDiscovery();
    }

    public void stopScanDevice(){
        mBTAdapter.cancelDiscovery();
    }

    public Set<BluetoothDevice> getConnectedDevice(){
        return mBTAdapter.getBondedDevices();
    }

    public void registerBTBroadcastRceciver(Activity activity){
        if (btBroadcastRceciver == null){
            btBroadcastRceciver = new BTBroadcastRceciver();
            IntentFilter filter = new IntentFilter();
            //发现设备
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            //设备绑定状态改变
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            //蓝牙设备状态改变
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            //搜素完成
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            activity.registerReceiver(btBroadcastRceciver, filter);
        }
    }

    public void setRole(int role){
        this.youRole = role;
    }

    public int getRole(){
        return youRole;
    }

    /**
     * 蓝牙发送文件步骤：
     * 1、服务器端创建监听
     * 2、客户端连接
     * 3、服务器监听到客户端连接
     * 4、服务器发送文件
     *
     * 步骤1：
     */
    public void createServer(){
        close();
        try {
            mBTServerSocket = mBTAdapter.listenUsingRfcommWithServiceRecord(TAG, SPP_UUID);
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mBTSocket = mBTServerSocket.accept();
                        mBTServerSocket.close();
                        loopRead(mBTSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        close();
                    }
                }
            });
        } catch (IOException e) {
            Logger.d("创建BTSocket失败" + e);
            e.printStackTrace();
            close();
        }
    }

    /**
     * 客户端（接受者）连接服务器端
     * 步骤2
     * @param device
     */
    public void connectServer(BluetoothDevice device){
        try {
            mBTSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loopRead(mBTSocket);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件给其他用户
     */
    public void sendFileToOtherUser(List<BTSelectedFileBean> list){
        sendFileIndex = 0;
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            strList.add(list.get(i).getPath());
        }
        sendFileList(strList, true);
    }

    public void unRegisterBTBroadcastRceciver(Activity activity){
        if (btBroadcastRceciver != null){
            activity.unregisterReceiver(btBroadcastRceciver);
            btBroadcastRceciver = null;
        }
    }

    /**
     * 主动停止服务
     */
    public void stopServer(){
        Logger.d("发送了中断的请求X1");
        setListener(null);
        sendDisConnectMsg();
        close();
        try {
            if (mBTServerSocket != null){
                mBTServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class BTBroadcastRceciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    //获取搜索到设备的信息
                    Logger.d("device name: " + device.getName() + " address: " + device.getAddress());
                    //获取绑定状态
                    Logger.d("device bond state : " + device.getBondState());
                    EventBus.getDefault().post(new BTEvent(BTEvent.BT_SCAN_RES, device, ""));
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    Logger.d("BOND_STATE_CHANGED device name: " + device.getName() + " address: " + device.getAddress());
                    Logger.d("BOND_STATE_CHANGED device bond state : " + device.getBondState());
                    EventBus.getDefault().post(new BTEvent(BTEvent.BT_STATUS_CHANGED, device, ""));
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Logger.d("BOND_STATE_CHANGED Xdevice name: " + device.getName() + " address: " + device.getAddress());
                    Logger.d( "BOND_STATE_CHANGED Xdevice bond state : " + device.getBondState());
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Logger.d("bluetooth discovery finished");
                    EventBus.getDefault().post(new BTEvent(BTEvent.BT_SCAN_FINISH, null, ""));
                    break;
            }
        }
    }

}
