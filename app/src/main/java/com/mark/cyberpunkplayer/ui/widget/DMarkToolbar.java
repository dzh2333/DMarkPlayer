package com.mark.cyberpunkplayer.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.AppManager;
import com.mark.cyberpunkplayer.util.LogUtils;

public class DMarkToolbar extends LinearLayout {

    private ImageView backButton;
    private TextView title;

    public DMarkToolbar(Context context) {
        super(context);
        initView(context);
    }

    public DMarkToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DMarkToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.include_toolbar, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        addView(view);

        backButton = view.findViewById(R.id.dmark_toolbar_back);
        title = view.findViewById(R.id.dmark_toolbar_title);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getActivity().finish();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTitle(String content){
        title.setText(content);
    }
}
