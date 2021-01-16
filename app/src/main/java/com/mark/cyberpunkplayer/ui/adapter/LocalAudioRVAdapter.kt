package com.mark.cyberpunkplayer.ui.adapter

import android.content.Context
import android.graphics.Color
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.base.BaseRecyclerViewAdaper
import com.mark.cyberpunkplayer.db.AudioBean
import com.mark.cyberpunkplayer.event.AppEvent
import com.mark.cyberpunkplayer.event.PlayerEvent
import com.mark.cyberpunkplayer.event.PlayerEvent.PLAY_AUDIO_PLAY
import org.greenrobot.eventbus.EventBus

public class LocalAudioRVAdapter(context : Context, list : List<AudioBean>) : BaseRecyclerViewAdaper<AudioBean>() {

    init {
        mContext = context
        mData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BaseRecyclerViewAdaper.TYPE_LOADING -> return LoadingViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.item_rv_loading, parent, false)
            )
            BaseRecyclerViewAdaper.TYPE_NORMAL -> return NothingViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.item_rv_nothing, parent, false)
            )
        }
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_local_audio, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder){
            val viewHolder : VideoViewHolder = holder
            val audioBean : AudioBean = mData.get(position)
            if (audioBean.playing){
                viewHolder.name.setTextColor(Color.RED)
            }else{
                viewHolder.name.setTextColor(Color.BLACK)
            }
            viewHolder.name.text = audioBean.name
            viewHolder.menu.setOnClickListener(object  : View.OnClickListener{
                override fun onClick(v: View?) {

                }
            })

            viewHolder.layout.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    EventBus.getDefault().post(PlayerEvent(PLAY_AUDIO_PLAY, audioBean.path,
                        position.toDouble()
                    ))
                }
            })
        }
    }

    public fun changItem(index : Int, audioBean: AudioBean){
        mData.set(index, audioBean)
        notifyItemChanged(index)
    }


    private class VideoViewHolder(itemView: View) :
        BaseRecyclerViewAdaper.BaseViewHolder(itemView) {
        val name: TextView
        val time: TextView
        val menu: ImageView
        val layout: ConstraintLayout
        init {
            name = itemView.findViewById(R.id.item_audio_name)
            time = itemView.findViewById(R.id.item_audio_time)
            menu = itemView.findViewById(R.id.item_audio_menu)
            layout = itemView.findViewById(R.id.item_audio_layout)
        }
    }

    private class LoadingViewHolder public constructor(itemView: View) :
        BaseRecyclerViewAdaper.BaseViewHolder(itemView)

    private class NothingViewHolder public constructor(itemView: View) :
        BaseRecyclerViewAdaper.BaseViewHolder(itemView)
}