package com.mark.cyberpunkplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hierynomus.mssmb2.messages.SMB2SetInfoRequest;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.network.LocalNetResBean;
import com.mark.cyberpunkplayer.db.SmbBean;
import com.mark.cyberpunkplayer.event.SmbSessionEvent;
import com.mark.cyberpunkplayer.player.PlayerActivity;
import com.mark.cyberpunkplayer.service.smb.SmbInfoManager;
import com.mark.cyberpunkplayer.ui.dialog.PictureDialog;
import com.mark.cyberpunkplayer.util.FileCategoryUtils;
import com.mark.cyberpunkplayer.util.IPUtils;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.mark.cyberpunkplayer.util.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.SmbSessionEvent.DELETE_FILE;
import static com.mark.cyberpunkplayer.event.SmbSessionEvent.DELETE_SESSION;
import static com.mark.cyberpunkplayer.event.SmbSessionEvent.OPEN_DIRECTORY;
import static com.mark.cyberpunkplayer.event.SmbSessionEvent.OPEN_FILE;
import static com.mark.cyberpunkplayer.event.SmbSessionEvent.OPEN_SESSION;
import static com.mark.cyberpunkplayer.event.SmbSessionEvent.SUCCESS_SESSION;
import static com.mark.cyberpunkplayer.util.FileCategoryUtils.FILE_IS_AUDIO;
import static com.mark.cyberpunkplayer.util.FileCategoryUtils.FILE_IS_PIC;
import static com.mark.cyberpunkplayer.util.FileCategoryUtils.FILE_IS_VIDEO;

public class LocalNetRVAdapter extends BaseRecyclerViewAdaper<SmbBean> {

    public LocalNetRVAdapter(Context context, List<SmbBean> list){
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_loading, parent, false));
            case TYPE_NORMAL:
                return new NothingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_nothing, parent, false));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_net_res, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommonViewHolder){
            CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            final SmbBean smbBean = mData.get(position);
            switch (smbBean.getShowLevel()){
                case 0:
                    if (smbBean.getMarkName().equals("")){
                        StringBuffer bf = new StringBuffer("Session:");
                        bf.append(smbBean.getSessionId());
                        commonViewHolder.title.setText(bf);
                    }else {
                        commonViewHolder.title.setText(smbBean.getMarkName());
                    }
                    commonViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new SmbSessionEvent(DELETE_SESSION, mData.get(position)));
                        }
                    });
                    commonViewHolder.deleteButton.setImageResource(R.drawable.icon_delete);
                    break;
                case 1:
                    StringBuffer sb = new StringBuffer(smbBean.getDiskPath());
                    sb.append(":");
                    commonViewHolder.title.setText(sb);
                    commonViewHolder.deleteButton.setOnClickListener(null);
                    commonViewHolder.deleteButton.setImageBitmap(null);
                    break;
                case 2:
                    commonViewHolder.title.setText(String.valueOf(smbBean.getFileName()));
                    commonViewHolder.deleteButton.setOnClickListener(null);
                    commonViewHolder.deleteButton.setImageBitmap(null);
                    break;
                default:
                    break;
            }
            switch (smbBean.getShowLevel()){
                case 0:
                    commonViewHolder.icon.setImageResource(R.drawable.icon_pc);
                    break;
                case 1:
                    commonViewHolder.icon.setImageResource(R.drawable.icon_disk);
                    break;
                case 2:
                    if (smbBean.getIsDirectory()){
                        commonViewHolder.icon.setImageResource(R.drawable.icon_folder);
                    }else {
                        switch (FileCategoryUtils.getFileCategory(smbBean.getFileName())){
                            case FILE_IS_VIDEO:
                                commonViewHolder.icon.setImageResource(R.drawable.session_icon_video);
                                break;
                            case FILE_IS_AUDIO:
                                commonViewHolder.icon.setImageResource(R.drawable.ic_audio);
                                break;
                            case FILE_IS_PIC:
                                commonViewHolder.icon.setImageResource(R.drawable.ic_picture);
                                break;
                            default:
                                commonViewHolder.icon.setImageResource(R.drawable.ic_file);
                                break;
                        }
                    }
                    break;
            }

            commonViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (smbBean.getShowLevel()){
                        case 0:
                            EventBus.getDefault().post(new SmbSessionEvent(OPEN_SESSION, smbBean));
                            break;
                        default:
                            if (smbBean.getIsDirectory()){
                                EventBus.getDefault().post(new SmbSessionEvent(OPEN_DIRECTORY,
                                        mData.get(position)));
                            }else {
                                switch (FileCategoryUtils.getFileCategory(smbBean.getFileName())){
                                    case FILE_IS_VIDEO:
                                        StringBuffer url =new StringBuffer("http://" + IPUtils.getLocalIpAddress(mContext) + ":2222/smb/");
                                        url.append(smbBean.getDiskPath() + "/")
                                                .append((smbBean.getFolderPath()).replaceAll("\\\\", "/"))
                                                .append(smbBean.getFileName());
                                        SmbInfoManager.getInstance().setTmpBean(smbBean);
                                        Intent intent = new Intent(mContext, PlayerActivity.class);
                                        intent.putExtra("play_url", url.toString());
                                        intent.putExtra("smb_file", true);
                                        mContext.startActivity(intent);
                                        break;
                                    default:
                                        ToastUtils.showToast(mContext.getString(R.string.unkown_file));
                                        break;
                                }
                            }
                            break;
                    }
                }
            });


        }
    }

    public void dataloading(){
        mData = new ArrayList<>();
        dataLoading = true;
        notifyDataSetChanged();
    }

    private class CommonViewHolder extends BaseViewHolder{
        private ImageView icon;
        private TextView title;
        private LinearLayout layout;
        private ImageView deleteButton;
        public CommonViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_local_net_res_icon);
            title = itemView.findViewById(R.id.item_local_net_res_title);
            layout = itemView.findViewById(R.id.item_local_net_res_layout);
            deleteButton = itemView.findViewById(R.id.item_local_net_res_delete);
        }
    }

    private class LoadingViewHolder extends BaseViewHolder{
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NothingViewHolder extends BaseViewHolder{
        public NothingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
