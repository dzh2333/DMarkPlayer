package com.mark.cyberpunkplayer.player

import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.base.BaseMVVMActivity
import com.mark.cyberpunkplayer.databinding.PlayerHomeBinding
import com.mark.cyberpunkplayer.event.PlayerEvent
import com.mark.cyberpunkplayer.model.PlayerActivityViewModel
import com.mark.cyberpunkplayer.service.smb.SmbEXThread
import com.mark.cyberpunkplayer.util.LogUtils
import kotlinx.android.synthetic.main.player_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

public class PlayerActivity : BaseMVVMActivity(){

    var showStatus = false
    var playerActivityViewModel : PlayerActivityViewModel ?= null
    var mViewDataBinding : PlayerHomeBinding?= null

    private var mPlayerViewCallBack: PlayerViewCallBack? = null
    private var dMarkVideo : DMarkVideo ?= null

    override fun initData() {
    }

    override fun initView() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        playerActivityViewModel = PlayerActivityViewModel(mViewDataBinding)
    }


    override fun getLayoutId(): Int {
        return R.layout.player_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(PlayerActivity@this)

        val url = intent.getStringExtra("play_url")
        val smbFile = intent.getBooleanExtra("smb_file", false)

        dMarkVideo = DMarkVideo(PlayerActivity@this, smbFile)
        val layoutParams : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT)
        dMarkVideo!!.layoutParams = layoutParams
        video_container.addView(dMarkVideo)

        DMarkVideoView.openUrl(url, dMarkVideo, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(player : PlayerEvent){
        when(player.type){
            PlayerEvent.PLAY_START->{
            }
            PlayerEvent.PLAY_CHANGE_SEEK->{
                dMarkVideo!!.setSeekProgress(player.seek)
            }
            PlayerEvent.PLAY_OVER->{
                finish()
            }
            PlayerEvent.PLAY_CHANGE_WIDTH_HEIGHT->{
                dMarkVideo!!.setVideoViewLayout(player.seek.toInt())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        DMarkVideoView.onPauseOrRestartVideo()
    }

    override fun onRestart() {
        super.onRestart()
        DMarkVideoView.onPauseOrRestartVideo()
    }

    fun setmPlayerViewCallBack(mPlayerViewCallBack: PlayerViewCallBack) {
        this.mPlayerViewCallBack = mPlayerViewCallBack
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        DMarkVideoView.stopVideo()
        dMarkVideo = null;
    }
}