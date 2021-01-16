package com.mark.cyberpunkplayer.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.net.Uri
import android.provider.Settings
import com.mark.cyberpunkplayer.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission




public class SplashActivity : AppCompatActivity(){

    var mNeedRequestPMSList : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .rationale { context, data, executor ->
                executor.execute()
            }
            .onGranted { permisson->
                jumpToMainActivity()
            }
            .onDenied { permission -> run{
                Toast.makeText(
                    this,
                    "应用缺少SDK运行必须的存储权限！请点击\"应用权限\"，打开所需要的权限。",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                finish()
            }}
            .start()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun jumpToMainActivity(){
        startActivity(object : Intent(SplashActivity@this, MainActivity::class.java){})
        finish()
    }

}