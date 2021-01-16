package com.mark.cyberpunkplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.base.BaseFragment
import com.mark.cyberpunkplayer.bean.local.PictureBean
import com.mark.cyberpunkplayer.event.AppConstants.*
import com.mark.cyberpunkplayer.event.AppEvent
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllPicViewModel
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllPicViewModel.PIC_SORT_MODE_SIZE
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllPicViewModel.PIC_SORT_MODE_TIME
import com.mark.cyberpunkplayer.ui.fragment.mvc.MVCCallBack
import com.mark.cyberpunkplayer.ui.adapter.AllPictureRVAdapter
import com.mark.cyberpunkplayer.ui.adapter.AllPictureRVAdapter.PICTURE_MODE_COMMON
import com.mark.cyberpunkplayer.ui.widget.MorePicSelectView
import com.mark.cyberpunkplayer.util.AppExecutors
import kotlinx.android.synthetic.main.fragment_local_pic.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import kotlin.collections.ArrayList

public class LocalPicFragment : BaseFragment(){

    var recyclerView : RecyclerView ?= null
    var allPictureRVAdapter : AllPictureRVAdapter ?= null
    var pictureBeans : List<PictureBean> ?= null

    var allPicViewModel : AllPicViewModel?= null

    var morePicSelectedView : View ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(appEvent: AppEvent){
        when(appEvent.type){
            HOME_TOOLBAR_LOCAL_PIC_REFLASH ->{
                allPicViewModel!!.getAllPicCallBack(object :
                    MVCCallBack<List<PictureBean>> {
                    override fun callback(t: List<PictureBean>?) {
                        AppExecutors.getInstance().mainThread().execute(object : Runnable{
                            override fun run() {
                                allPictureRVAdapter!!.changeListData(t)
                            }
                        })
                    }
                }, PIC_SORT_MODE_TIME)
            }
            HOME_PIC_DELETE ->{
                val file  = File(appEvent.data)
                if (file.exists() && !file.isDirectory){
                    file.delete()
                }
                allPicViewModel!!.getAllPicCallBack(object :
                    MVCCallBack<List<PictureBean>> {
                    override fun callback(t: List<PictureBean>?) {
                        AppExecutors.getInstance().mainThread().execute(object : Runnable{
                            override fun run() {
                                allPictureRVAdapter!!.changeListData(t)
                            }
                        })
                    }
                }, PIC_SORT_MODE_TIME)
            }
            HOME_PIC_MORE_SELECTED ->{
                if (morePicSelectedView == null){
                    morePicSelectedView = MorePicSelectView(context!!)
                    morePicSelectedView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    local_pic_layout.addView(morePicSelectedView)
                }
            }
            HOME_PIC_MORE_NO_SELECTED ->{
                if (morePicSelectedView != null){
                    local_pic_layout.removeView(morePicSelectedView)
                    morePicSelectedView = null
                }
            }
            HOME_PIC_MORE_DELETE ->{
                val removeList = allPictureRVAdapter!!.getFocusedItem()
                for ( i in 0..(removeList.size - 1)){
                    val file =  File(removeList.get(i).path)
                    if (file.exists()){
                        file.delete()
                    }
                }
                allPicViewModel!!.getAllPicCallBack(object :
                    MVCCallBack<List<PictureBean>> {
                    override fun callback(t: List<PictureBean>?) {
                        AppExecutors.getInstance().mainThread().execute(object : Runnable{
                            override fun run() {
                                allPictureRVAdapter!!.changeListData(t)
                            }
                        })
                    }
                }, PIC_SORT_MODE_TIME)
                allPictureRVAdapter!!.setMode(PICTURE_MODE_COMMON)
                EventBus.getDefault().post(AppEvent(HOME_PIC_MORE_NO_SELECTED, ""))
            }
            HOME_PIC_MORE_CANCEL ->{
                allPictureRVAdapter!!.setMode(PICTURE_MODE_COMMON)
                EventBus.getDefault().post(AppEvent(HOME_PIC_MORE_NO_SELECTED, ""))
            }
            HOME_PIC_SORT_TIME ->{
                allPicViewModel!!.getAllPicCallBack(object :
                    MVCCallBack<List<PictureBean>> {
                    override fun callback(t: List<PictureBean>?) {
                        AppExecutors.getInstance().mainThread().execute(object : Runnable{
                            override fun run() {
                                allPictureRVAdapter!!.changeListData(t)
                            }
                        })
                    }
                }, PIC_SORT_MODE_TIME)
            }
            HOME_PIC_SORT_SIZE ->{
                allPicViewModel!!.getAllPicCallBack(object :
                    MVCCallBack<List<PictureBean>> {
                    override fun callback(t: List<PictureBean>?) {
                        AppExecutors.getInstance().mainThread().execute(object : Runnable{
                            override fun run() {
                                allPictureRVAdapter!!.changeListData(t)
                            }
                        })
                    }
                }, PIC_SORT_MODE_SIZE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun initData() {
        allPicViewModel = AllPicViewModel()
        allPictureRVAdapter!!.setLoading(true)
        allPicViewModel!!.getAllPicCallBack(object :
            MVCCallBack<List<PictureBean>> {
            override fun callback(t: List<PictureBean>?) {
                AppExecutors.getInstance().mainThread().execute(object : Runnable{
                    override fun run() {
                        allPictureRVAdapter!!.changeListData(t)
                    }
                })
            }
        },PIC_SORT_MODE_TIME)
    }

    override fun initView() {
        recyclerView = mView.findViewById(R.id.local_pic_rv)
        pictureBeans = ArrayList<PictureBean>()
        allPictureRVAdapter = AllPictureRVAdapter(context, pictureBeans)
        val gridLayoutManager : GridLayoutManager = GridLayoutManager(context, 4)
        recyclerView!!.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?
        recyclerView!!.adapter = allPictureRVAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_local_pic
    }

}