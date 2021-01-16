package com.mark.cyberpunkplayer.base.mvvm;

import android.util.Log;

import androidx.databinding.ViewDataBinding;

public class AbstractViewModel <T extends ViewDataBinding> implements BaseViewModel {

    public T mViewDataBinding;

    public AbstractViewModel(T viewDataBinding){
        this.mViewDataBinding = viewDataBinding;
    }

    @Override
    public void onDestory() {
        mViewDataBinding.unbind();
    }
}
