package com.mark.cyberpunkplayer.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.mark.cyberpunkplayer.base.mvvm.AbstractViewModel;
import com.mark.cyberpunkplayer.base.mvvm.AbstractViewModel;

public abstract class BaseMVVMActivity extends AppCompatActivity {

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

}
