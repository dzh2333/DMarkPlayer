package com.mark.cyberpunkplayer.bt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.text.TextUtils;

import com.google.android.datatransport.runtime.scheduling.persistence.EventStoreModule_SchemaVersionFactory;
import com.mark.cyberpunkplayer.event.BTMsgEvent;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.FileUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

public class BtBase {
    static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FBCCC"); //自定义
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dmarkbluetooth/";
    public static final int FLAG_MSG = 0;  //消息标记
    public static final int FLAG_FILE = 1; //文件标记
    public static final int CONNECT_SUCCESS = 2; //连接成功标记
    public static final int DISCONNECT_TO_DEVICE = 3; //断开连接标记

    private BluetoothSocket mSocket;
    private DataOutputStream mOut;
    private Listener mListener;
    private boolean isRead;
    private boolean isSending;

    private List<String> filePtahList;
    protected int sendFileIndex = 0;

    /**
     * 循环读取对方数据(若没有数据，则阻塞等待)
     */
    public void loopRead(BluetoothSocket socket) {
        mSocket = socket;
        try {
            if (!mSocket.isConnected()){
                mSocket.connect();
            }
            notifyUI(Listener.CONNECTED, mSocket.getRemoteDevice());
            mOut = new DataOutputStream(mSocket.getOutputStream());
            DataInputStream in = new DataInputStream(mSocket.getInputStream());
            isRead = true;
            while (isRead) { //死循环读取
                switch (in.readInt()) {
                    case FLAG_MSG: //读取短消息
                        String msg = in.readUTF();
                        notifyUI(Listener.MSG, "接收短消息：" + msg);
                        break;
                    case FLAG_FILE: //读取文件
                        FileUtil.mkdir(FILE_PATH);
                        String fileName = in.readUTF(); //文件名
                        long fileLen = in.readLong(); //文件长度
                        notifyUI(Listener.MSG, "正在接收文件(" + fileName + ")····················");
                        // 读取文件内容
                        long len = 0;
                        int r;
                        byte[] b = new byte[4 * 1024];
                        FileOutputStream out = new FileOutputStream(FILE_PATH + fileName);
                        while ((r = in.read(b)) != -1) {
                            out.write(b, 0, r);
                            len += r;
                            if (len >= fileLen)
                                break;
                        }
                        notifyUI(Listener.MSG, "文件接收完成(存放在:" + FILE_PATH + ")" + fileLen + "   " + fileName);
                        EventBus.getDefault().post(new BTMsgEvent(BTMsgEvent.FILE_TRANSPORT_FINISH, fileName));
                        break;
                    case CONNECT_SUCCESS:
                        notifyUI(Listener.CONNECTED, "连接成功");
                        break;
                    case DISCONNECT_TO_DEVICE:
                        Logger.d("触发1 蓝牙中断");
                        notifyUI(Listener.CLIENT_DISCONNECTED, "连接中断");
                        break;
                    default:
                        break;
                }
            }
        } catch (Throwable e) {
            Logger.d("异常X1" + e);
            e.printStackTrace();
            close();
        }
    }

    /**
     * 发送短消息
     */
    public void sendMsg(String msg) {
        if (isSending || TextUtils.isEmpty(msg))
            return;
        isSending = true;
        try {
            mOut.writeInt(FLAG_MSG); //消息标记
            mOut.writeUTF(msg);
        } catch (Throwable e) {
            close();
        }
        notifyUI(Listener.MSG, "发送短消息：" + msg);
        isSending = false;
    }

    /**
     * 发送连接成功消息
     */
    public void sendConnectSuccessMsg(){
        if (isSending)
            return;
        isSending = true;
        try {
            mOut.writeInt(CONNECT_SUCCESS); //消息标记
            mOut.writeUTF("connect success");
        } catch (Throwable e) {
            close();
        }
        notifyUI(Listener.MSG, "连接成功");
        isSending = false;
    }

