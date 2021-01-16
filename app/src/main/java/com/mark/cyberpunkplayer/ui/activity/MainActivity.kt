package com.mark.cyberpunkplayer.ui.activity

import android.content.Intent
import com.mark.cyberpunkplayer.R
import kotlinx.android.synthetic.main.include_home_toolbar.*
import androidx.appcompat.app.ActionBarDrawerToggle
import android.os.*
import android.view.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.FragmentTransaction
import com.mark.cyberpunkplayer.app.AppManager
import com.mark.cyberpunkplayer.base.BaseActivity
import com.mark.cyberpunkplayer.event.AppConstants.*
import com.mark.cyberpunkplayer.event.AppEvent
import com.mark.cyberpunkplayer.service.smb.SmbService
import com.mark.cyberpunkplayer.ui.activity.bt.BTIndexActivity
import com.mark.cyberpunkplayer.ui.fragment.*
import com.mark.cyberpunkplayer.ui.widget.VideoPopWindow
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity() {

    var showIndex : Int = 10

    var localPicFragment : LocalPicFragment ?= null
    var localVideoFragment : LocalVideoFragment ?= null
    var localAudioFragment : LocalAudioFragment ?= null
    var localNetResFragment : LocalNetResFragment ?= null
    var wifiShareFragment : WifiFragment ?= null

    var videoPopWindow : VideoPopWindow ?= null

    override fun initView() {

    }

    fun changeFragment(index : Int , title : String ){
        home_drawerlayout.closeDrawer(home_main_nav)
        setFragment(index)
        showIndex = index
        home_toolbar.setTitle(title)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_local_pic_menu, menu)
        when (showIndex){
            10 ->{
//                menu!!.findItem(R.id.video_menu).setVisible(true)
//                menu.findItem(R.id.pic_menu).setVisible(false)
//                menu.findItem(R.id.audio_menu).setVisible(false)
                menu!!.findItem(R.id.video_sort).setVisible(true)
                menu.findItem(R.id.pic_sort).setVisible(false)
            }
            20 ->{
//                menu!!.findItem(R.id.video_menu).setVisible(false)
//                menu.findItem(R.id.pic_menu).setVisible(true)
//                menu.findItem(R.id.audio_menu).setVisible(false)
                menu!!.findItem(R.id.video_sort).setVisible(false)
                menu.findItem(R.id.pic_sort).setVisible(false)
            }
            30 ->{
//                menu!!.findItem(R.id.video_menu).setVisible(false)
//                menu.findItem(R.id.pic_menu).setVisible(true)
//                menu.findItem(R.id.audio_menu).setVisible(false)
                menu!!.findItem(R.id.video_sort).setVisible(false)
                menu.findItem(R.id.pic_sort).setVisible(false)
            }
            100 ->{
                menu!!.findItem(R.id.video_sort).setVisible(false)
                menu.findItem(R.id.pic_sort).setVisible(false)
            }
            200->{
                menu!!.findItem(R.id.video_sort).setVisible(false)
                menu.findItem(R.id.pic_sort).setVisible(false)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
//            R.id.video_menu ->{
//                EventBus.getDefault().post(AppEvent(HOME_TOOLBAR_LOCAL_VIDEO_REFLASH, ""))
//            }
//            R.id.pic_menu ->{
//                EventBus.getDefault().post(AppEvent(HOME_TOOLBAR_LOCAL_PIC_REFLASH, ""))
//            }
            R.id.video_sort_time ->{
                EventBus.getDefault().post(AppEvent(HOME_VIDEO_SORT_TIME, ""))
            }
            R.id.video_sort_size ->{
                EventBus.getDefault().post(AppEvent(HOME_VIDEO_SORT_SIZE, ""))
            }
            R.id.pic_sort_size ->{
                EventBus.getDefault().post(AppEvent(HOME_PIC_SORT_SIZE, ""))
            }
            R.id.pic_sort_time ->{
                EventBus.getDefault().post(AppEvent(HOME_PIC_SORT_TIME, ""))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(home_toolbar)
        EventBus.getDefault().register(this)
        Logger.d("MainActivityX  onCreate")

        setFragment(10)
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, home_drawerlayout, home_toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle.syncState()
        home_drawerlayout.addDrawerListener(actionBarDrawerToggle)

        home_main_nav.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                invalidateOptionsMenu()
                when (item.title){
                    "本地视频"->{
                        changeFragment(10, getString(R.string.menu_local_video))
                    }
                    "本地图片"->{
                        changeFragment(20, getString(R.string.menu_local_pic))
                    }
                    "本地音频"->{
                        changeFragment(30, getString(R.string.menu_local_audio))
                    }
                    "本地网络资源(BETA)" ->{
                        changeFragment(100, getString(R.string.menu_net_video))
                    }
                    "关于APP" ->{
                        home_drawerlayout.closeDrawer(home_main_nav)
                        startActivity(Intent(AppManager.getActivity(), AboutMeActivity::class.java))
                    }
                    "WIFI传输" ->{
                        changeFragment(200, "WIFI传输")
                    }
                    "蓝牙互传" ->{
                        home_drawerlayout.closeDrawer(home_main_nav)
                        startActivity(Intent(AppManager.getActivity(), BTIndexActivity::class.java))
                    }
                }
                return true
            }
        })

        startService(Intent(MainActivity@this, SmbService::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(appEvent: AppEvent){
        when(appEvent.type){
            HOME_VIDEO_SHOW_POPWINDOW ->{
                videoPopWindow = VideoPopWindow(MainActivity@this, appEvent.data)
                videoPopWindow!!.setBackgroundDrawable(MainActivity@this.getDrawable(R.drawable.popwinow_bg))
                videoPopWindow!!.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
            }
            HOME_VIDEO_DELETE ->{
                if (videoPopWindow != null){
                    videoPopWindow!!.dismiss()
                }
            }

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_BACK ->{

            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("MainActivityX  onDestroy")
        EventBus.getDefault().unregister(this)
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("avcodec-58")
            System.loadLibrary("avfilter-7")
            System.loadLibrary("avformat-58")
            System.loadLibrary("avutil-56")
            System.loadLibrary("swresample-3")
            System.loadLibrary("swscale-5")
            System.loadLibrary("yuv")
        }
    }

    override fun onPause() {
        super.onPause()
        Logger.d("MainActivityX  onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.d("MainActivityX  onStop")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("f_Index", showIndex)
        Logger.d("MainActivityX  onSaveInstanceState")
        Logger.d("MainActivityX  onSaveInstanceState Save   " + showIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.d("MainActivityX  onRestoreInstanceState")
        Logger.d("MainActivityX  onRestoreInstanceState  Get  " + savedInstanceState.getInt("f_Index", 10))

    }

    override fun onStart() {
        super.onStart()
        Logger.d("MainActivityX  onStart")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("MainActivityX  onResume")
    }

    private var currentFragment = 0
    fun setFragment(index: Int) {
        //获取Fragment管理器
        val mFragmentManager = supportFragmentManager
        //开启事务
        val mTransaction = mFragmentManager.beginTransaction()
        //隐藏所有Fragment
        hideFragments(mTransaction)
        when (index) {
            10 ->
                //显示对应Fragment
                if (localVideoFragment == null) {
                    localVideoFragment = LocalVideoFragment()
                    mTransaction.add(
                        R.id.home_container, localVideoFragment!!,
                        "main_fragment"
                    )
                } else {
                    mTransaction.show(localVideoFragment!!)
                }
            20 -> if (localPicFragment == null) {
                localPicFragment = LocalPicFragment()
                mTransaction.add(
                    R.id.home_container, localPicFragment!!,
                    "bao_qu_game_fragment"
                )
                }   else {
                mTransaction.show(localPicFragment!!)
                }
            30 -> if (localAudioFragment == null) {
                localAudioFragment = LocalAudioFragment()
                mTransaction.add(
                    R.id.home_container, localAudioFragment!!,
                    "bao_qu_game_fragment"
                )
            }   else {
                mTransaction.show(localAudioFragment!!)
            }
            100 -> {
                if (localNetResFragment == null){
                    localNetResFragment = LocalNetResFragment()
                    mTransaction.add(
                        R.id.home_container, localNetResFragment!!,
                        "s"
                    )
                }else {
                    mTransaction.show(localNetResFragment!!)
                }
            }
            200 ->{
                if (wifiShareFragment == null){
                    wifiShareFragment = WifiFragment()
                    mTransaction.add(
                        R.id.home_container, wifiShareFragment!!,
                        "s"
                    )
                }else {
                    mTransaction.show(wifiShareFragment!!)
                }
            }
        }
        mTransaction.commitAllowingStateLoss()
        currentFragment = index
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        if (localVideoFragment != null) {
            transaction.hide(localVideoFragment!!)
        }
        if (localPicFragment != null) {
            transaction.hide(localPicFragment!!)
        }
        if (localNetResFragment != null){
            transaction.hide(localNetResFragment!!)
        }
        if (localAudioFragment != null){
            transaction.hide(localAudioFragment!!)
        }
        if (wifiShareFragment != null){
            transaction.hide(wifiShareFragment!!)
        }
    }
}
