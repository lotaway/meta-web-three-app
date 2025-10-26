package com.metawebthree.app.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import metawebthreeapp.composeapp.generated.resources.Res
import metawebthreeapp.composeapp.generated.resources.compose_multiplatform
import java.time.LocalDateTime

class SyncService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun start() {
        val now = LocalDateTime.now()
        val notification = NotificationCompat.Builder(this, "background_channel").let {
            it.apply {
//                setSmallIcon(Res.drawable.compose_multiplatform)
                setContentTitle("Sync run is active")
                setContentText("Elapsed time: $now")
            }
            it.build()
        }
        startForeground(1, notification)
    }

    enum class Actions {
        START,
        STOP,
    }

}