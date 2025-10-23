package com.metawebthree.keykeeper

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metawebthree.keykeeper.broadcast.AirPlaneModeReceiver
import com.metawebthree.keykeeper.broadcast.MediaButtonReceiver
import com.metawebthree.keykeeper.broadcast.WatcherForActivity
import com.metawebthree.keykeeper.broadcast.SmsReceiver
import com.metawebthree.keykeeper.config.ApplicationRoute
import com.metawebthree.keykeeper.ui.screen.SettingPage
import com.metawebthree.keykeeper.ui.screen.StartPage
import com.metawebthree.keykeeper.ui.screen.tabbar.TabBarView
import com.metawebthree.keykeeper.ui.theme.DefaultTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.toColorInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val smsResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission().also {
            it.createIntent(this@MainActivity, Manifest.permission.RECEIVE_SMS)
        }) {
            when (it) {
                false -> Toast.makeText(
                    this,
                    "未授权，无法实现预定的功能：RECEIVE_SMS！",
                    Toast.LENGTH_SHORT
                )
                    .show()

                else -> {}
            }
        }
    private val airPlaneModeReceiver = AirPlaneModeReceiver()
    private val mediaButtonReceiver = MediaButtonReceiver()

    private fun getWatchers(): Array<WatcherForActivity> {
        return arrayOf(airPlaneModeReceiver, mediaButtonReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultTheme {
                val appNavController = rememberNavController()
                NavHost(
                    navController = appNavController,
                    startDestination = ApplicationRoute.START_PAGE
                ) {
                    composable(ApplicationRoute.START_PAGE) {
                        StartPage(navController = appNavController)
                    }
                    composable(ApplicationRoute.HOME_PAGE) {
                        TabBarView(
                            appNavController = appNavController,
                            context = applicationContext
                        )
                    }
                    composable(ApplicationRoute.SETTING_PAGE) {
                        SettingPage(context = applicationContext)
                    }
                }
            }
        }
        getWatchers().forEach {
            it.onCreate(this)
        }
        registry()
        fixHuaweiUI()
    }

    private fun fixHuaweiUI() {
        (this as Activity).window.navigationBarColor = "#0b1729".toColorInt()
    }

    private fun registry() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
        ) {
            smsResultLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_MEDIA_IMAGES
                ),
                0
            )
            NotificationChannel(
                "background_channel",
                "Background sync notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).let { channel ->
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                    createNotificationChannel(channel)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
            when (event?.repeatCount) {
                //  短按
                0 -> Toast.makeText(this, "多媒体按键：短按", Toast.LENGTH_SHORT)
                //  长按
                else -> Toast.makeText(this, "多媒体按键：长按", Toast.LENGTH_SHORT)
            }.show()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        getWatchers().forEach {
            it.onResume(this)
        }
        return super.onResume()
    }

    override fun onPause() {
        getWatchers().forEach {
            it.onPause(this)
        }
        return super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        getWatchers().forEach {
            it.onDestroy(this)
        }
        //创建组件对象
        val smsReceiver = ComponentName(this, SmsReceiver::class.java)
        // 获取包管理器对象禁用一个静态注册的广播接收者
        packageManager.setComponentEnabledSetting(
            smsReceiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}