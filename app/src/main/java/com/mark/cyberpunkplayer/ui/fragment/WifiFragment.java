package com.mark.cyberpunkplayer.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseFragment;
import com.mark.cyberpunkplayer.service.wifi.WifiHttpThread;
import com.mark.cyberpunkplayer.service.wifi.WifiService;
import com.mark.cyberpunkplayer.util.IPUtils;

public class WifiFragment extends BaseFragment {

    private Button controButton;
    private TextView shareDirectory;
    private TextView shareHost;
    private TextView status;

    private static final int SERVER_OFF = 0;
    private static final int SERVER_ON = 1;

    private int serverStatus = SERVER_OFF;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wifi;
    }

    @Override
    protected void initView() {
        controButton = (Button) mView.findViewById(R.id.server_button);
        shareDirectory  = (TextView) mView.findViewById(R.id.server_address);
        shareHost = (TextView) mView.findViewById(R.id.server_host);
        status = mView.findViewById(R.id.server_status);

        controButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (serverStatus){
                    case SERVER_ON:
                        status.setText(getResources().getString(R.string.wifi_server_status_no));
                        status.setTextColor(Color.RED);
                        controButton.setText(getResources().getString(R.string.open));
                        intent = new Intent(getContext(), WifiService.class);
                        getActivity().stopService(intent);
                        serverStatus = SERVER_OFF;
                        shareHost.setText("");
                        break;
                    case SERVER_OFF:
                        status.setText(getResources().getString(R.string.wifi_server_status_ing));
                        status.setTextColor(Color.GREEN);
                        controButton.setText(getResources().getString(R.string.close));
                        intent = new Intent(getContext(), WifiService.class);
                        getActivity().startService(intent);
                        serverStatus = SERVER_ON;
                        StringBuffer buffer = new StringBuffer("http://");
                        buffer.append(IPUtils.getLocalIpAddress(getActivity()));
                        buffer.append(":");
                        buffer.append(String.valueOf(WifiHttpThread.LISTENER_PORT));
                        shareHost.setText(buffer);
                        break;
                }
            }
        });



    }

    @Override
    protected void initData() {
        StringBuffer bf = new StringBuffer("存储位置：");
        bf.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        shareDirectory.setText(bf);
    }
}
