package com.metawebthree.keykeeper.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.metawebthree.keykeeper.R
import java.time.LocalDateTime

class SyncService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val now = LocalDateTime.now()
        val notification = NotificationCompat.Builder(this, "background_channel").let {
            it.apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
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