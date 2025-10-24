package com.metawebthree.keykeeper.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.provider.Settings
import com.metawebthree.app.broadcast.WatcherForActivity

class AirPlaneModeReceiver : BroadcastReceiver(),
    WatcherForActivity {

    var isTurnedOn: Boolean = false
    override fun onCreate(context: Context) {

    }

    override fun onResume(context: Context) {
        context.registerReceiver(this, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun peekService(myContext: Context?, service: Intent?): IBinder {
        return super.peekService(myContext, service)
    }

    override fun onPause(context: Context) {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            isTurnedOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) != 0
        }
    }

    override fun onDestroy(context: Context) {

    }

}