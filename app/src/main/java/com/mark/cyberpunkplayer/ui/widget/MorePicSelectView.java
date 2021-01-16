package com.mark.cyberpunkplayer.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.event.AppEvent;

import org.greenrobot.eventbus.EventBus;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_PIC_MORE_CANCEL;
import static com.mark.cyberpunkplayer.event.AppConstants.HOME_PIC_MORE_DELETE;

public class MorePicSelectView extends FrameLayout {

    public MorePicSelectView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MorePicSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MorePicSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_more_pic, null);
        addView(view);

        Button deleteButton = view.findViewById(R.id.view_more_pic_delete);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppEvent(HOME_PIC_MORE_DELETE, ""));
            }
        });

        Button cancelButton = view.findViewById(R.id.view_more_pic_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppEvent(HOME_PIC_MORE_CANCEL, ""));
            }
        });
    }
}