    /**
     * 发送断开连接的消息给另一端
     */
    public void sendDisConnectMsg(){
        setListener(null);
        if (isSending)
            return;
        isSending = true;
        try {
            mOut.writeInt(DISCONNECT_TO_DEVICE); //断开连接
            mOut.writeUTF("device disconnect");
        } catch (Throwable e) {
            close();
        }
        notifyUI(Listener.MSG, "发送断开连接请求");
        isSending = false;
    }

    /**
     * 发送文件
     */
    protected void sendFile(final String filePath) {
        if (isSending || TextUtils.isEmpty(filePath))
            return;
        isSending = true;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    notifyUI(Listener.MSG, "正在发送文件(" + filePath + ")····················");
                    FileInputStream in = new FileInputStream(filePath);
                    File file = new File(filePath);
                    mOut.writeInt(FLAG_FILE); //文件标记
                    mOut.writeUTF(file.getName()); //文件名
                    mOut.writeLong(file.length()); //文件长度
                    int r;
                    byte[] b = new byte[4 * 1024];
                    while ((r = in.read(b)) != -1) {
                        mOut.write(b, 0, r);
                    }
                    notifyUI(Listener.MSG, "文件发送完成.");
                } catch (Throwable e) {
                    close();
                }
                isSending = false;
            }
        });
    }

    protected void sendFileList(final List<String> fileList, boolean outside){
        if (outside){
            sendFileIndex = 0;
            filePtahList = fileList;
        }
        if (isSending || fileList == null || fileList.size() == 0){
            Logger.d("发送结束  XX" + sendFileIndex   +  "  " + fileList.size());
            notifyUI(Listener.MSG, "正在发送文件");
            return;
        }
        if (sendFileIndex >= fileList.size()){
            Logger.d("发送结束" + sendFileIndex   +  "  " + fileList.size());
            notifyUI(Listener.SEND_FILE_FINISHED, "发送结束");
            isSending = false;
            return;
        }
        Logger.d("发送结束  XXAAA" + sendFileIndex);
        isSending = true;
        Logger.d("蓝牙设备  发送文件  mark 1");
        final String filePath = filePtahList.get(sendFileIndex);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    notifyUI(Listener.MSG, "正在发送文件(" + filePath + ")····················");
                    Logger.d("蓝牙设备  发送文件  mark 1   " + filePath);
                    FileInputStream in = new FileInputStream(filePath);
                    File file = new File(filePath);
                    mOut.writeInt(FLAG_FILE); //文件标记
                    mOut.writeUTF(file.getName()); //文件名
                    mOut.writeLong(file.length()); //文件长度
                    int r;
                    byte[] b = new byte[4 * 1024];
                    while ((r = in.read(b)) != -1) {
                        mOut.write(b, 0, r);
                    }
                    sendFileIndex += 1;
                    isSending = false;

                    notifyUI(Listener.MSG, "文件发送完成.");
                    Logger.d("消息标记1111");
                    Thread.sleep(500);

                    sendFileList(fileList, false);
                } catch (Throwable e) {
                    e.printStackTrace();
                    close();
                }
            }
        });
    }

    /**
     * 关闭Socket连接
     */
    public void close() {
        Logger.d("调用了CLose");
        try {
            isRead = false;
            mSocket.close();
            notifyUI(Listener.DISCONNECTED, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 当前设备与指定设备是否连接
     */
    public boolean isConnected(BluetoothDevice dev) {
        boolean connected = (mSocket != null && mSocket.isConnected());
        if (dev == null)
            return connected;
        return connected && mSocket.getRemoteDevice().equals(dev);
    }

    // ============================================通知UI===========================================================
    private void notifyUI(final int state, final Object obj) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mListener.socketNotify(state, obj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface Listener {
        int DISCONNECTED = 0;
        int CONNECTED = 1;
        int MSG = 2;
        int SEND_FILE_FINISHED = 3;
        int CLIENT_DISCONNECTED = 4;

        void socketNotify(int state, Object obj);
    }

    public void setListener(Listener listener){
        this.mListener = listener;
    }
}
