package com.mark.cyberpunkplayer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.local.BTSelectedFileBean;
import com.mark.cyberpunkplayer.event.FileEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SelectedFileRVAdapter extends BaseRecyclerViewAdaper<BTSelectedFileBean> {

    public SelectedFileRVAdapter(Context context, List<BTSelectedFileBean> list){
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_file, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommonViewHolder holder1 = (CommonViewHolder) holder;
        final BTSelectedFileBean bean = mData.get(position);

        holder1.name.setText(bean.getFileName());

        holder1.layout.setClickable(true);
        holder1.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isDirectory()){
                    EventBus.getDefault().post(new FileEvent(FileEvent.OPEN_DIRECTORY, bean.getPath()));
                }else {
                    EventBus.getDefault().post(new FileEvent(FileEvent.SELECTED_FILE, bean.getPath()));
                }
            }
        });

        if (bean.isDirectory()){
            holder1.imageView.setImageResource(R.drawable.icon_folder);
        }else {
            holder1.imageView.setImageResource(R.drawable.ic_file);
        }
    }

    private class CommonViewHolder extends BaseViewHolder{
        TextView name;
        LinearLayout layout;
        ImageView imageView;
        public CommonViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_selected_file_name);
            layout = itemView.findViewById(R.id.item_selected_file_layout);
            imageView = itemView.findViewById(R.id.item_selected_file_icon);
        }
    }
}
