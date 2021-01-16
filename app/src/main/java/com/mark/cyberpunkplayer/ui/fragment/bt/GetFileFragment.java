package com.mark.cyberpunkplayer.ui.fragment.bt;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.mark.cyberpunkplayer.event.BTMsgEvent;
import com.mark.cyberpunkplayer.ui.adapter.SelectedFileRVAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.haha.perflib.Main;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.bt.BtBase.FLAG_FILE;
import static com.mark.cyberpunkplayer.bt.BtBase.FLAG_MSG;

public class GetFileFragment extends BaseFragment {

    private TextView statusTextView;
    private TextView storageTextView;
    private TextView msgTextView;
    private TextView logTextView;


    private RecyclerView recyclerView;
    private SelectedFileRVAdapter selectedFileRVAdapter;
    private List<BTSelectedFileBean> list;
    private ProgressBar progressBar;

    //显示日志
    private String log = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BTMsgEvent event){
        switch (event.getType()){
            case BTMsgEvent.FILE_TRANSPORT_FINISH:
                selectedFileRVAdapter.insertItem(new BTSelectedFileBean(event.getMsg(), event.getMsg(), false));
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
        BTManager.getInstance().stopServer();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bt_geter;
    }

    @Override
    protected void initView() {
        statusTextView = mView.findViewById(R.id.fragment_geter_status);
        storageTextView = mView.findViewById(R.id.fragment_geter_storage_location);
        recyclerView = mView.findViewById(R.id.fragment_geter_rv);
        msgTextView = mView.findViewById(R.id.fragment_geter_msg_title);
        logTextView = mView.findViewById(R.id.fragment_geter_log);
        progressBar = mView.findViewById(R.id.fragment_geter_progressbar);

        list = new ArrayList<>();
        selectedFileRVAdapter = new SelectedFileRVAdapter(getActivity(), list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(selectedFileRVAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        StringBuffer bf = new StringBuffer("存储位置：");
        bf.append(BtBase.FILE_PATH);
        storageTextView.setText(bf);

        BTManager.getInstance().createServer();
        BTManager.getInstance().setListener(new BtBase.Listener() {
            @Override
            public void socketNotify(int state, Object obj) {
                Logger.d("接受者消息" + obj);
                switch (state){
                    case BtBase.Listener.MSG:
                        log = log + "\n" + obj;
                        msgTextView.setText(log);
                        break;
                    case BtBase.Listener.CONNECTED:
                        progressBar.setVisibility(View.GONE);
                        statusTextView.setText("发送者设备接入成功!");
                        log = log + "\n" + "连接成功";
                        msgTextView.setText(log);
                        break;
                    case BtBase.Listener.CLIENT_DISCONNECTED:
                        progressBar.setVisibility(View.VISIBLE);
                        statusTextView.setText("连接中断，等待设备连接");
                        log = log + "\n" + "发送者连接中断";
                        msgTextView.setText(log);
                        Logger.d("接受者消息  重启蓝牙服务器");
                        BTManager.getInstance().createServer();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
