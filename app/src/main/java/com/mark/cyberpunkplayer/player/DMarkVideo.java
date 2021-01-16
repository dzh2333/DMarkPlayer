package com.mark.cyberpunkplayer.player;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.app.MainApplication;
import com.mark.cyberpunkplayer.event.PlayerEvent;
import com.mark.cyberpunkplayer.player.ui.LeftFragment;
import com.mark.cyberpunkplayer.player.ui.RightFragment;
import com.mark.cyberpunkplayer.service.smb.SmbEXThread;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.mark.cyberpunkplayer.util.PhoneLightUtil;
import com.mark.cyberpunkplayer.util.ToastUtils;
import com.mark.cyberpunkplayer.util.VolumeUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import static com.mark.cyberpunkplayer.app.AppManager.getActivity;

public class DMarkVideo extends ConstraintLayout {

    private static final int DMarkNV_SHOW = 0;
    private static final int DMarkNV_HIDE = 1;

    private LeftFragment leftFragment;
    private RightFragment rightFragment;

    private LinearLayout videoContainer;
    private DMarkVideoView dMarkVideoView;

    private ImageView playOrPause;
    private ImageView lastButton;
    private ImageView nextButton;
    private AppCompatSeekBar appCompatSeekBar;

    private View rootView;
    private ViewGroup nvViewGroup;

    private int nowNight = 0;

    private int mVideoWidth;
    private int mVideoHeight;
    private boolean smbFile;

    private volatile long clickTime;
    private volatile int nowShowStatus = DMarkNV_SHOW;

    private WeakReference<Context> reference;

    public DMarkVideo(Context context, boolean smbFile){
        super(context);
        this.smbFile = smbFile;
        reference = new WeakReference<>(context);
        initView(reference.get());
    }

    public DMarkVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DMarkVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context){
        rootView = LayoutInflater.from(context).inflate(R.layout.player_main_view, this);
        leftFragment = rootView.findViewById(R.id.player_home_play_left);
        rightFragment = rootView.findViewById(R.id.player_home_play_right);
        nvViewGroup = (ViewGroup) rootView.findViewById(R.id.player_home_play_nv);
        appCompatSeekBar = rootView.findViewById(R.id.player_home_play_seekbar);
        lastButton = rootView.findViewById(R.id.player_home_play_last);
        nextButton = rootView.findViewById(R.id.player_home_play_next);
        videoContainer = rootView.findViewById(R.id.player_home_videoview_container);

        nvViewGroup.setVisibility(View.VISIBLE);

        ContentResolver contentResolver = MainApplication.getInstance().getContentResolver();
        nowNight = Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, 125);

        appCompatSeekBar.setMax(1000);
        appCompatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (smbFile){
                    ToastUtils.showToast("未能调整进度，功能后续完善");
                    return;
                }

                DMarkVideoView.changeSeek((double)seekBar.getProgress()/(double)seekBar.getMax());
                playOrPause.setImageResource(R.drawable.player_pause);
            }
        });

        leftFragment.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        clickTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        LogUtils.d("移动改变的X为" + dx + "  改变的Y为" + dy );
                        if (dy >= 100){
                            nowNight = PhoneLightUtil.setLight(false, nowNight);
                        }else if (dy <= -100){
                            nowNight = PhoneLightUtil.setLight(true, nowNight);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        long nowTime = System.currentTimeMillis();
                        if ((nowTime - clickTime) <= 800){
                            showNotificationOrPause();
                        }
                        LogUtils.d("点击时间" + clickTime + "  " + nowTime + "  " + (nowTime - clickTime));
                        clickTime = nowTime;
                        break;
                }
                return true;
            }
        });

        rightFragment.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        LogUtils.d("移动改变的X为" + dx + "  改变的Y为" + dy );
                        if (dy >= 100){
                            VolumeUtil.reduceVolume();
                        }else if (dy <= -100){
                            VolumeUtil.addVolume();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        long nowTime = System.currentTimeMillis();
                        if ((nowTime - clickTime) <= 800){
                            showNotificationOrPause();
                        }
                        LogUtils.d("点击时间" + clickTime + "  " + nowTime + "  " + (nowTime - clickTime));
                        clickTime = nowTime;
                        break;
                }
                return true;
            }
        });
        initNV();
    }

    /**
     * 展示或者隐藏
     */
    private void showNotificationOrPause(){
        if (nowShowStatus == DMarkNV_SHOW){
            //隐藏起来
            nowShowStatus = DMarkNV_HIDE;
            nvViewGroup.setVisibility(View.INVISIBLE);
        }else {
            //展示出来
            nowShowStatus = DMarkNV_SHOW;
            nvViewGroup.setVisibility(View.VISIBLE);
        }
    }

    private void initNV(){
        playOrPause = rootView.findViewById(R.id.player_home_play_or_pause);
        playOrPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean videoIsPause = DMarkVideoView.onPauseOrRestartVideo();
                if (!videoIsPause){
                    playOrPause.setImageResource(R.drawable.player_pause);
                }else {
                    playOrPause.setImageResource(R.drawable.player_play);
                }
            }
        });
        lastButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_CHANGE_URL_LAST, "", 0));
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_CHANGE_URL_NEXT, "", 0));
            }
        });
    }

    public void setSeekProgress(double seek){
        appCompatSeekBar.setProgress((int) (seek*100));
    }

    /**
     * JNI调用改变Seek进度
     * @param seek
     */
    public static void changeSeek(double seek){
        if (seek == 0){
            return;
        }
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_CHANGE_SEEK, "", seek  * 10));
    }


    public static void setVideoViewLayoutParams( final int width){
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_CHANGE_WIDTH_HEIGHT, "", width));
    }

    public void setVideoViewLayout(final int width){
        videoContainer.removeView(dMarkVideoView);
        LogUtils.d("接受视频宽高" + width + " ");
        dMarkVideoView = new DMarkVideoView(getContext());
        LayoutParams layoutParams = new LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        dMarkVideoView.setLayoutParams(layoutParams);
        videoContainer.addView(dMarkVideoView);
    }

    public static void playOver(){
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_OVER, "", 0));
    }


    public void setURL(String url){

    }
}
