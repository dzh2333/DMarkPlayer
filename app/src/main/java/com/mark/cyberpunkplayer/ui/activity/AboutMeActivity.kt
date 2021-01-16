package com.mark.cyberpunkplayer.ui.activity


import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Gravity
import com.mark.cyberpunkplayer.BuildConfig

import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.base.BaseActivity
import com.mark.cyberpunkplayer.util.LogUtils
import kotlinx.android.synthetic.main.activity_about_me.*

class AboutMeActivity : BaseActivity() {
    override fun initData(savedInstanceState: Bundle?) {
        verisonCode = StringBuffer("目前版本：")
        verisonCode!!.append(BuildConfig.VERSION_NAME)
        about_me_version.setText(verisonCode)
    }

    var verisonCode : StringBuffer ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about_me
    }
}
