package com.mark.cyberpunkplayer.base;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdaper<T> extends RecyclerView.Adapter {

    protected Context mContext;

    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_FOOTER = 1;
    protected static final int TYPE_NORMAL = 2;
    protected static final int TYPE_LOADING = 3;
    protected static final int TYPE_COMMON = 4;

    protected boolean hasHeaderView;
    protected boolean hasfooterView;

    protected boolean dataLoading = false;
    protected List<T> mData;
    protected String mTag;

    @Override
    public int getItemCount() {
        if (mData.size() == 0) {
            return 1;
        }
        if(!hasHeaderView && !hasfooterView){
            return mData.size();
        }else if(!hasHeaderView && hasfooterView){
            return mData.size() + 1;
        }else if(hasHeaderView && !hasfooterView){
            return mData.size() + 1;
        }else{
            return mData.size() + 2;
        }
    }

    public void insertItem(T bean){
        if (bean != null){
            mData.add(bean);
            notifyItemChanged(mData.size() - 1);
        }
    }

    public void clear(){
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public List<T> getDatas(){
        return mData;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataLoading){
            return TYPE_LOADING;
        }
        if (mData.size() == 0){
            return TYPE_NORMAL;
        }
        if (hasHeaderView) {
            if (position == 0) {
                return TYPE_HEADER;
            }
        }
        if (hasfooterView) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_COMMON;
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder{
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void changeListData(List<T> list){
        dataLoading = false;
        mData = list;
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading ){
        this.dataLoading = loading;
    }

    public boolean isHasHeaderView() {
        return hasHeaderView;
    }

    public void setHasHeaderView(boolean hasHeaderView) {
        this.hasHeaderView = hasHeaderView;
    }


    public boolean isHasfooterView() {
        return hasfooterView;
    }

    public void setHasfooterView(boolean hasfooterView) {
        this.hasfooterView = hasfooterView;
    }

}
