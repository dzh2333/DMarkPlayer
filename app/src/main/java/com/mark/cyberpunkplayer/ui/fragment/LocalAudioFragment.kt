package com.mark.cyberpunkplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.base.BaseFragment
import com.mark.cyberpunkplayer.base.LazyLoadFragment
import com.mark.cyberpunkplayer.db.AudioBean
import com.mark.cyberpunkplayer.event.AppEvent
import com.mark.cyberpunkplayer.event.PlayerEvent
import com.mark.cyberpunkplayer.event.PlayerEvent.*
import com.mark.cyberpunkplayer.player.DMarkVideoView
import com.mark.cyberpunkplayer.player.audio.AudioPlayerManager
import com.mark.cyberpunkplayer.ui.adapter.LocalAudioRVAdapter
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllAudioViewModel
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllAudioViewModel.AUDIO_SORT_MODE_TIME
import com.mark.cyberpunkplayer.ui.fragment.mvc.MVCCallBack
import com.mark.cyberpunkplayer.util.AppExecutors
import com.mark.cyberpunkplayer.util.ToastUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_local_audio.*
import kotlinx.android.synthetic.main.include_audio_player.*
import kotlinx.android.synthetic.main.include_player_bottom_nv.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

public class LocalAudioFragment : BaseFragment(){

    var audioRVAdapter : LocalAudioRVAdapter? = null
    var musicList : List<AudioBean>? = null
    var musicPlaying : Boolean = false

    var nowPlayIndex : Int = 0

    var allAudioViewModel : AllAudioViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(playerEvent: PlayerEvent){
        when (playerEvent.type){
            PLAY_AUDIO_SEEK_CHANGE -> {
                audio_player_seek.setProgress((playerEvent.seek * 100).toInt())
                player_home_play_or_pause.setImageResource(R.drawable.audiopause)
            }
            PLAY_AUDIO_OVER -> {

            }
            PLAY_AUDIO_PLAY ->{
                musicList!!.get(nowPlayIndex).playing = false
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
                nowPlayIndex = playerEvent.seek.toInt()
                audio_player_play_or_pause.setImageResource(R.drawable.audiopause)
                DMarkVideoView.openUrl(playerEvent.data, AudioPlayerManager(), 1)
                musicList!!.get(nowPlayIndex).playing = true
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun initData() {
        allAudioViewModel = AllAudioViewModel()
        allAudioViewModel!!.getAllAudioCallBack(object :
            MVCCallBack<MutableList<AudioBean>> {
            override fun callback(t: MutableList<AudioBean>?) {
                AppExecutors.getInstance().mainThread().execute(object : Runnable{
                    override fun run() {
                        musicList = t
                        audioRVAdapter!!.changeListData(t)
                    }
                })
            }
        }, AUDIO_SORT_MODE_TIME)
    }

    override fun initView() {
        audio_player_seek.max = 1000
        audio_player_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                DMarkVideoView.changeSeek(seekBar!!.getProgress().toDouble() / seekBar.getMax().toDouble())
                audio_player_play_or_pause.setImageResource(R.drawable.audiopause)
            }
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
            }
        })
        audio_player_play_or_pause.setImageResource(R.drawable.audio_player)
        audio_player_play_or_pause.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val videoIsPause = DMarkVideoView.onPauseOrRestartVideo()
                if (!videoIsPause) {
                    audio_player_play_or_pause.setImageResource(R.drawable.audiopause)
                } else {
                    audio_player_play_or_pause.setImageResource(R.drawable.audio_player)
                }
            }
        })
        audio_player_next.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                nextOrLastMusic(true)
            }
        })
        audio_player_left.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                nextOrLastMusic(false)
            }
        })

        initRV()
    }

    private fun nextOrLastMusic(next : Boolean){
        if (next){
            if ((nowPlayIndex + 1) < musicList!!.size){
                DMarkVideoView.openUrl(musicList!!.get((nowPlayIndex + 1)).path, AudioPlayerManager(), 1)
                musicList!!.get(nowPlayIndex).playing = false
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
                nowPlayIndex += 1
                musicList!!.get(nowPlayIndex).playing = true
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
            }else{
                ToastUtils.showToast("没有更多音频啦")
            }
        }else{
            if (nowPlayIndex <= 0){
                ToastUtils.showToast("没有更多音频啦")
            }else{
                DMarkVideoView.openUrl(musicList!!.get((nowPlayIndex - 1)).path, AudioPlayerManager(), 1)
                musicList!!.get(nowPlayIndex).playing = false
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
                nowPlayIndex -= 1
                musicList!!.get(nowPlayIndex).playing = true
                audioRVAdapter!!.changItem(nowPlayIndex, musicList!!.get(nowPlayIndex))
            }
        }
    }


    private fun initRV(){
        musicList = ArrayList()
        audioRVAdapter = LocalAudioRVAdapter(context!!, musicList!!)
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(context)
        local_audio_rv.layoutManager = linearLayoutManager
        local_audio_rv.adapter = audioRVAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_local_audio
    }


}