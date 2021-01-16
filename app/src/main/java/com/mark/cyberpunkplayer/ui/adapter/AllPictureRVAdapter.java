package com.mark.cyberpunkplayer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper;
import com.mark.cyberpunkplayer.bean.local.PictureBean;
import com.mark.cyberpunkplayer.event.AppEvent;
import com.mark.cyberpunkplayer.ui.dialog.PictureDialog;
import com.mark.cyberpunkplayer.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_PIC_MORE_SELECTED;

public class AllPictureRVAdapter extends BaseRecyclerViewAdaper<PictureBean> {

    public static final int PICTURE_MODE_COMMON = 0;
    public static final int PICTURE_MODE_SELECTED = 1;

    private int mode;

    private String sourceParentPath;
    private boolean rootPath;

    public AllPictureRVAdapter(Context context, List<PictureBean> list){
        this.mContext = context;
        mData = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (i){
            case TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_loading, parent, false));
            case TYPE_NORMAL:
                return new NothingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_nothing, parent, false));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof PicViewHolder){
            final PicViewHolder picViewHolder = (PicViewHolder) viewHolder;
            Glide.with(mContext).load(mData.get(i).getPath()).into(picViewHolder.imageView);
            final int position = i;

            if (mode == PICTURE_MODE_SELECTED) {
                if (!mData.get(position).isFocused()){
                    picViewHolder.selectBg.setVisibility(View.VISIBLE);
                    picViewHolder.selectCheckbox.setVisibility(View.VISIBLE);

                    mData.get(position).setFocused(false);
                    Glide.with(mContext).load(R.drawable.picture_no_focused).into(picViewHolder.selectBg);
                    Glide.with(mContext).load(R.drawable.check_box).into(picViewHolder.selectCheckbox);
                }else {
                    picViewHolder.selectBg.setVisibility(View.VISIBLE);
                    picViewHolder.selectCheckbox.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.check_box_focused).into(picViewHolder.selectCheckbox);
                    picViewHolder.selectBg.setImageResource(R.drawable.picture_focused);
                }
            }else {
                //还原为正常状态
                picViewHolder.selectCheckbox.setVisibility(View.GONE);
                picViewHolder.selectBg.setVisibility(View.GONE);
                mData.get(position).setFocused(false);
            }

            picViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == PICTURE_MODE_SELECTED){
                        if (mData.get(position).isFocused()){
                            picViewHolder.selectBg.setVisibility(View.VISIBLE);
                            picViewHolder.selectCheckbox.setVisibility(View.VISIBLE);

                            mData.get(position).setFocused(false);
                            Glide.with(mContext).load(R.drawable.picture_no_focused).override(100, 100).into(picViewHolder.selectBg);
                            Glide.with(mContext).load(R.drawable.check_box).into(picViewHolder.selectCheckbox);

                        }else {
                            mData.get(position).setFocused(true);
                            picViewHolder.selectBg.setVisibility(View.VISIBLE);
                            picViewHolder.selectCheckbox.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(R.drawable.check_box_focused).into(picViewHolder.selectCheckbox);
//                        Glide.with(context).load(R.drawable.picture_focused).into(picViewHolder.selectBg);
                            picViewHolder.selectBg.setImageResource(R.drawable.picture_focused);
                        }
                    }else {
                        //还原为正常状态
                        picViewHolder.selectCheckbox.setVisibility(View.GONE);
                        picViewHolder.selectBg.setVisibility(View.GONE);
                        mData.get(position).setFocused(false);

                        PictureDialog pictureDialog = new PictureDialog(mContext);
                        pictureDialog.setPath(mData.get(position).getPath());
                        pictureDialog.show();
                    }
                }
            });

            picViewHolder.layout.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if (mode == PICTURE_MODE_COMMON){
                        mData.get(position).setFocused(true);
                        setMode(PICTURE_MODE_SELECTED);
                        EventBus.getDefault().post(new AppEvent(HOME_PIC_MORE_SELECTED, ""));
                    }
                    return true;
                }
            });
        }
    }

    private class PicViewHolder extends BaseViewHolder{
        ImageView imageView;
        RelativeLayout layout;
        ImageView selectBg;
        ImageView selectCheckbox;
        public PicViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_rv_select_pic_layout);
            imageView = itemView.findViewById(R.id.item_rv_select_pic_imageview);
            selectBg = itemView.findViewById(R.id.item_rv_select_pic_select);
            selectCheckbox = itemView.findViewById(R.id.item_rv_select_pic_checkbox);
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

    public void setMode(int mode){
        this.mode = mode;
        notifyDataSetChanged();
    }

    public List<PictureBean> getFocusedItem(){
        List<PictureBean> res = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++){
            if (mData.get(i).isFocused()){
                res.add(mData.get(i));
            }
        }
        return res;
    }

    public List<PictureBean> getDataList(){
        return mData;
    }

    public void setDataList(List<PictureBean> list){
        mData = list;
        notifyDataSetChanged();
        initFocusedStatus();
    }

    public void initFocusedStatus(){
        for (int i = 0; i < mData.size(); i++){
            mData.get(i).setFocused(false);
        }
    }

    public String getSourceParentPath() {
        return sourceParentPath;
    }

    public void setSourceParentPath(String sourceParentPath) {
        this.sourceParentPath = sourceParentPath;
    }

    public boolean isRootPath() {
        return rootPath;
    }

    public void setRootPath(boolean rootPath) {
        this.rootPath = rootPath;
    }

}
