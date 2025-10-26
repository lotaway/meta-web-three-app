package com.metawebthree.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metawebthree.app.broadcast.SmsReceiver
import com.metawebthree.app.broadcast.WatcherForActivity
import com.metawebthree.app.broadcast.AirPlaneModeReceiver
import com.metawebthree.app.broadcast.MediaButtonReceiver
import com.metawebthree.app.config.ApplicationRoute
import com.metawebthree.app.ui.screen.SettingPage
import com.metawebthree.app.ui.screen.StartPage
import com.metawebthree.app.ui.screen.tabbar.TabBarView
import com.metawebthree.app.ui.theme.DefaultTheme

class MainActivity : ComponentActivity() {

    private var smsResultLauncherBuilder: SMSLauncherBuilder? = null

    private var airPlaneModeReceiver: WatcherForActivity? = null
    private var mediaButtonReceiver: WatcherForActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
//                    composable(ApplicationRoute.HOME_PAGE) {
//                        TabBarView(
//                            appNavController = appNavController,
//                            context = applicationContext
//                        )
//                    }
//                    composable(ApplicationRoute.SETTING_PAGE) {
//                        SettingPage(context = applicationContext)
//                    }
                }
            }
//            App()
        }
//        getWatchers().forEach {
//            it.onCreate(this)
//        }
//        registry()
//        fixUI()
    }

    private fun getWatchers(): Array<WatcherForActivity> {
        if (airPlaneModeReceiver == null) {
            airPlaneModeReceiver = AirPlaneModeReceiver()
        }
        if (mediaButtonReceiver == null) {
            mediaButtonReceiver = MediaButtonReceiver()
        }
        return arrayOf(
            airPlaneModeReceiver as WatcherForActivity,
            mediaButtonReceiver as WatcherForActivity
        )
    }

    private fun fixUI() {
        when {
            Build.MANUFACTURER.contains("huawei", ignoreCase = true) -> fixHuaweiUI()

            else -> {
                // do nothing
            }
        }
    }

    private fun fixHuaweiUI() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA -> {
//                WindowCompat.getInsetsController(
//                    this.window,
//                    this.window.decorView
//                ).isAppearanceLightStatusBars = true
                this.window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

            else -> {
//                val color = MaterialTheme.colorScheme.background
                this.window.navigationBarColor = "#0b1729".toColorInt()
//                      this.window.navigationBarColor = getColor(R.color.bg_color)
            }
        }
    }

    private fun registry() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
        ) {
            if (smsResultLauncherBuilder == null) {
                smsResultLauncherBuilder = AndroidSMSResultLauncher().apply {
                    init(this@MainActivity)
                }
            }
            val smsResultLauncher = smsResultLauncherBuilder?.build()
            smsResultLauncher?.launch(Manifest.permission.RECEIVE_SMS)
        }
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
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

            else -> {
                // do nothing
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
            when (event?.repeatCount) {
                //  短按
                0 -> Toast.makeText(this, "This is not video detail page", Toast.LENGTH_SHORT)
                //  长按
                else -> Toast.makeText(this, "Please use back button to quit", Toast.LENGTH_SHORT)
            }.show()
        }
        return super.onKeyDown(keyCode, event)
    }

//    override fun onResume() {
//        getWatchers().forEach {
//            it.onResume(this)
//        }
//        return super.onResume()
//    }

//    override fun onPause() {
//        getWatchers().forEach {
//            it.onPause(this)
//        }
//        return super.onPause()
//    }

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

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}