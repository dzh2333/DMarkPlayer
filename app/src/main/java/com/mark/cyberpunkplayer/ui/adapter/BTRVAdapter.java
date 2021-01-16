package com.mark.cyberpunkplayer.ui.adapter;

import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.local.BTBean;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.orhanobut.logger.Logger;

import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothDevice.BOND_BONDING;

/**
 * 选择配对设备的RV适配器
 */
public class BTRVAdapter extends BaseRecyclerViewAdaper<BTBean> {

    public BTRVAdapter(Context context, List<BTBean> list){
        mContext = context;
        mData = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_loading, parent, false));
            case TYPE_NORMAL:
                return new NothingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bt_nothing, parent, false));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bt, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonViewHolder){
            CommonViewHolder holder1 = (CommonViewHolder) holder;
            final BTBean btBean = mData.get(position);

            try {
                Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                isConnectedMethod.setAccessible(true);
                boolean isConnected = (boolean) isConnectedMethod.invoke(btBean.getDevice(), (Object[]) null);

                Logger.d("connected status :" + btBean.getDevice().getName() + "  " +  isConnected);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (btBean.getStatus() == BTBean.NEARBY_DEVICE){
                holder1.address.setText(btBean.getDevice().getAddress() + "(附近设备)");
            }else {
                holder1.address.setText(btBean.getDevice().getAddress() + "(配对过的设备)");
            }

            holder1.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new BTEvent(BTEvent.BT_SELECT_DEVICE, btBean.getDevice(), ""));
                }
            });

            if (btBean.getDevice().getName() == null || btBean.getDevice().getName().equals("")){
                holder1.title.setText(mContext.getResources().getString(R.string.unknown));
            }else {
                holder1.title.setText(btBean.getDevice().getName());
            }

        }else if (holder instanceof NothingViewHolder){
            NothingViewHolder holder1 = (NothingViewHolder) holder;
            holder1.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new BTEvent(BTEvent.BT_OPEN, null, ""));
                }
            });
        }
    }

    public void insertItem(BTBean btBean){
        dataLoading = false;
        mData.add(btBean);
        notifyItemInserted(mData.size());
    }

    public void reSetAdapter(){
        mData = new ArrayList<>();
        dataLoading = true;
        notifyDataSetChanged();
    }

    private class CommonViewHolder extends BaseViewHolder{
        RelativeLayout layout;
        TextView title;
        TextView address;
        public CommonViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_bt_layout);
            title = itemView.findViewById(R.id.item_bt_name);
            address = itemView.findViewById(R.id.item_bt_address);
        }
    }

    private class NothingViewHolder extends BaseViewHolder{
        Button button;
        public NothingViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.item_bt_nothing_reconnect);
        }
    }

    private class LoadingViewHolder extends BaseViewHolder{
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

}
