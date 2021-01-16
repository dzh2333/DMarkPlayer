package com.mark.cyberpunkplayer.ui.dialog;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.DrawableUtils;

import com.bumptech.glide.Glide;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.event.AppEvent;
import com.mark.cyberpunkplayer.util.DisplayUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_PIC_DELETE;
import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_DELETE;

public class PictureDialog extends Dialog {

    private ImageView imageView;
    private Button setBackGroup;
    private Button remove;
    private Button exit;

    private String path;

    public PictureDialog(@NonNull Context context) {
        super(context);
    }

    public PictureDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PictureDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pic);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    private void initView(){
        imageView = findViewById(R.id.dialog_pic_image);
        exit = findViewById(R.id.dialog_pic_exit);
        remove = findViewById(R.id.dialog_pic_delete);
        setBackGroup = findViewById(R.id.dialog_pic_setbackgroup);
        Glide.with(getContext()).load(path).into(imageView);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppEvent(HOME_PIC_DELETE, path));
                dismiss();
            }
        });

        setBackGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                wallpaperManager.suggestDesiredDimensions(DisplayUtils.getWidthPixels(),
                        DisplayUtils.getHeightPixels());
                try {
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }

    public void setPath(String path) {
        this.path = path;
    }


}
