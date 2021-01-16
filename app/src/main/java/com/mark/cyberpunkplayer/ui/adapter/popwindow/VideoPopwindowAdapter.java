package com.mark.cyberpunkplayer.ui.adapter.popwindow;

import android.content.Context;
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
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.local.ShowVideoPopwindowBean;

import java.util.List;

public class VideoPopwindowAdapter extends BaseRecyclerViewAdaper<ShowVideoPopwindowBean> {

    public VideoPopwindowAdapter(Context context, List<ShowVideoPopwindowBean> list){
        mData = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popwinodw_item_local_video, parent, false);
        return new PopwindowVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PopwindowVideoHolder popwindowVideoHolder = (PopwindowVideoHolder) holder;
        Glide.with(mContext).load(mData.get(position).getRseId()).into(popwindowVideoHolder.imageView);
        popwindowVideoHolder.textView.setText(mData.get(position).getName());
        popwindowVideoHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(position).getClickInterface().click(mData.get(position).getId(), mData.get(position).getPath());
            }
        });
    }

    private class PopwindowVideoHolder extends BaseViewHolder{

        private ImageView imageView;
        private TextView textView;
        private ConstraintLayout constraintLayout;

        public PopwindowVideoHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.popwindow_item_video_icon);
            textView = itemView.findViewById(R.id.popwindow_item_video_name);
            constraintLayout = itemView.findViewById(R.id.popwindow_item_video_layout);
        }
    }
}
