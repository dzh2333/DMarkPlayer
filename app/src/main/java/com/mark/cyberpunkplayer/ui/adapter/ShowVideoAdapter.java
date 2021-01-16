package com.mark.cyberpunkplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.AppManager;
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.local.ShowVideoBean;
import com.mark.cyberpunkplayer.event.AppEvent;
import com.mark.cyberpunkplayer.event.PlayerEvent;
import com.mark.cyberpunkplayer.player.PlayerActivity;
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllVideoModel;
import com.mark.cyberpunkplayer.ui.widget.RoundImageView;
import com.mark.cyberpunkplayer.util.DataUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_SHOW_POPWINDOW;

public class ShowVideoAdapter extends BaseRecyclerViewAdaper<ShowVideoBean> {

    private int sort_mode = AllVideoModel.SORT_TIME;

    public ShowVideoAdapter(Context context, List<ShowVideoBean> list){
        this.mContext = context;
        mData = list;
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
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoViewHolder){
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.name.setText(mData.get(position).getName());
            Glide.with(mContext).load(mData.get(position).getPath()).into(videoViewHolder.image);

            videoViewHolder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AppEvent(HOME_VIDEO_SHOW_POPWINDOW, mData.get(position).getPath()));
                }
            });

            if (sort_mode == AllVideoModel.SORT_TIME){
                StringBuffer stringBuffer = new StringBuffer("create time :");
                stringBuffer.append(DataUtils.DateToString(new Date(mData.get(position).getTime())));
                videoViewHolder.time.setText(stringBuffer);
            }else {
                StringBuffer stringBuffer = new StringBuffer("size :");
                stringBuffer.append(mData.get(position).getSize() / 1024 / 1024).append("MB");
                videoViewHolder.time.setText(stringBuffer);
            }

            videoViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(AppManager.getActivity(), PlayerActivity.class);
                    intent.putExtra("play_url", mData.get(position).getPath());
                    intent.putExtra("width", mData.get(position).getWidth());
                    intent.putExtra("height", mData.get(position).getHeight());
                    mContext.startActivity(intent);
                    EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_START, "", position));
                }
            });
        }

    }

    public void changeListAndSort(int mode, List<ShowVideoBean> list){
        sort_mode = mode;
        changeListData(list);
    }

    private class VideoViewHolder extends BaseViewHolder{
        private RoundImageView image;
        private TextView name;
        private TextView time;
        private ImageView menu;
        private ConstraintLayout layout;
        public VideoViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_video_image);
            name = itemView.findViewById(R.id.item_video_name);
            time = itemView.findViewById(R.id.item_video_time);
            menu = itemView.findViewById(R.id.item_video_menu);
            layout = itemView.findViewById(R.id.item_video_layout);
        }
    }

    private class LoadingViewHolder extends BaseViewHolder{
        private LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NothingViewHolder extends BaseViewHolder{
        private NothingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
