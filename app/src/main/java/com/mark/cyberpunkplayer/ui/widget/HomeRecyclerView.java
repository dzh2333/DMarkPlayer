package com.mark.cyberpunkplayer.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.app.Constants;

public class HomeRecyclerView extends RecyclerView {

    public HomeRecyclerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public HomeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
    }


}
