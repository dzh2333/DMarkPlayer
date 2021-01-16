package com.mark.cyberpunkplayer.base;

import android.os.Bundle;
import android.os.Environment;
import android.transition.Explode;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            setContentView(getLayoutId());
            initView();
            initData(savedInstanceState);
        }
    }


}
