package com.mark.cyberpunkplayer.ui.fragment.bt;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.BTManager;
import com.mark.cyberpunkplayer.base.BaseFragment;
import com.mark.cyberpunkplayer.bean.local.BTSelectedFileBean;
import com.mark.cyberpunkplayer.bt.BtBase;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.mark.cyberpunkplayer.ui.activity.bt.BtActivity;
import com.mark.cyberpunkplayer.ui.activity.SelectFileActivity;
import com.mark.cyberpunkplayer.ui.adapter.SelectedFileRVAdapter;
import com.mark.cyberpunkplayer.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.BTEvent.BT_SELECT_DEVICE;
import static com.mark.cyberpunkplayer.event.BTEvent.BT_SELECT_FILE;

public class SendFileBTFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private SelectedFileRVAdapter selectedFileRVAdapter;
    private List<BTSelectedFileBean> list;

    //接收文件的设备信息
    private BluetoothDevice sendDevice;

    private Button addButton;
    private Button commitButton;
    private Button setUserButton;
    private TextView userNameTextView;
    private TextView logTextView;

    private String log = "";
    private boolean sending = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BTEvent event){
        switch (event.getType()){
            case BT_SELECT_FILE:
                selectedFileRVAdapter.insertItem(new BTSelectedFileBean(event.getExtraData(), event.getExtraData(), false));
                break;
            case BT_SELECT_DEVICE:
                sendDevice = event.getDevice();
                StringBuffer buffer = new StringBuffer(log + "\n接收设备为：");
                buffer.append(sendDevice.getName());
                userNameTextView.setText(buffer);
                BTManager.getInstance().connectServer(sendDevice);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BTManager.getInstance().sendDisConnectMsg();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_identity;
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        recyclerView = mView.findViewById(R.id.fragment_select_identity_rv);
        addButton = mView.findViewById(R.id.fragment_select_identity_add_file);
        commitButton  = mView.findViewById(R.id.fragment_select_identity_commit);
        userNameTextView = mView.findViewById(R.id.fragment_select_device_name);
        setUserButton = mView.findViewById(R.id.fragment_select_device);
        logTextView = mView.findViewById(R.id.fragment_send_file_log);

        selectedFileRVAdapter = new SelectedFileRVAdapter(getActivity(), list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(selectedFileRVAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFileRVAdapter.getItemCount() >= 1){
                    ToastUtils.showToast("目前仅支持1个文件传输");
                }else {
                    startActivity(new Intent(getActivity(), SelectFileActivity.class));
                }
            }
        });

        setUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BTManager.getInstance().btIsEnabled()){
                    startActivity(new Intent(getActivity(), BtActivity.class));
                }else {
                    ToastUtils.showToast("请先打开蓝牙");
                }
            }
        });

        BTManager.getInstance().setListener(new BtBase.Listener() {
            @Override
            public void socketNotify(int state, Object obj) {
                switch (state){
                    case BtBase.Listener.MSG:
                        log = log + "\n" + obj;
                        logTextView.setText(log);
                        break;
                    case BtBase.Listener.SEND_FILE_FINISHED:
                        log = log + "\n" + "文件发送完毕";
                        logTextView.setText(log);
                        sending = false;
                        selectedFileRVAdapter.clear();
                        break;
                    default:
                        break;
                }
            }
        });
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sending){
                    ToastUtils.showToast("正在发送文件中");
                    return;
                }
                if (sendDevice == null){
                    ToastUtils.showToast("请选择你要发送给哪个设备");
                    return;
                }
                if (list.size() == 0){
                    ToastUtils.showToast("请添加你要发送的文件");
                    return;
                }
                if (BTManager.getInstance().isConnected(sendDevice)){
                    ToastUtils.showToast("开始发送文件");
                    sending = true;
                    log = "";
                    BTManager.getInstance().sendFileToOtherUser(selectedFileRVAdapter.getDatas());
                }else {
                    ToastUtils.showToast("连接已经断开,正在尝试重新连接");
                    BTManager.getInstance().connectServer(sendDevice);
                }
            }
        });
    }



    @Override
    protected void initData() {

    }
}
