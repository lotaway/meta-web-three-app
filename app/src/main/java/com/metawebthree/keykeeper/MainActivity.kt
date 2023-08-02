package com.metawebthree.keykeeper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.metawebthree.keykeeper.broadcast.SmsReceiver
import com.metawebthree.keykeeper.config.ApplicationRoute
import com.metawebthree.keykeeper.ui.screen.SettingPage
import com.metawebthree.keykeeper.ui.screen.StartPage
import com.metawebthree.keykeeper.ui.screen.tabbar.TabBarView
import com.metawebthree.keykeeper.ui.theme.DefaultTheme
import dagger.hilt.android.AndroidEntryPoint

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
        registry()
    }

    private fun registry() {
        registerReceiver(airPlaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
        ) {
            smsResultLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReceiver)
        //创建组件对象
        val receiver = ComponentName(this, SmsReceiver::class.java)
        // 获取包管理器对象禁用一个静态注册的广播接收者
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}