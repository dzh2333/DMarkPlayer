package com.mark.cyberpunkplayer.service.smb;

import android.os.FileUtils;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.msfscc.fileinformation.FileStandardInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.security.jce.JceSecurityProvider;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskEntry;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.hierynomus.smbj.utils.SmbFiles;
import com.mark.cyberpunkplayer.db.SmbBean;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.orhanobut.logger.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPServer;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.http.HTTPSocket;
import org.cybergarage.http.HTTPStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OPEN;

public class SmbEXThread extends Thread implements HTTPRequestListener {

    public static final String CONTENT_EXPORT_URI = "/smb";

    public void setBindIP(String bindIP) {
        this.bindIP = bindIP;
    }

    private HTTPServerList httpServerList = new HTTPServerList();

    // 默认的共享端口
    public static int HTTPPort = 2222;

    // 绑定的ip
    private String bindIP = null;

    public HTTPServerList getHttpServerList() {
        return httpServerList;
    }


    public int getHTTPPort() {
        return HTTPPort;
    }

    public void setHTTPPort(int HTTPPort) {
        this.HTTPPort = HTTPPort;
    }

    public String getBindIP() {
        return bindIP;
    }


    @Override
    public void run() {
        super.run();
        // 重试次数
        int retryCnt = 0;
        // 获取端口 2222
        int bindPort = getHTTPPort();
        HTTPServerList hsl = getHttpServerList();
        while (hsl.open(bindPort) == false) {
            retryCnt++;
            // 重试次数大于服务器重试次数时返回
            if (100 < retryCnt) {
                return;
            }
            setHTTPPort(bindPort + 1);
            bindPort = getHTTPPort();
        }
        hsl.addRequestListener(this);
        hsl.start();
    }

    public void removeAllListener(){
        getHttpServerList().close();
    }

    @Override
    public void httpRequestRecieved(HTTPRequest httpReq) {
        try {
            String uri = httpReq.getURI();
            Logger.d("接受到的URL为" + uri);

            if (uri.startsWith(CONTENT_EXPORT_URI) == false) {
                httpReq.returnBadRequest();
                System.out.println("uri*****  uri error ");
                return;
            }
            try {
                uri = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            System.out.println("uri=====" + uri);
            if (uri.length() < 6) {
                return;
            }
            // 截取文件的信息
            String filePaths = "smb://" + uri.substring(5);

            System.out.println("filePaths=" + filePaths);
            // 判断uri中是否包含参数
            int indexOf = filePaths.indexOf("&");

            if (indexOf != -1) {
                filePaths = filePaths.substring(0, indexOf);
            }

            // 获取文件的大小
            long contentLen = 0;
            // 获取文件类型
            String contentType = "video/mp4";
            InputStream contentIn = null;
//            String contentType = "application/octet-stream";

            SMBClient client = new SMBClient();
            SmbBean bean = SmbInfoManager.getInstance().getTmpBean();
            Connection connection = client.connect(bean.getHostName());
            AuthenticationContext ac = new AuthenticationContext(bean.getUserName(),
                    bean.getPassword().toCharArray(),
                    "DOMAIN");
            Session session = connection.authenticate(ac);
            Logger.d("打开的Bean 为" + bean.getFileName() + "  " + bean.getFolderPath());

            DiskShare share = (DiskShare) session.connectShare(bean.getDiskPath());
            //这里遍历了该磁盘内文件夹的txt文件
            FileAllInformation fileInfo = share.getFileInformation(bean.getFolderPath() + bean.getFileName());
            FileStandardInformation f = fileInfo.getStandardInformation();
            contentLen = f.getEndOfFile();
            File file = share.openFile(bean.getFolderPath() + bean.getFileName(),
                    EnumSet.of(AccessMask.FILE_READ_DATA), null, SMB2ShareAccess.ALL, FILE_OPEN, null);
            contentIn = file.getInputStream();

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

            httpReq.getSocket().close();

            LogUtils.d("create file over" + httpReq.getSocket().getSocket().isConnected() + "  "  +httpReq.getSocket().getSocket().isClosed());
        } catch (Exception e) {
            LogUtils.d("create file http " + e);
            return;
        }
    }
}
