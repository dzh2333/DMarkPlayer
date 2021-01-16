package com.mark.cyberpunkplayer.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.AppManager;
import com.mark.cyberpunkplayer.bean.local.ShowVideoPopwindowBean;
import com.mark.cyberpunkplayer.event.AppEvent;
import com.mark.cyberpunkplayer.ui.adapter.popwindow.PopwindowClickInterface;
import com.mark.cyberpunkplayer.ui.adapter.popwindow.VideoPopwindowAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_DELETE;

public class VideoPopWindow extends PopupWindow {

    private RecyclerView recyclerView;
    private VideoPopwindowAdapter videoPopwindowAdapter;
    private List<ShowVideoPopwindowBean> list;
    private Context mContext;

    public VideoPopWindow(Context context) {
        super(context);
    }

    public VideoPopWindow(Context context, String path) {
        super(context);
        this.mContext = context;
        initView(path);
    }

    public VideoPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(String path){
        list = new ArrayList<>();
        list.add(new ShowVideoPopwindowBean(0, "删除", R.drawable.delete, path, new PopwindowClickInterface() {
            @Override
            public void click(int id, String path) {
                File file = new File(path);
                if (file.exists() && !file.isDirectory()){
                    file.delete();
                }
                EventBus.getDefault().post(new AppEvent(HOME_VIDEO_DELETE, ""));
            }
        }));

//        list.add(new ShowVideoPopwindowBean(0, "移动", R.drawable.move, path, new PopwindowClickInterface() {
//            @Override
//            public void click(int id, String path) {
//
//            }
//        }));

//        list.add(new ShowVideoPopwindowBean(0, "分享", R.drawable.share, path, new PopwindowClickInterface() {
//            @Override
//            public void click(int id, String path) {
//
//            }
//        }));
//        setAnimationStyle(R.anim.popwindow_show_anim);

        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindow_local_video, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setOutsideTouchable(true);
        setFocusable(true);

        recyclerView = view.findViewById(R.id.popwindow_local_video_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        videoPopwindowAdapter = new VideoPopwindowAdapter(mContext, list);
        recyclerView.setAdapter(videoPopwindowAdapter);

        Window window = AppManager.getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.alpha = 0.7f;
        window.setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Window dialogWindow = AppManager.getActivity().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
    }
}
