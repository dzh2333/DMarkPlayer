package com.mark.cyberpunkplayer.service.wifi;

import android.os.Environment;

import com.mark.cyberpunkplayer.util.ContentTypeUtil;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.orhanobut.logger.Logger;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.http.HTTPStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.EnumSet;

import static com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OPEN;

public class WifiHttpThread extends Thread implements HTTPRequestListener {

    public static final int LISTENER_PORT = 5533;
    private HTTPServerList httpServerList = new HTTPServerList();
    private String bindIP = null;


    public HTTPServerList getHttpServerList() {
        return httpServerList;
    }

    public String getBindIP() {
        return bindIP;
    }

    public void setBindIP(String bindIP) {
        this.bindIP = bindIP;
    }

    @Override
    public void run() {
        super.run();

        // 重试次数
        int retryCnt = 0;
        // 获取端口 2222
        int bindPort = LISTENER_PORT;
        HTTPServerList hsl = getHttpServerList();
        while (hsl.open(bindPort) == false) {
            retryCnt++;
            // 重试次数大于服务器重试次数时返回
            if (100 < retryCnt) {
                return;
            }
            bindPort = LISTENER_PORT + retryCnt;
        }
        hsl.addRequestListener(this);
        Logger.d("开始监听" + bindPort);
        hsl.start();
    }


    @Override
    public void httpRequestRecieved(HTTPRequest httpReq) {
        try {
            String uri = httpReq.getURI();
            Logger.d("接受到的URL为" + uri);

            try {
                uri = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            System.out.println("uri=====" + uri);

            // 获取文件的大小
            long contentLen = 0;
            // 获取文件类型
            String contentType = ContentTypeUtil.getType(uri);
            InputStream contentIn = null;

            //读取文件
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + uri);
            Logger.d("接受到的URL为拼接为" + file.getPath());
            if (file.exists() && !file.isDirectory()){
                contentIn = new FileInputStream(file);
                contentLen = file.length();
            }else {
                HTTPResponse httpRes = new HTTPResponse();
                httpRes.setContentType(contentType);
                httpRes.setStatusCode(HTTPStatus.NOT_FOUND);
                //Socket还在运行
                httpReq.post(httpRes);
                Logger.d("拒绝了");
                return;
            }


            if (contentLen <= 0 || contentType.length() <= 0
                    || contentIn == null) {
                httpReq.returnBadRequest();
                return;
            }

            HTTPResponse httpRes = new HTTPResponse();
            httpRes.setContentType(contentType);
            httpRes.setStatusCode(HTTPStatus.OK);
            httpRes.setContentLength(contentLen);
            httpRes.setContentInputStream(contentIn);
            //Socket还在运行
            httpReq.post(httpRes);

//            httpReq.getSocket().close();

            LogUtils.d("create file over" + httpReq.getSocket().getSocket().isConnected() + "  "  +httpReq.getSocket().getSocket().isClosed());
        } catch (Exception e) {
            LogUtils.d("create file http " + e);
            return;
        }
    }

    public void removeAllListener(){
        getHttpServerList().close();
    }

}
