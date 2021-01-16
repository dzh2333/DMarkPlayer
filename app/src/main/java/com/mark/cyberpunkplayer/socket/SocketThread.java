package com.mark.cyberpunkplayer.socket;

import java.net.DatagramSocket;
import java.net.Socket;

public class SocketThread extends Thread{

    private Socket mSocket;



    public SocketThread(){
        init();
    }

    private void init(){
        mSocket = new Socket();
    }

    @Override
    public void run() {
        super.run();

    }
}
