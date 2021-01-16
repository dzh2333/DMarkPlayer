package com.mark.cyberpunkplayer.ui.fragment.bt;

import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.BTManager;
import com.mark.cyberpunkplayer.base.BaseFragment;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.mark.cyberpunkplayer.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import static com.mark.cyberpunkplayer.event.BTEvent.BT_GETER;
import static com.mark.cyberpunkplayer.event.BTEvent.BT_SENDER;

public class BTIndexFragment extends BaseFragment {


    private MaterialCheckBox senderBox;
    private MaterialCheckBox getBox;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bt_index;
    }

    @Override
    protected void initView() {
        senderBox = mView.findViewById(R.id.sender_checkbox);
        getBox = mView.findViewById(R.id.get_checkbox);

        senderBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!BTManager.getInstance().btIsEnabled()){
                    ToastUtils.showToast("请先开启蓝牙");
                    senderBox.setChecked(false);
                    return;
                }
                if (isChecked){
                    getBox.setChecked(false);
                    EventBus.getDefault().post(new BTEvent(BT_SENDER, null, ""));
                }
            }
        });

        getBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!BTManager.getInstance().btIsEnabled()){
                    ToastUtils.showToast("请先开启蓝牙");
                    getBox.setChecked(false);
                    return;
                }
                if (isChecked){
                    senderBox.setChecked(false);
                    EventBus.getDefault().post(new BTEvent(BT_GETER, null, ""));
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
