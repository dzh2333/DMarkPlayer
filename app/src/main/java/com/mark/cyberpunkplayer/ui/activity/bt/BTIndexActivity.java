package com.mark.cyberpunkplayer.ui.activity.bt;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.BTManager;
import com.mark.cyberpunkplayer.base.BaseActivity;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.mark.cyberpunkplayer.ui.fragment.bt.BTIndexFragment;
import com.mark.cyberpunkplayer.ui.fragment.bt.GetFileFragment;
import com.mark.cyberpunkplayer.ui.fragment.bt.SendFileBTFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.mark.cyberpunkplayer.event.BTEvent.BT_GETER;
import static com.mark.cyberpunkplayer.event.BTEvent.BT_SENDER;

public class BTIndexActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEvent(BTEvent event){
        switch (event.getType()){
            case BT_SENDER:
                showSenderFragment();
                break;
            case BT_GETER:
                showGeterFragment();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_bt_index_container, new BTIndexFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bt_index;
    }

    private void showSenderFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_bt_index_container, new SendFileBTFragment())
                .commitAllowingStateLoss();
    }

    private void showGeterFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_bt_index_container, new GetFileFragment())
                .commitAllowingStateLoss();
    }


}
