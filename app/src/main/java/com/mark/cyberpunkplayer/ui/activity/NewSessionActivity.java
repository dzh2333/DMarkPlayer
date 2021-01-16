package com.mark.cyberpunkplayer.ui.activity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseActivity;
import com.mark.cyberpunkplayer.db.SmbBean;
import com.mark.cyberpunkplayer.event.SmbSessionEvent;
import com.mark.cyberpunkplayer.ui.widget.DMarkToolbar;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.mark.cyberpunkplayer.util.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class NewSessionActivity extends BaseActivity {

    private TextInputEditText hostName;
    private TextInputEditText userName;
    private TextInputEditText password;
    private TextInputEditText markName;
    private Button commitButton;

    @Override
    protected void initView() {
        hostName = findViewById(R.id.new_session_ip_address);
        userName = findViewById(R.id.new_session_user_name);
        password = findViewById(R.id.new_session_user_password);
        markName = findViewById(R.id.new_session_markname);
        commitButton = findViewById(R.id.new_session_commit);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hostName.getText().toString().equals("") ||
                    userName.getText().toString().equals("")){
                    ToastUtils.showToast(getResources().getString(R.string.new_session_please_set_correct_format));
                    return;
                }
                SmbBean bean = new SmbBean();
                bean.setSessionId(System.currentTimeMillis());
                bean.setPassword(password.getText().toString());
                bean.setHostName(hostName.getText().toString());
                bean.setUserName(userName.getText().toString());
                bean.setPort(0);
                bean.setFolderPath("");
                bean.setMarkName(markName.getText().toString());
                bean.setDiskPath("");
                bean.setFileName("");
                bean.setShowLevel(0);
                bean.setIsDirectory(false);
                checkSessionVaild(bean);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_session;
    }

    private void checkSessionVaild(final SmbBean bean){
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                SMBClient client = new SMBClient();
                try (Connection connection = client.connect(bean.getHostName())) {
                    AuthenticationContext ac = new AuthenticationContext(bean.getUserName(),
                            bean.getPassword().toCharArray(),
                            "DOMAIN");
                    connection.authenticate(ac);
                    EventBus.getDefault().post(new SmbSessionEvent(SmbSessionEvent.SUCCESS_SESSION, bean));
                    if (!isDestroyed()){
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
