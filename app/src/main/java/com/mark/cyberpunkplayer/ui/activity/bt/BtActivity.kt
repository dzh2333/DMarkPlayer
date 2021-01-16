package com.mark.cyberpunkplayer.ui.activity.bt

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mark.cyberpunkplayer.R
import com.mark.cyberpunkplayer.app.BTManager
import com.mark.cyberpunkplayer.base.BaseActivity
import com.mark.cyberpunkplayer.bean.local.BTBean
import com.mark.cyberpunkplayer.bean.local.BTBean.NEARBY_DEVICE
import com.mark.cyberpunkplayer.event.BTEvent
import com.mark.cyberpunkplayer.ui.adapter.BTRVAdapter
import com.mark.cyberpunkplayer.util.ToastUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_bt.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 作用：
 *  选择蓝牙连接的对象
 */
public class BtActivity : BaseActivity(){

    var adapter : BTRVAdapter ?= null
    var list : ArrayList<BTBean> ?= null

    var bro : BTManager.BTBroadcastRceciver ?= null

    val FB_BT_NO = 0
    val FB_BT_REFLESH = 1
    val FB_BT_TRAN_SELECT_FILE = 2

    var fbStatus : Int = FB_BT_NO


    override fun onCreate(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(BtActivity@this)
        super.onCreate(savedInstanceState)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(btEvent: BTEvent){
        when(btEvent.type){
            BTEvent.BT_OPEN ->{
                AndPermission.with(this)
                    .runtime()
                    .permission(Permission.Group.LOCATION)
                    .rationale { context, data, executor ->
                        executor.execute()
                    }
                    .onGranted { permisson->
                        if(BTManager.getInstance().btIsEnabled()){
                            scanBTDevice()
                        }else{
                            ToastUtils.showToast("请打开蓝牙，然后重试")
                        }
                    }
                    .onDenied { permission -> run{
                        ToastUtils.showToast("请通过必须权限，才能正常使用该功能")
                    }}
                    .start()
            }
            BTEvent.BT_CONNECT ->{
                if (btEvent.device.createBond()){
                    ToastUtils.showToast("发送配对请求成功")
                }else{
                    ToastUtils.showToast("发送配对请求失败")
                }
            }
            BTEvent.BT_SCAN_FINISH->{
                ToastUtils.showToast("搜索附近蓝牙设备完毕")
                bt_toolbar.setTitle("附近设备（搜索完毕）")
            }
            BTEvent.BT_SCAN ->{
               scanBTDevice()
            }
            BTEvent.BT_SCAN_RES ->{
                adapter!!.insertItem(BTBean(NEARBY_DEVICE, btEvent.device))
            }
            BTEvent.BT_SELECT_DEVICE ->{
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(BtActivity@this)
        BTManager.getInstance().unRegisterBTBroadcastRceciver(BtActivity@this)
        BTManager.getInstance().stopScanDevice()
    }

    override fun initView() {
        list = ArrayList()
        adapter = BTRVAdapter(BtActivity@this, list)
        adapter!!.setLoading(true)
        var layoutManager : LinearLayoutManager = LinearLayoutManager(BtActivity@this)
        bt_rv.layoutManager = layoutManager
        bt_rv.addItemDecoration(DividerItemDecoration(BtActivity@this, DividerItemDecoration.VERTICAL))
        bt_rv.adapter = adapter

        bt_toolbar.setTitle(getString(R.string.nearby_device))

        bt_float_button.visibility = View.INVISIBLE
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (BTManager.getInstance().btIsEnabled()){
            //搜索BT
            EventBus.getDefault().post(BTEvent(BTEvent.BT_SCAN, null, ""))
        }else{
            adapter!!.setLoading(false)
            adapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 搜索附近蓝牙设备
     */
    private fun scanBTDevice(){
        adapter!!.setLoading(true)
        adapter!!.notifyDataSetChanged()
        BTManager.getInstance().registerBTBroadcastRceciver(BtActivity@this)
        BTManager.getInstance().startScanDevice()
        bt_toolbar.setTitle("搜索设备中...")

        fbStatus = FB_BT_REFLESH
        bt_float_button.visibility = View.VISIBLE

        bt_float_button_layout.setOnClickListener(object  : View.OnClickListener{
            override fun onClick(v: View?) {
                var anim = ObjectAnimator.ofFloat(bt_float_button, "rotation", 0f, 360f)
                anim.setDuration(1000)
                anim.start()
                adapter!!.reSetAdapter()
                EventBus.getDefault().post(BTEvent(BTEvent.BT_SCAN, null, ""))
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt
    }
}