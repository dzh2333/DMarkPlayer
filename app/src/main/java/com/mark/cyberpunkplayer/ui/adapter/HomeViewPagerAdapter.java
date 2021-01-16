package com.mark.cyberpunkplayer.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mark.cyberpunkplayer.util.LogUtils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> datas;
    FragmentManager fragmentManager;

    public HomeViewPagerAdapter(FragmentManager fm){
        super(fm);
        this.fragmentManager = fm;
    }

    public void setData(ArrayList<Fragment> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentManager.beginTransaction().show(fragment).commit();
        LogUtils.d("实例化 " + fragment.getClass().getName());
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = datas.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
